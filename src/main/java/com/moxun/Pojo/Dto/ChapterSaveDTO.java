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

    private Long id;
    @NotNull(message = "课程ID不能为空")
    private Long courseId;
    @NotBlank(message = "章节标题不能为空")
    private String title;
    private Integer sort;
}
