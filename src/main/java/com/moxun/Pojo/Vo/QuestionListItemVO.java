package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 问题列表项VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionListItemVO {

    /**
     * 问题ID
     */
    private Long id;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 提问用户ID
     */
    private Long userId;

    /**
     * 关联课程ID
     */
    private Long courseId;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 回答数量
     */
    private Integer answerCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
