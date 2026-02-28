package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程评论 DTO
 */
@Data
public class CourseCommentCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @NotBlank(message = "评论内容不能为空")
    private String content;

    @Min(value = 1, message = "评分1-5")
    @Max(value = 5, message = "评分1-5")
    private Integer rating = 5;
}
