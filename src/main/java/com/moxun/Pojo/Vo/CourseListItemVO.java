package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程列表项VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseListItemVO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 课程ID
     */
    private Long id;

    /**
     * 课程标题
     */
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
     * 难度级别
     */
    private String level;

    /**
     * 学生数量
     */
    private Integer studentCount;

    /**
     * 评分（平均分）
     */
    private Double rating;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
