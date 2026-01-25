package com.moxun.exception;


import com.moxun.Enum.ResultCode;
import com.moxun.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;


/**
 * 全局异常处理器
 * 
 * 【异常处理优先级】
 * 1. 参数校验异常（MethodArgumentNotValidException）
 * 2. 参数绑定异常（BindException）
 * 3. 业务异常（BusinessException）- 返回具体错误信息
 * 4. 404异常（NoResourceFoundException）
 * 5. 系统异常（Exception）- 返回统一500错误
 * 
 * 【重要】所有方法必须是非静态的，否则无法正确返回响应给前端
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数校验异常: {}, URL: {}", message, request.getRequestURL());
        return Result.error(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数绑定异常: {}, URL: {}", message, request.getRequestURL());
        return Result.error(ResultCode.PARAM_ERROR.getCode(), message);
    }

    /**
     * 处理业务异常
     * 业务异常通常是预期内的异常，如登录失败、参数校验失败等
     * 需要将具体的错误信息返回给前端
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常: {}, URL: {}", e.getMessage(), request.getRequestURL());
        if (e.getData() != null) {
            return Result.error(e.getCode(), e.getMessage(), e.getData());
        }
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public Result<?> handleNoHandlerFoundException(NoResourceFoundException e, HttpServletRequest request) {
        log.warn("接口不存在: {}, URL: {}", e.getMessage(), request.getRequestURL());
        return Result.error(ResultCode.NOT_FOUND);
    }

    /**
     * 处理其他所有异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e, HttpServletRequest request) {
        log.error("系统异常: {}, URL: {}", e.getMessage(), request.getRequestURL(), e);
        return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
    }

}
