package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 批改作业请求DTO
 */
@Data
public class AssignmentGradeDTO implements Serializable {
    private static final long serialVersionUID = 1L;


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
