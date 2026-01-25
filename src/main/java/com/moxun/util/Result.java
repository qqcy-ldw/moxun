package com.moxun.util;


import com.moxun.Enum.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 请求ID（用于日志追踪）
     */
    private String requestId;

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 成功结果（无数据）
     */
    public static <T> Result<T> success() {
        return new Result<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage());
    }

    /**
     * 成功结果（带数据）
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功结果（自定义消息）
     */
    public static <T> Result<T> success(String message) {
        return new Result<T>(ResultCode.SUCCESS.getCode(), message);
    }

    /**
     * 成功结果（自定义消息和数据）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<T>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 失败结果
     */
    public static <T> Result<T> error() {
        return new Result<T>(ResultCode.ERROR.getCode(), ResultCode.ERROR.getMessage());
    }

    /**
     * 失败结果（自定义消息）
     */
    public static <T> Result<T> error(String message) {
        return new Result<T>(ResultCode.ERROR.getCode(), message);
    }

    /**
     * 失败结果（自定义状态码和消息）
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<T>(code, message);
    }

    /**
     * 失败结果（使用预定义状态码）
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<T>(resultCode.getCode(), resultCode.getMessage());
    }

    /**
     * 失败结果（使用预定义状态码，自定义消息）
     */
    public static <T> Result<T> error(ResultCode resultCode, String message) {
        return new Result<T>(resultCode.getCode(), message);
    }

    /**
     * 失败结果（自定义状态码、消息和数据）
     */
    public static <T> Result<T> error(Integer code, String message, T data) {
        return new Result<T>(code, message, data);
    }

    /**
     * 设置请求ID
     */
    public String setRequestId(String requestId) {
        this.requestId = requestId;
        return this.requestId;
    }

}
