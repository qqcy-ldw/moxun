package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {
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
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 最后回答时间
     */
    private LocalDateTime lastAnswerTime;
}
