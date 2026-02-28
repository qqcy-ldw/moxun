package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 菜单权限实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long parentId;
    private String menuName;
    private String menuType;  // DIR, MENU, BUTTON
    private String permission;
    private String icon;
    private String path;
    private String component;
    private Integer sort;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
