package com.moxun.annotation;

import com.moxun.Enum.ActionType;

import java.lang.annotation.*;

/**
 * 用户操作日志注解
 * 标注在Controller方法上，自动记录用户操作日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserAction {

    /**
     * 操作类型
     * 使用 ActionType 枚举定义
     */
    ActionType actionType();

    /**
     * 操作描述
     * 例如："创建课程"、"删除用户"
     */
    String description() default "";

    /**
     * 资源类型
     * 例如：course、user、assignment
     */
    String resourceType() default "";

    /**
     * 是否记录请求参数
     * 默认 true，建议设为 false 避免记录敏感信息（如密码）
     */
    boolean logParams() default true;

    /**
     * 是否记录错误响应结果
     * 默认 false，成功的响应结果无需记录
     * 设为 true 时，仅当接口返回错误（code != 200）才会记录响应内容
     */
    boolean logResult() default false;
}
