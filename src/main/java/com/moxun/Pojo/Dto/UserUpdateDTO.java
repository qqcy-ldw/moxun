package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

/**
 * 用户资料更新请求DTO
 */
public class UserUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 昵称/用户名
     */
    @Size(min = 3, max = 50, message = "用户名长度在3-50个字符")
    private String username;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别:1-男，0-女
     */
    private Integer gender;

    /**
     * 头像路径
     */
    private String avatar;
}
