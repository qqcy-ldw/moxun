package com.moxun.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moxun.Pojo.Dto.RoleSaveDTO;
import com.moxun.Pojo.Entity.SysRole;
import com.moxun.Pojo.Vo.RoleVO;
import com.moxun.exception.BusinessException;
import com.moxun.mapper.admin.AdminRoleMapper;
import com.moxun.service.admin.AdminRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Override
    public List<RoleVO> listRoles(Integer page, Integer pageSize) {
        page = (page == null || page < 1) ? 1 : page;
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        PageHelper.startPage(page, pageSize);
        Page<RoleVO> rolePage = (Page<RoleVO>) adminRoleMapper.listRoles();
        return rolePage.getResult();
    }

    @Override
    public RoleVO getRoleById(Long id) {
        if (id == null) {
            throw new BusinessException("角色ID不能为空");
        }
        SysRole sysRole = adminRoleMapper.getById(id);
        if (sysRole == null) {
            throw new BusinessException("角色不存在");
        }
        List<Long> menuIds = adminRoleMapper.getMenuIdsByRoleId(id);
        RoleVO roleVO = RoleVO.builder()
                .id(sysRole.getId())
                .roleName(sysRole.getRoleName())
                .roleDesc(sysRole.getRoleDesc())
                .menuIds(menuIds != null ? menuIds : Collections.emptyList())
                .build();
        return roleVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRole(RoleSaveDTO dto) {
        if (dto == null) {
            throw new BusinessException("参数不能为空");
        }
        if (dto.getRoleName() == null || dto.getRoleName().trim().isEmpty()) {
            throw new BusinessException("角色名称不能为空");
        }
        String existsRoleName = adminRoleMapper.existsByRoleName(dto.getRoleName().trim(), null);
        if (existsRoleName != null) {
            throw new BusinessException("角色名称已存在");
        }
        SysRole sysRole = SysRole.builder()
                .roleName(dto.getRoleName().trim())
                .roleDesc(dto.getRoleDesc())
                .build();
        adminRoleMapper.insert(sysRole);
        if (dto.getMenuIds() != null && !dto.getMenuIds().isEmpty()) {
            adminRoleMapper.deleteRoleMenus(sysRole.getId());
            adminRoleMapper.insertRoleMenus(sysRole.getId(), dto.getMenuIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(RoleSaveDTO dto) {
        if (dto == null || dto.getId() == null) {
            throw new BusinessException("参数错误");
        }
        SysRole existing = adminRoleMapper.getById(dto.getId());
        if (existing == null) {
            throw new BusinessException("角色不存在");
        }
        String existsRoleName = adminRoleMapper.existsByRoleName(dto.getRoleName().trim(), dto.getId());
        if (existsRoleName != null) {
            throw new BusinessException("角色名称已存在");
        }
        SysRole sysRole = SysRole.builder()
                .id(dto.getId())
                .roleName(dto.getRoleName().trim())
                .roleDesc(dto.getRoleDesc())
                .build();
        adminRoleMapper.update(sysRole);
        adminRoleMapper.deleteRoleMenus(dto.getId());
        if (dto.getMenuIds() != null && !dto.getMenuIds().isEmpty()) {
            adminRoleMapper.insertRoleMenus(dto.getId(), dto.getMenuIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        if (id == null) {
            throw new BusinessException("角色ID不能为空");
        }
        SysRole existing = adminRoleMapper.getById(id);
        if (existing == null) {
            throw new BusinessException("角色不存在");
        }
        int userCount = adminRoleMapper.countUsersByRoleId(id);
        if (userCount > 0) {
            throw new BusinessException("该角色下仍有用户关联，无法删除，请先移除用户或变更其角色");
        }
        adminRoleMapper.deleteRoleMenus(id);
        adminRoleMapper.deleteById(id);
    }
}
