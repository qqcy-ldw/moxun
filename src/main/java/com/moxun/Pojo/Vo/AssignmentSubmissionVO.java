package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 作业提交VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentSubmissionVO {

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

    /**
     * 提交附件列表
     */
    private List<SubmissionAttachmentVO> attachments;
}
