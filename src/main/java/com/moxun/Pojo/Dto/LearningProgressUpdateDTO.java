package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 学习进度更新 DTO
 */
@Data
public class LearningProgressUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "课时ID不能为空")
    private Long sectionId;

    /**
     * 视频播放位置(秒)
     */
    private Integer position;

    /**
     * 是否完成
     */
    private Boolean finished;
}
