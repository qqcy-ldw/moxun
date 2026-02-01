package com.moxun.Enum;

import lombok.Getter;

@Getter
public enum ResultCode {
    /**
     * 成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 失败
     */
    ERROR(500, "操作失败"),

    /**
     * 参数错误
     */
    PARAM_ERROR(400, "参数错误"),

    /**
     * 未认证
     */
    UNAUTHORIZED(401, "未认证"),

    /**
     * 无权限
     */
    FORBIDDEN(403, "无权限"),

    /**
     * 资源未找到
     */
    NOT_FOUND(404, "资源未找到"),

    /**
     * 请求方法不支持
     */
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),

    /**
     * 请求过于频繁
     */
    TOO_MANY_REQUESTS(429, "请求过于频繁"),

    /**
     * 服务器内部错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),

    /**
     * 服务不可用
     */
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    /**
     * 业务异常
     */
    BUSINESS_ERROR(1000, "业务异常"),

    /**
     * 数据已存在
     */
    DATA_EXISTS(1001, "数据已存在"),

    /**
     * 数据不存在
     */
    DATA_NOT_EXISTS(1002, "数据不存在"),

    /**
     * 数据重复
     */
    DATA_DUPLICATE(1003, "数据重复"),

    /**
     * 验证码错误
     */
    CAPTCHA_ERROR(1004, "验证码错误"),

    /**
     * 用户名或密码错误
     */
    LOGIN_ERROR(1005, "用户名或密码错误"),

    /**
     * Token过期
     */
    TOKEN_EXPIRED(1006, "Token已过期"),

    /**
     * Token无效
     */
    TOKEN_INVALID(1007, "Token无效"),

    /**
     * 文件上传失败
     */
    FILE_UPLOAD_ERROR(1008, "文件上传失败"),

    /**
     * 文件类型不支持
     */
    FILE_TYPE_NOT_SUPPORT(1009, "文件类型不支持"),

    /**
     * 文件大小超限
     */
    FILE_SIZE_EXCEED(1010, "文件大小超限"),

    /**
     * 账号已被锁定
     */
    ACCOUNT_LOCKED(1011, "账号已被锁定"),

    /**
     * 账号已被禁用
     */
    ACCOUNT_DISABLED(1012, "账号已被禁用"),

    /**
     * 用户登录失败
     */
    USER_LOGIN_ERROR(4000, "用户登录失败"),

    /**
     * 数据库操作失败
     */
    DATABASE_ERROR(4001, "数据库操作失败"),

    /**
     * 数据已存在
     */
    DATA_EXIST(4002, "数据已存在"),

    /**
     * 数据不存在
     */
    DATA_NOT_FOUND(4003, "数据不存在");


    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据code获取枚举
     */
    public static ResultCode getByCode(Integer code) {
        for (ResultCode resultCode : ResultCode.values()) {
            if (resultCode.getCode().equals(code)) {
                return resultCode;
            }
        }
        return ERROR;
    }
}