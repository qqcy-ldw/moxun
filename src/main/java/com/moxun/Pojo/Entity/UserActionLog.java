package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActionLog implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 日志ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 登录状态：1-成功，0-失败
     */
    private Integer loginStatus;

    /**
     * 失败原因（如"密码错误"、"账号锁定"）
     */
    private String failReason;

    /**
     * 操作类型
     */
    private String actionType;

    /**
     * 操作描述
     */
    private String actionDesc;

    /**
     * 资源类型
     */
    private String resourceType;

    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 用户代理
     */
    private String userAgent;

    /**
     * 请求URL
     */
    private String requestUrl;

    /**
     * 请求方法
     */
    private String requestMethod;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 响应状态码
     */
    private Integer responseCode;

    /**
     * 错误响应结果（仅记录接口返回错误时的响应内容）
     */
    private String responseResult;

    /**
     * 操作耗时(毫秒)
     */
    private Integer duration;

    /**
     * 请求ID（用于日志链路追踪）
     */
    private String requestId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
