package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 章节保存 DTO（新增/编辑共用）
 */
@Data
public class ChapterSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 章节ID
     */
    private Long id;

    /**
     * 课程ID
     */
    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    /**
     * 章节标题
     */
    @NotBlank(message = "章节标题不能为空")
    private String title;

    /**
     * 排序
     */
    private Integer sort;
}
