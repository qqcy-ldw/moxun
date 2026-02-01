package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 课程详情VO（包含章节和课时）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetailVO implements Serializable {
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
     * 分类ID
     */
    private Long categoryId;

    /**
     * 难度级别
     */
    private String level;

    /**
     * 课程总时长(分钟)
     */
    private Integer duration;

    /**
     * 学生数量
     */
    private Integer studentCount;

    /**
     * 状态(draft/published/offline)
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 章节列表（包含课时）
     */
    private List<ChapterVO> chapters;
}
