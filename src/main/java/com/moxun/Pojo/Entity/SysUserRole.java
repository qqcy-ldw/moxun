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
public class SysUserRole implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户ID（关联users.id）
     */
    private Long userId;

    /**
     * 角色ID（关联sys_role.id）
     */
    private Long roleId;
}
