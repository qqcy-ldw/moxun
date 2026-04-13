package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 课程评论 VO
 * 👑 面试考点：用于前端展示评论列表，包含用户头像和用户名
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评分（1-5）
     */
    private Integer rating;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String avatar;
}
