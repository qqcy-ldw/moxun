package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 作业VO（列表/基础信息）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentVO {

    /**
     * 作业ID
     */
    private Long id;

    /**
     * 作业标题
     */
    private String title;

    /**
     * 作业描述
     */
    private String description;

    /**
     * 关联课程ID
     */
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
     * 创建时间
     */
    private LocalDateTime createTime;
}
