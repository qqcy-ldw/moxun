package com.moxun.Pojo.Dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 菜单保存 DTO（新增/编辑共用）
 */
@Data
public class MenuSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long parentId;
    private String menuName;
    private String menuType;   // DIR, MENU, BUTTON
    private String permission;
    private String icon;
    private String path;
    private String component;
    private Integer sort;
    private Integer status;
}
