package com.moxun.controller;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.moxun.Enum.Folder;
import com.moxun.Pojo.Dto.LoginDTO;
import com.moxun.Pojo.Dto.UserUpdateDTO;
import com.moxun.Pojo.Vo.LoginResultVO;
import com.moxun.Pojo.Vo.UserProfileVO;
import com.moxun.service.AuthService;
import com.moxun.service.FileService;
import com.moxun.util.IpUtil;
import com.moxun.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 认证控制器
 * 处理登录、注册等认证相关请求
 */
@Slf4j
@RestController
@RequestMapping("/auth/api/v1")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private FileService fileService;

    private final static String SESSION_KEY = "captcha";

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Result CommonLogin(String username,
                              String password,
                              String captcha,
                              HttpServletRequest request){
        String header = request.getHeader(SESSION_KEY);
        String ipAddress = IpUtil.getClientIpAddress(request);
        log.info("用户名{},密码{},验证码{},IP地址{}", username, password, captcha,ipAddress);
        log.info("验证码：" + header);
        if (!header.equals(captcha)){
            throw new RuntimeException("验证码错误");
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
    @PutMapping("/users/{id}")
    public Result updateUserInfo(@RequestBody UserUpdateDTO UserUpdateDTO){
        log.info("更新用户信息：" + UserUpdateDTO);
        authService.modifyUpdateUser(UserUpdateDTO);
        return Result.success();
    }


    @PostMapping("/upload/user/avatar")
    public Result<String> uploadUserAvatar(@RequestParam("file") MultipartFile file){
        log.info("上传用户头像：" + file.getOriginalFilename());
        String upload = fileService.upload(file, Folder.AVATAR.getValue());
        authService.uploadUserAvatar(upload);
        return Result.success();
    }

}
