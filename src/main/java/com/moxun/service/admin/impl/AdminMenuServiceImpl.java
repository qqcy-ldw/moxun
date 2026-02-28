package com.moxun.service.admin.impl;

import com.moxun.Pojo.Dto.MenuSaveDTO;
import com.moxun.Pojo.Entity.SysMenu;
import com.moxun.Pojo.Vo.MenuTreeVO;
import com.moxun.service.admin.AdminMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单管理 Service 实现（占位）
 * 请在此类中实现具体业务逻辑，并注入 AdminMenuMapper
 */
@Service
public class AdminMenuServiceImpl implements AdminMenuService {

    @Override
    public List<MenuTreeVO> listMenuTree() {
        // TODO: 实现菜单树构建（parentId=0 为根，递归组装 children）
        return new ArrayList<>();
    }

    @Override
    public List<SysMenu> listMenus(Integer page, Integer pageSize) {
        // TODO: 实现分页查询
        return new ArrayList<>();
    }

    @Override
    public SysMenu getMenuById(Long id) {
        // TODO: 实现根据ID查询
        return null;
    }

    @Override
    public void addMenu(MenuSaveDTO dto) {
        // TODO: 实现新增菜单
    }

    @Override
    public void updateMenu(MenuSaveDTO dto) {
        // TODO: 实现编辑菜单
    }

    @Override
    public void deleteMenu(Long id) {
        // TODO: 实现删除菜单（需考虑子菜单）
    }
}
