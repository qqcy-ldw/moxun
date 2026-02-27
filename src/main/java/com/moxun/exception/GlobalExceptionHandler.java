package com.moxun.exception;


import com.moxun.Enum.ResultCode;
import com.moxun.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.util.stream.Collectors;
/**
 * 全局异常处理器
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
    public Result<?> handleBusinessException(BusinessException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.warn("业务异常: {}, URL: {}", e.getMessage(), request.getRequestURL());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
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
     * 处理 Spring Security 权限不足异常
     * 
     * 【重要说明】
     * @PreAuthorize 抛出的 AccessDeniedException 是在方法执行时通过 AOP 抛出的，
     * 不会经过 Spring Security 的过滤器链，所以不会被 accessDeniedHandler 处理。
     * 因此，@PreAuthorize 的异常必须在这里处理。
     * 
     * 【异常处理流程】
     * 1. URL 级别权限控制（authorizeHttpRequests）→ accessDeniedHandler 处理
     * 2. 方法级别权限控制（@PreAuthorize）→ GlobalExceptionHandler 处理（这里）
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result<?> handleAccessDeniedException(
            AccessDeniedException e,
            HttpServletRequest request,
            HttpServletResponse response) {
        log.warn("权限不足访问（方法级权限控制）: {}, URL: {}", e.getMessage(), request.getRequestURL());
        // 设置 HTTP 状态码为 403，与 accessDeniedHandler 保持一致
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return Result.error(ResultCode.FORBIDDEN);
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
