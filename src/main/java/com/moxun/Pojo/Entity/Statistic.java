package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Statistic {
    /**
     * 统计ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 统计日期
     */
    private LocalDate statDate;

    /**
     * 统计类型(learning_time/income/login)
     */
    private String statType;

    /**
     * 统计值
     */
    private BigDecimal value;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
