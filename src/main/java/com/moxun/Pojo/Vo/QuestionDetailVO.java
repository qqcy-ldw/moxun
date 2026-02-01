package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 问题详情VO（包含回答列表）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDetailVO implements Serializable {
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
     * 问题内容
     */
    private String content;

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

    /**
     * 最后回答时间
     */
    private LocalDateTime lastAnswerTime;

    /**
     * 回答列表
     */
    private List<AnswerVO> answers;
}
