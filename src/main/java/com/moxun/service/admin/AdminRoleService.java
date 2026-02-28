package com.moxun.service.admin;

import com.moxun.Pojo.Dto.RoleSaveDTO;
import com.moxun.Pojo.Vo.RoleVO;

import java.util.List;

/**
 * 角色管理 Service 接口
 * 请自行实现 AdminRoleServiceImpl
 */
public interface AdminRoleService {

    /**
     * 分页查询角色列表
     */
    List<RoleVO> listRoles(Integer page, Integer pageSize);

    /**
     * 根据ID获取角色详情（含关联菜单）
     */
    RoleVO getRoleById(Long id);

    /**
     * 新增角色
     */
    void addRole(RoleSaveDTO dto);

    /**
     * 编辑角色
     */
    void updateRole(RoleSaveDTO dto);

    /**
     * 删除角色
     */
    void deleteRole(Long id);
}
