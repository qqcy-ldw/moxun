package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色保存 DTO（新增/编辑共用）
 */
@Data
public class RoleSaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID（编辑时必填，新增时为空）
     */
    private Long id;

    /**
     * 角色名称（如 ROLE_ADMIN）
     */
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 关联的菜单ID列表
     */
    private List<Long> menuIds;
}
