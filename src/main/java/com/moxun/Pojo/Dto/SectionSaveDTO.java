package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 课时保存 DTO（新增/编辑共用）
 */
@Data
public class SectionSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotNull(message = "章节ID不能为空")
    private Long chapterId;
    @NotBlank(message = "课时标题不能为空")
    private String title;
    private String videoUrl;
    private Integer duration;
    private Integer isFree;  // 0-否 1-是
    private Integer sort;
}
