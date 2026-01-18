package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 创建问题请求DTO
 */
public class QuestionCreateDTO {

    /**
     * 问题标题
     */
    @NotBlank(message = "问题标题不能为空")
    private String title;

    /**
     * 问题内容
     */
    @NotBlank(message = "问题内容不能为空")
    private String content;

    /**
     * 关联课程ID
     */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;
}
