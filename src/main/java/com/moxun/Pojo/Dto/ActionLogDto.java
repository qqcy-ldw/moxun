package com.moxun.Pojo.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 用户操作日志 - 查询DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionLogDto {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 操作类型
     */
    private String actionType;

    /**
     * 资源类型
     */
    private String resourceType;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * IP地址
     */
    private String ipAddress;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 10;
}
