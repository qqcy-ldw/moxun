package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 更新课程请求DTO
 */
public class CourseUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 课程ID
     */
    private Long id;

    /**
     * 课程标题
     */
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
    private BigDecimal price;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 难度级别(初级/中级/高级)
     */
    private String level;

    /**
     * 状态(draft/published/offline)
     */
    private String status;
}
