package com.moxun.service.impl;

import com.moxun.Pojo.Dto.LoginDTO;
import com.moxun.Pojo.Dto.UserUpdateDTO;
import com.moxun.Pojo.Entity.User;
import com.moxun.Pojo.Vo.LoginResultVO;
import com.moxun.Pojo.Vo.UserProfileVO;
import com.moxun.mapper.AuthMapper;
import com.moxun.service.AuthService;
import com.moxun.util.Jwt;
import com.moxun.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    
    // 使用线程安全的Map记录登录失败次数（用户名 -> 失败次数）
    private static final Map<String, Integer> loginFailCount = new ConcurrentHashMap<>();

    //剩余过期时间
    private int Remaining = 0;
    @Autowired
    private AuthMapper authMapper;

    /**
     * 登录
     * @param username
     * @param password
     * @param ipAddress
     * @return
     */
    @Override
    public LoginResultVO CommonLogin(String username, String password, String ipAddress) {
        User loginUser = authMapper.CommonLogin(username);
        log.info("根据用户名查询用户信息{}", loginUser);
        
        if (loginUser == null){
            throw new RuntimeException("用户不存在，请重新输入用户名");
        }
        
        // 检查账号状态
        if (loginUser.getStatus() == 0){
            throw new RuntimeException("用户被禁用，请联系管理员");
        }
        if (loginUser.getStatus() == 2 ){
            //判断是否过期
            if (loginUser.getPasswordExpireTime().isAfter(LocalDateTime.now())){
                Remaining = (int)LocalDateTime.now().until(loginUser.getPasswordExpireTime(), ChronoUnit.MINUTES);
                log.warn("还剩{}分钟",loginUser.getPasswordExpireTime().until(LocalDateTime.now(), ChronoUnit.MINUTES));
                throw new RuntimeException("登录失败次数过多，请"+Remaining+"分钟后重试");
            }else {
                loginUser.setStatus(1);
                authMapper.setStatus(loginUser);
            }
        }

        if (!loginUser.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))){
            // 累加失败次数
            int failCount = loginFailCount.getOrDefault(loginUser.getUsername(), 0) + 1;
            loginFailCount.put(loginUser.getUsername(), failCount);
            log.info("用户[{}]登录失败次数：{}", loginUser.getUsername(), failCount);
            
            // 失败3次后锁定账号
            if (failCount >= 3){
                log.warn("用户[{}]登录失败次数达到3次，锁定账号", loginUser.getUsername());
                //获取当前时间10分钟后
                loginUser.setPasswordExpireTime(LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
                loginUser.setStatus(2);
                authMapper.setStatus(loginUser);
            }
            throw new RuntimeException("密码错误，请重新输入密码");
        }
        
        // 登录成功，清除失败次数
        loginFailCount.remove(loginUser.getUsername());
        log.info("用户[{}]登录成功，清除失败计数", loginUser.getUsername());

        //修改更新时间、最后登录ip
        loginUser.setUpdateTime(LocalDateTime.now());
        loginUser.setLastLoginIp(ipAddress);
        authMapper.modifyUpdateTime(loginUser);


        
        // 生成token
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", loginUser.getId());
        claims.put("userName", loginUser.getUsername());
        String token = Jwt.generateToken(claims);
        log.info("生成token：{}", token);
        
        LoginResultVO loginResultVO = LoginResultVO.builder()
                .userId(loginUser.getId())
                .username(loginUser.getUsername())
                .avatar(loginUser.getAvatar())
                .token(token)
                .build();
        return loginResultVO;
    }

    /**
     * 注册
     * @param loginDTO
     */
    @Override
    public void CommonRegister(LoginDTO loginDTO) {
        User username = authMapper.CommonLogin(loginDTO.getUsername());
        if (username != null){
            throw new RuntimeException("用户已存在，请重新输入用户名");
        }
        User user = new User();
        BeanUtils.copyProperties(loginDTO, user);

        user.setStatus(1);
        //密码使用默认密码(加密)
        user.setPassword(DigestUtils.md5DigestAsHex(loginDTO.getPassword().getBytes()));

        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        authMapper.CommonRegister(user);
    }

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    @Override
    public UserProfileVO getUserInfo(Integer id) {
        UserProfileVO userInfo = authMapper.getUserInfo(id);
        return userInfo;
    }

    /**
     * 修改用户信息
     * @param userUpdateDTO
     */
    @Override
    public void modifyUpdateUser(UserUpdateDTO userUpdateDTO) {
        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO, user);
        Map<String, Object> currentUser = UserContext.getCurrentUser();
        user.setId(Long.valueOf(currentUser.get("userId").toString()));
        user.setUpdateTime(LocalDateTime.now());
        authMapper.modifyUpdateUser(user);
    }

    /**
     * 上传用户头像路径保存到数据库
     * @param upload
     * @return
     */
    @Override
    public void uploadUserAvatar(String upload) {
        String userName = null;
        try {
            Map<String, Object> currentUser = UserContext.getCurrentUser();
            userName = currentUser.get("userName").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("用户名{}", userName);
        if (upload.trim()!= "" && upload != null){
            authMapper.uploadUserAvatar(upload, userName);
        }
    }
}
