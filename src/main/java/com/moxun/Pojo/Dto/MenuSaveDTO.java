package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/**
 * 菜单保存 DTO（新增/编辑共用）
 */
@Data
public class MenuSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    private Long id;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 菜单名称
     */
    @NotBlank(message = "菜单名称不能为空")
    private String menuName;

    /**
     * 菜单类型
     */
    @NotBlank(message = "菜单类型不能为空")
    private String menuType;   // DIR, MENU, BUTTON

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 菜单图标
     */
    private String icon;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     * 显示排序
     */
    private Integer sort;

    /**
     * 菜单状态（0显示 1隐藏）
     */
    private Integer status;

}
