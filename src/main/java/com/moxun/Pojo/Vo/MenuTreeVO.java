package com.moxun.Pojo.Vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 菜单树形结构 VO
 */
@Data
public class MenuTreeVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long parentId;
    private String menuName;
    private String menuType;
    private String permission;
    private String icon;
    private String path;
    private String component;
    private Integer sort;
    private Integer status;

    /**
     * 子菜单
     */
    private List<MenuTreeVO> children;
}
