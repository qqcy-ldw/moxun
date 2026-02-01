package com.moxun.exception;

import com.moxun.Enum.ResultCode;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库约束异常处理器
 * 专门处理数据库操作相关的异常
 */
@Slf4j
@RestControllerAdvice
public class SqlException {
    /**
     * 处理数据完整性约束异常（字段缺失、外键约束等）
     * 包括：Field 'xxx' doesn't have a default value
     *      Duplicate entry for key
     *      Cannot add or update a child row
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("数据完整性约束异常: {}", e.getMessage(), e);

        // 获取原始SQL异常
        Throwable cause = e.getCause();
        String errorMessage = "数据库操作失败";

        if (cause != null) {
            String message = cause.getMessage();

            if (message.contains("doesn't have a default value")) {
                // 提取字段名
                String fieldName = extractFieldName(message, "doesn't have a default value");
                errorMessage = String.format("字段'%s'不能为空，请提供该字段的值", fieldName);

            } else if (message.contains("Duplicate entry")) {
                // 重复数据
                String duplicateValue = extractDuplicateValue(message);
                errorMessage = String.format("数据重复: %s", duplicateValue);

            } else if (message.contains("foreign key constraint fails")) {
                errorMessage = "关联数据不存在，请检查相关数据";

            } else if (message.contains("Data too long for column")) {
                // 数据过长
                String fieldName = extractFieldName(message, "Data too long for column");
                errorMessage = String.format("字段'%s'数据过长，请缩短输入", fieldName);

            } else if (message.contains("cannot be null")) {
                // 不能为null
                String fieldName = extractFieldName(message, "cannot be null");
                errorMessage = String.format("字段'%s'不能为空", fieldName);
            }
        }

        return Result.error(ResultCode.DATABASE_ERROR.getCode(), errorMessage);
    }

    /**
     * 处理SQL完整性约束异常（主键重复、唯一约束等）
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<?> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        log.error("SQL完整性约束异常: {}", e.getMessage(), e);

        String message = e.getMessage();
        String errorMessage = "数据约束冲突";

        if (message.contains("Duplicate entry")) {
            String duplicateValue = extractDuplicateValue(message);
            errorMessage = String.format("数据已存在: %s", duplicateValue);
        } else if (message.contains("foreign key constraint fails")) {
            errorMessage = "关联数据不存在";
        } else if (message.contains("cannot be null")) {
            String fieldName = extractFieldName(message, "cannot be null");
            errorMessage = String.format("字段%s不能为空", fieldName);
        }

        return Result.error(ResultCode.DATABASE_ERROR.getCode(), errorMessage);
    }

    /**
     * 处理原生SQL异常
     */
    @ExceptionHandler(SQLException.class)
    public Result<?> handleSQLException(SQLException e) {
        log.error("SQL执行异常: {}, SQL State: {}, Error Code: {}",
                e.getMessage(), e.getSQLState(), e.getErrorCode(), e);

        String errorMessage = "数据库执行错误";
        int errorCode = e.getErrorCode();

        // 根据MySQL错误码返回具体信息
        switch (errorCode) {
            case 1062: // 重复键
                errorMessage = "数据已存在，请勿重复添加";
                break;
            case 1048: // 列不能为空
                errorMessage = "必填字段不能为空";
                break;
            case 1452: // 外键约束失败
                errorMessage = "关联数据不存在";
                break;
            case 1406: // 数据过长
                errorMessage = "输入数据过长，请缩短后重试";
                break;
            case 1366: // 不正确的列值
                errorMessage = "输入数据格式不正确";
                break;
        }

        return Result.error(ResultCode.DATABASE_ERROR.getCode(), errorMessage);
    }

    /**
     * 处理SQL语法异常
     */
    @ExceptionHandler(BadSqlGrammarException.class)
    public Result<?> handleBadSqlGrammarException(BadSqlGrammarException e) {
        log.error("SQL语法异常: {}", e.getMessage(), e);
        return Result.error(ResultCode.DATABASE_ERROR.getCode(), "SQL语法错误，请联系管理员");
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("参数验证异常: {}", e.getMessage(), e);

        // 收集所有字段错误
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return Result.error(
                ResultCode.PARAM_ERROR.getCode(),
                "参数验证失败",
                errors
        );
    }
    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("参数类型不匹配异常: {}", e.getMessage(), e);

        String errorMessage = String.format("参数'%s'类型错误，期望类型: %s",
                e.getName(),
                e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "未知"
        );

        return Result.error(ResultCode.PARAM_ERROR.getCode(), errorMessage);
    }

    // ==================== 工具方法 ====================

    /**
     * 从异常消息中提取字段名
     */
    private String extractFieldName(String message, String keyword) {
        try {
            // 示例消息: Field 'real_name' doesn't have a default value
            int start = message.indexOf("'") + 1;
            int end = message.indexOf("'", start);
            return message.substring(start, end);
        } catch (Exception e) {
            log.warn("提取字段名失败: {}", message);
            return "未知字段";
        }
    }

    /**
     * 从异常消息中提取重复值
     */
    private String extractDuplicateValue(String message) {
        try {
            // 示例消息: Duplicate entry 'zhangsan' for key 'users.uk_username'
            int start = message.indexOf("'") + 1;
            int end = message.indexOf("'", start);
            return message.substring(start, end);
        } catch (Exception e) {
            log.warn("提取重复值失败: {}", message);
            return "未知值";
        }
    }
}
