package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 创建作业请求DTO
 */
public class AssignmentCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 作业标题
     */
    @NotBlank(message = "作业标题不能为空")
    private String title;

    /**
     * 作业描述
     */
    private String description;

    /**
     * 关联课程ID
     */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /**
     * 截止日期
     */
    private LocalDateTime deadline;

    /**
     * 总分
     */
    private Integer totalScore;

    /**
     * 作业内容
     */
    private String content;
}
