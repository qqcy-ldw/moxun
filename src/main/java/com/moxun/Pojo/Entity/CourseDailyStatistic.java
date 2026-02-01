package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDailyStatistic implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * ID
     */
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 统计日期
     */
    private LocalDate statDate;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 学习人数
     */
    private Integer studyCount;

    /**
     * 学习时长(分钟)
     */
    private Integer studyTime;

    /**
     * 新增学员数
     */
    private Integer newStudentCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
