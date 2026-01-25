package com.moxun.service.auth.impl;

import com.moxun.Enum.ResultCode;
import com.moxun.Pojo.Dto.LoginDTO;
import com.moxun.Pojo.Dto.UserUpdateDTO;
import com.moxun.Pojo.Entity.User;
import com.moxun.Pojo.Vo.LoginResultVO;
import com.moxun.Pojo.Vo.UserProfileVO;
import com.moxun.exception.BusinessException;
import com.moxun.mapper.auth.AuthMapper;
import com.moxun.service.auth.AuthService;
import com.moxun.util.Jwt;
import com.moxun.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证服务实现类
 * 
 * 【重要改动】
 * 1. 使用Spring Security的AuthenticationManager进行认证
 * 2. 使用PasswordEncoder处理密码（支持MD5和BCrypt）
 * 3. 认证成功后生成JWT Token
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    ConcurrentHashMap<String, Integer> loginFailCount = new ConcurrentHashMap<>();

    /**
     * 用户登录
     * 
     * 【流程】
     * 1. 创建认证令牌（用户名+原始密码）
     * 2. 调用AuthenticationManager进行认证
     *    - 内部会调用UserDetailsService加载用户
     *    - 使用PasswordEncoder验证密码
     *    - 检查账号状态（禁用、锁定）
     * 3. 认证成功，生成JWT Token
     * 4. 更新最后登录信息
     * 
     * @param username 用户名
     * @param password 原始密码（未加密）
     * @param ipAddress 登录IP
     * @return 登录结果（包含Token）
     */
    @Override
    public LoginResultVO CommonLogin(String username, String password, String ipAddress) {
        log.info("用户登录请求 - 用户名: {}, IP: {}", username, ipAddress);

        try {
            // 1. 创建认证令牌
            // 注意：这里传入的是原始密码，Spring Security会自动使用PasswordEncoder验证
            UsernamePasswordAuthenticationToken authenticationToken = 
                    new UsernamePasswordAuthenticationToken(username, password);

            // 2. 执行认证
            // 如果认证失败，会抛出异常：
            // - BadCredentialsException: 密码错误
            // - DisabledException: 账号被禁用
            // - LockedException: 账号被锁定
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            log.info("认证成功 - 用户: {}, 权限: {}", username, authentication.getAuthorities());

            // 3. 认证成功，清除失败次数
            loginFailCount.remove(username);
            log.info("用户[{}]登录成功，清除失败计数", username);

            // 4. 查询用户完整信息
            User loginUser = authMapper.CommonLogin(username);

            // 5. 更新最后登录信息
            loginUser.setUpdateTime(LocalDateTime.now());
            loginUser.setLastLoginIp(ipAddress);
            loginUser.setLastLoginTime(LocalDateTime.now());
            authMapper.modifyUpdateTime(loginUser);

            // 6. 生成JWT Token
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("userId", loginUser.getId());
            claims.put("userName", loginUser.getUsername());
            String token = Jwt.generateToken(claims);

            log.info("JWT Token生成成功 - 用户: {}", username);

            // 7. 返回登录结果
            return LoginResultVO.builder()
                    .userId(loginUser.getId())
                    .username(loginUser.getUsername())
                    .realName(loginUser.getRealName())
                    .avatar(loginUser.getAvatar())
                    .token(token)
                    .build();

        } catch (Exception e) {
            log.error("登录失败 - 用户: {}, 错误: {}", username, e.getMessage());
            
            // 1. 记录失败次数（累加）
            int failCount = loginFailCount.getOrDefault(username, 0) + 1;
            loginFailCount.put(username, failCount);
            log.info("用户[{}]登录失败次数：{}", username, failCount);

            // 2. 失败3次后锁定账号
            if (failCount >= 3) {
                log.warn("用户[{}]登录失败次数达到3次，锁定账号10分钟", username);
                
                try {
                    // 获取当前时间10分钟后
                    User user = new User();
                    user.setUsername(username);  // 必须设置username，SQL需要知道更新哪个用户
                    user.setPasswordExpireTime(LocalDateTime.now().plus(10, ChronoUnit.MINUTES));
                    user.setStatus(2);  // 2表示锁定状态
                    authMapper.setStatus(user);
                    
                    log.info("用户[{}]已锁定，锁定至：{}", username, user.getPasswordExpireTime());
                } catch (Exception ex) {
                    log.error("锁定用户[{}]失败：{}", username, ex.getMessage());
                }
                
                // 抛出锁定异常，告知用户账号已锁定
                throw new BusinessException(ResultCode.ACCOUNT_LOCKED, "登录失败次数过多，账号已锁定10分钟");
            }
            
            // 3. 抛出登录失败异常
            throw new BusinessException(ResultCode.LOGIN_ERROR, "用户名或密码错误（剩余尝试次数：" + (3 - failCount) + "）");
        }
    }

    /**
     * 根据用户名查询用户信息
     */
    @Override
    public User getByUserName(String username) {
        return authMapper.getByUserName(username);
    }

    /**
     * 用户注册
     * 
     * 【密码处理】
     * 新用户使用BCrypt加密，格式：{bcrypt}$2a$10$xxxxx
     * 
     * @param loginDTO 注册信息
     */
    @Override
    public void CommonRegister(LoginDTO loginDTO) {
        log.info("用户注册请求 - 用户名: {}", loginDTO.getUsername());

        // 1. 检查用户名是否已存在
        User existUser = authMapper.CommonLogin(loginDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException(ResultCode.DATA_EXISTS, "用户名已存在");
        }

        // 2. 创建用户对象
        User user = new User();
        BeanUtils.copyProperties(loginDTO, user);
        user.setStatus(1);  // 正常状态

        // 3. 加密密码（使用BCrypt）
        // encode()会自动添加{bcrypt}前缀
        String encodedPassword = passwordEncoder.encode(loginDTO.getPassword());
        user.setPassword(encodedPassword);

        log.info("密码加密完成 - 加密后: {}", encodedPassword);

        // 4. 设置时间
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        // 5. 保存用户
        authMapper.CommonRegister(user);
        
        log.info("用户注册成功 - 用户名: {}", loginDTO.getUsername());
    }

    /**
     * 获取用户信息
     */
    @Override
    public UserProfileVO getUserInfo(Integer id) {
        return authMapper.getUserInfo(id);
    }

    /**
     * 修改用户信息
     */
    @Override
    public void modifyUpdateUser(UserUpdateDTO userUpdateDTO) {
        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO, user);
        
        Map<String, Object> currentUser = UserContext.getCurrentUser();
        user.setId(Long.valueOf(currentUser.get("userId").toString()));
        user.setUpdateTime(LocalDateTime.now());
        
        authMapper.modifyUpdateUser(user);
        log.info("用户信息更新成功 - 用户ID: {}", user.getId());
    }

    /**
     * 上传用户头像
     */
    @Override
    public void uploadUserAvatar(String upload) {
        try {
            Map<String, Object> currentUser = UserContext.getCurrentUser();
            String userName = currentUser.get("userName").toString();
            
            if (upload != null && !upload.trim().isEmpty()) {
                authMapper.uploadUserAvatar(upload, userName);
                log.info("用户头像更新成功 - 用户: {}, URL: {}", userName, upload);
            }
        } catch (Exception e) {
            log.error("用户头像上传失败: {}", e.getMessage());
            throw new BusinessException(ResultCode.FILE_UPLOAD_ERROR, "头像上传失败");
        }
    }
}
