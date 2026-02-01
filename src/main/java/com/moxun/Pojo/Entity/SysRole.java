package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRole implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 角色ID
     */
    private Long id;

    /**
     * 角色名称（如"ROLE_USER"、"ROLE_ADMIN"）
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;
}
