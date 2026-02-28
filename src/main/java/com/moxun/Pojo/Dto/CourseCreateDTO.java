package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 创建课程请求DTO
 */
@Data
public class CourseCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 课程标题
     */
    @NotBlank(message = "课程标题不能为空")
    @Size(max = 100, message = "课程标题长度不能超过100个字符")
    private String title;

    /**
     * 课程描述
     */
    private String description;

    /**
     * 封面图片URL
     */
    private String coverImage;

    /**
     * 价格
     */
    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    /**
     * 分类ID
     */
    @NotNull(message = "课程分类不能为空")
    private Long categoryId;

    /**
     * 难度级别(初级/中级/高级)
     */
    private String level;
}
