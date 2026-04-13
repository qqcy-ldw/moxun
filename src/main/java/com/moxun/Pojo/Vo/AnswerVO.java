package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 回答 VO
 * 👑 面试考点：用于问答详情展示，包含用户信息和点赞状态
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerVO implements Serializable {
    private static final long serialVersionUID = 1L;

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
     * 回答用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 回答内容
     */
    private String content;

    /**
     * 是否被采纳（1-是，0-否）
     */
    private Integer isAccepted;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 当前用户是否已点赞（0-否，1-是）
     */
    private Integer isLiked;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
