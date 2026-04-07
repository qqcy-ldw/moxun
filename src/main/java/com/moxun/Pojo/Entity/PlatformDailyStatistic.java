package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformDailyStatistic implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 统计日期
     */
    private LocalDate statDate;

    /**
     * 平台总用户数
     */
    private Integer totalUsers;

    /**
     * 新增用户数
     */
    private Integer newUsers;

    /**
     * 平台活跃用户数
     */
    private Integer activeUsers;

    /**
     * 平台总课程数
     */
    private Integer courseViews;

    /**
     * 平台总课时数
     */
    private Integer lessonViews;

    /**
     * 平台总学习时长
     */
    private Integer totalStudyTime;

    /**
     * 平台总作业提交数
     */
    private Integer assignmentSubmissions;

    /**
     * 平台总问题提交数
     */
    private Integer questionPosts;

    /**
     * 平台总讨论帖数
     */
    private Integer answerPosts;

    /**
     * 平台总评论数
     */
    private Integer commentPosts;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
