package com.moxun.exception;

import com.moxun.Enum.ResultCode;
import lombok.Getter;

/**
 * 业务异常类
 * 用于处理业务逻辑中的异常情况（如登录失败、参数错误等）
 * 与系统异常（RuntimeException、SQLException）区分开
 */
@Getter
public class BusinessException extends RuntimeException{
    private Integer code;
    private Object data;

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }

    public BusinessException(String message, Object data) {
        super(message);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
        this.data = data;
    }

    public BusinessException(Integer code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }
}
