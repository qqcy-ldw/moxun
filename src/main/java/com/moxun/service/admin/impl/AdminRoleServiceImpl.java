package com.moxun.service.admin.impl;

import com.moxun.Pojo.Dto.RoleSaveDTO;
import com.moxun.Pojo.Vo.RoleVO;
import com.moxun.service.admin.AdminRoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色管理 Service 实现（占位）
 * 请在此类中实现具体业务逻辑，并注入 AdminRoleMapper
 */
@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    @Override
    public List<RoleVO> listRoles(Integer page, Integer pageSize) {
        // TODO: 实现分页查询，注入 AdminRoleMapper
        return new ArrayList<>();
    }

    @Override
    public RoleVO getRoleById(Long id) {
        // TODO: 实现根据ID查询角色及关联菜单
        return null;
    }

    @Override
    public void addRole(RoleSaveDTO dto) {
        // TODO: 实现新增角色及角色-菜单关联
    }

    @Override
    public void updateRole(RoleSaveDTO dto) {
        // TODO: 实现编辑角色及角色-菜单关联
    }

    @Override
    public void deleteRole(Long id) {
        // TODO: 实现删除角色（需先删除 sys_user_role、sys_role_menu 关联）
    }
}
