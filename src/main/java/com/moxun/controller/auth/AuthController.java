package com.moxun.controller.auth;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.moxun.Enum.Folder;
import com.moxun.Enum.ResultCode;
import com.moxun.Pojo.Dto.LoginDTO;
import com.moxun.Pojo.Dto.UserUpdateDTO;
import com.moxun.Pojo.Vo.LoginResultVO;
import com.moxun.Pojo.Vo.UserProfileVO;
import com.moxun.exception.BusinessException;
import com.moxun.service.auth.AuthService;
import com.moxun.service.auth.FileService;
import com.moxun.util.IpUtil;
import com.moxun.util.Result;
import com.moxun.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证控制器
 * 处理登录、注册等认证相关请求
 */
@Slf4j
@RestController
@RequestMapping("/auth/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private FileService fileService;

    /**
     * 验证码session key
     */
    private final static String SESSION_KEY = "captcha";

    //T0D0: 后续使用redis存储验证码
    private final ConcurrentHashMap map = new ConcurrentHashMap();

    /**
     * 用户登录
     * @param loginDTO
     * @return
     */
    @PostMapping("/login")
    public Result CommonLogin(@RequestBody LoginDTO loginDTO, HttpServletRequest request) throws Exception {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        String captcha = loginDTO.getCaptcha();
        String ipAddress = IpUtil.getClientIpAddress(request);
        log.info("登录用户：" + username);
        log.info("验证码：" + captcha);
        //TODO目前只校验了验证码，后续需要完善
        if (!captcha.equals(map.get(SESSION_KEY))){
            throw new BusinessException(ResultCode.CAPTCHA_ERROR,"验证码错误");
        }
        LoginResultVO loginResultVO = authService.CommonLogin(username, password,ipAddress);
        log.info("登录结果：" + loginResultVO);
        return Result.success(loginResultVO);
    }

    /**
     *
     * 用户注册
     * @param loginDTO
     * @return
     */
    //TODO 完善注册功能：用户名称
    @PostMapping("/register")
    public Result CommonRegister(@RequestBody LoginDTO loginDTO){
        authService.CommonRegister(loginDTO);
        log.info("用户注册：" + loginDTO.toString());
        return Result.success();
    }

    /**
     * 生成验证码
     * @param response
     * @throws IOException
     */
    @GetMapping("/captcha")
    public void generateCaptcha(HttpServletResponse  response) throws IOException {
        CircleCaptcha CircleCaptcha = CaptchaUtil.createCircleCaptcha(200, 100,4,20);
        String code = CircleCaptcha.getCode();
        log.info("验证码：" + code);
        CircleCaptcha.write(response.getOutputStream());
        map.put(SESSION_KEY,code);
        response.setHeader(SESSION_KEY,code);
    }

    /**
     * 获取登录IP地址
     */
    @GetMapping("/getLoginIp")
    public String getLoginIp(HttpServletRequest request){
        String clientIpAddress = IpUtil.getClientIpAddress(request);
        log.info("登录IP地址：" + clientIpAddress);
        return clientIpAddress;
    }


    /**
     * 获取当前登录用户信息
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me/{id}")
    public Result<UserProfileVO> getUserInfo(@PathVariable Integer id){
        log.info("获取用户信息：" + id);
        UserProfileVO userinfo = authService.getUserInfo(id);
        log.info("登录用户信息{}" + userinfo);
        return Result.success(userinfo);
    }

    /**
     * 更新用户信息
     * @param UserUpdateDTO
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/users/{id}")
    public Result updateUserInfo(@RequestBody UserUpdateDTO UserUpdateDTO){
        log.info("更新用户信息：" + UserUpdateDTO);
        authService.modifyUpdateUser(UserUpdateDTO);
        return Result.success();
    }


    /**
     * 上传用户头像
     * @param file
     * @return
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/upload/user/avatar")
    public Result<String> uploadUserAvatar(@RequestParam("file") MultipartFile file){
        log.info("上传用户头像：" + file.getOriginalFilename());
        String upload = fileService.upload(file, Folder.AVATAR.getValue());
        authService.uploadUserAvatar(upload);
        return Result.success();
    }

    /**
     * 退出登录
     * @return 成功/失败
     */
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public Result<Void> logout() {
        // 获取当前用户ID
        Map<String, Object> map = UserContext.getCurrentUser();
        String userName =  map.get("userName").toString();
        log.info("用户退出登录：{}", userName);

        // 清除ThreadLocal
        UserContext.clear();

        return Result.success();
    }

    /**
     * 用户注销
     * @return
     */
    @GetMapping("/delUserName")
    public Result<Void> delUserName(){
        UserContext.clear();
        log.info("用户注销");
        return Result.success();
    }


}
