package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 批改作业请求DTO
 */
public class AssignmentGradeDTO {

    /**
     * 提交ID
     */
    @NotNull(message = "提交ID不能为空")
    private Long submissionId;

    /**
     * 得分
     */
    @NotNull(message = "得分不能为空")
    @Min(value = 0, message = "得分不能小于0")
    @Max(value = 100, message = "得分不能大于100")
    private Integer score;

    /**
     * 反馈
     */
    private String feedback;
}
