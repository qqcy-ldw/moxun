package com.moxun.Pojo.Vo;

import com.moxun.Pojo.Entity.CourseCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

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




  、   */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 子分类列表（用于树形结构）
     */
    private List<CourseCategoryVO> children;

    /**
     * 父分类名称
     */
    private String parentName;

    /**
     * 该分类下课程数量
     */
    private Integer courseCount;

    /**
     * 完整路径：编程开发 > Web前端
     */
    private String fullPath;
}
