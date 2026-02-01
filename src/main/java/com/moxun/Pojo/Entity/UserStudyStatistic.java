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
public class UserStudyStatistic implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 统计日期
     */
    private LocalDate statDate;

    /**
     * 总学习时长(分钟)
     */
    private Integer totalStudyTime;

    /**
     * 学习课程数
     */
    private Integer courseCount;

    /**
     * 学习课时数
     */
    private Integer lessonCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
