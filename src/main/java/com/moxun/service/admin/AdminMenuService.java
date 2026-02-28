package com.moxun.service.admin;

import com.moxun.Pojo.Dto.MenuSaveDTO;
import com.moxun.Pojo.Entity.SysMenu;
import com.moxun.Pojo.Vo.MenuTreeVO;

import java.util.List;

/**
 * 菜单管理 Service 接口
 * 请自行实现 AdminMenuServiceImpl
 */
public interface AdminMenuService {

    /**
     * 获取菜单树（用于前端展示）
     */
    List<MenuTreeVO> listMenuTree();

    /**
     * 获取菜单列表（扁平，可分页）
     */
    List<SysMenu> listMenus(Integer page, Integer pageSize);

    /**
     * 根据ID获取菜单详情
     */
    SysMenu getMenuById(Long id);

    /**
     * 新增菜单
     */
    void addMenu(MenuSaveDTO dto);

    /**
     * 编辑菜单
     */
    void updateMenu(MenuSaveDTO dto);

    /**
     * 删除菜单
     */
    void deleteMenu(Long id);
}
