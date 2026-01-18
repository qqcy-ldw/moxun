package com.moxun.exception;


import com.moxun.Enum.ResultCode;
import com.moxun.Pojo.Vo.ResultVO;
import com.moxun.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public static Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
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
    public static Result<?> handleBindException(BindException e, HttpServletRequest request) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("参数绑定异常: {}, URL: {}", message, request.getRequestURL());
        return Result.error(ResultCode.PARAM_ERROR.getCode(), message);
    }


    /**
     * 处理404异常
     */
//    @ExceptionHandler(NoResourceFoundException.class)
//    public static Result<?> handleNoHandlerFoundException(NoResourceFoundException e, HttpServletRequest request) {
//        log.warn("接口不存在: {}, URL: {}", e.getMessage(), request.getRequestURL());
//        return Result.error(ResultCode.NOT_FOUND);
//    }

    /**
     * 处理其他所有异常
     */
//    @ExceptionHandler(Exception.class)
//    public static Result<?> handleException(Exception e, HttpServletRequest request) {
//        log.error("系统异常: {}, URL: {}", e.getMessage(), request.getRequestURL(), e);
//        return Result.error(ResultCode.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResultVO> handleRuntimeException(RuntimeException e) {
        // 返回统一的错误格式
        return ResponseEntity.badRequest()
                .body(ResultVO.builder()
                        .code(ResultCode.USER_LOGIN_ERROR.getCode())
                        .message(e.getMessage())
                        .data(null)
                        .build());
    }
}
