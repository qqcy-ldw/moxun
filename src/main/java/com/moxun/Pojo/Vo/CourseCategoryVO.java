package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 课程分类VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseCategoryVO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 分类ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 父分类ID
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sort;
}
