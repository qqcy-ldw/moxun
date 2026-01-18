package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmission {
    /**
     * 提交ID
     */
    private Long id;

    /**
     * 作业ID
     */
    private Long assignmentId;

    /**
     * 学生ID
     */
    private Long userId;

    /**
     * 提交内容
     */
    private String content;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 反馈
     */
    private String feedback;

    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 评分时间
     */
    private LocalDateTime gradedTime;
}
