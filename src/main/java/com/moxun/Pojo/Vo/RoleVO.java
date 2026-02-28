package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 角色 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String roleName;
    private String roleDesc;
    private List<Long> menuIds;
}
