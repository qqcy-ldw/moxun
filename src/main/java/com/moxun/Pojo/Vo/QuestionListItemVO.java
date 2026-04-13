package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 问题列表项 VO
 * 👑 面试考点：用于问答列表展示，包含回答数和浏览数
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionListItemVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 问题ID
     */
    private Long id;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 问题内容摘要
     */
    private String content;

    /**
     * 提问用户ID
     */
    private Long userId;

    /**
     * 提问用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 关联课程ID
     */
    private Long courseId;

    /**
     * 课程名称
     */
    private String courseName;

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

    /**
     * 最后回答时间
     */
    private LocalDateTime lastAnswerTime;
}
