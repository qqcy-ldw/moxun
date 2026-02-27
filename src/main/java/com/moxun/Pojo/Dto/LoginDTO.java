package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录请求DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度在3-50个字符")
    private String username;

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @Size(min = 2, max = 50, message = "真实姓名长度在2-50个字符")
    private String realName;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度在6-20个字符")
    private String password;

    /**
     * 邮箱
     */
    @Email
    private String email;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\\\d{9}$", message = "手机格式不正确")
    private Integer phone;

    /**
     * 性别:1-男，0-女
     */
    @NotBlank(message = "性别不能为空")
    private Integer gender;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 头像路径
     */
    private String avatar;

    /**
     * 验证码
     */
    private String captcha;

    /**
     * 验证码key
     */
    private String captchaKey;




}

