package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseCategoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    private Integer id;

    /**
     * 分类名称
     */
    @NonNull()
    @NotBlank(message = "分类名称不能为空")
    private String name;

    /**
     * 分类图标
     */
    private String icon;

    /**
     * 父分类ID
     */
    private Integer parentId;

    /**
     * 排序序号
     */
    private Integer sort;
}
