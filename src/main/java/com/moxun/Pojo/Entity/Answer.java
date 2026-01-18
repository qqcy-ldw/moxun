package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
    /**
     * 回答ID
     */
    private Long id;

    /**
     * 问题ID
     */
    private Long questionId;

    /**
     * 回答用户ID
     */
    private Long userId;

    /**
     * 回答内容
     */
    private String content;

    /**
     * 是否被采纳
     */
    private Integer isAccepted;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
