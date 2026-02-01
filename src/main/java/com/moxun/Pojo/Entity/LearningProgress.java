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
public class LearningProgress implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 进度ID
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
     * 课时ID
     */
    private Long sectionId;

    /**
     * 视频播放位置(秒)
     */
    private Integer position;

    /**
     * 是否完成
     */
    private Integer finished;

    /**
     * 最后学习时间
     */
    private LocalDateTime lastLearnTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
