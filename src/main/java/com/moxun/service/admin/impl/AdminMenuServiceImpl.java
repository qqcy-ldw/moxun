package com.moxun.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moxun.Pojo.Dto.MenuSaveDTO;
import com.moxun.Pojo.Entity.SysMenu;
import com.moxun.Pojo.Vo.CourseCategoryVO;
import com.moxun.Pojo.Vo.MenuTreeVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.exception.BusinessException;
import com.moxun.mapper.admin.AdminMenuMapper;
import com.moxun.service.admin.AdminMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 菜单管理 Service 实现（占位）
 * 请在此类中实现具体业务逻辑，并注入 AdminMenuMapper
 */
@Slf4j
@Service
public class AdminMenuServiceImpl implements AdminMenuService {

    @Autowired
    private AdminMenuMapper adminMenuMapper;
    /**
     * 构建菜单树
     * @return
     */
    @Override
    public List<MenuTreeVO> listMenuTree() {
        //获取所有菜单
        List<MenuTreeVO> menuTree = adminMenuMapper.listMenuTree();
        if (Objects.isNull(menuTree)){
            throw new RuntimeException("菜单树构建失败");
        }
        //过滤出目录
        List<MenuTreeVO> rootList = menuTree.stream().
                filter(menu -> menu.getParentId() == 0)
                .collect(Collectors.toList());
        //递归构建子菜单
        rootList.forEach(menu -> buildChildren(menu, menuTree));
        return rootList;
    }

    /**
     * 分页查询菜单列表
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public PageResult listMenus(Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        Page<SysMenu> menuPage = adminMenuMapper.listMenus();
        return new PageResult(menuPage.getTotal(), menuPage.getResult());
    }

    /**
     * 根据ID查询菜单
     * @param id
     * @return
     */
    @Override
    public SysMenu getMenuById(Long id) {
        if (id == null){
            throw new RuntimeException("请输入正确的菜单id");
        }
        SysMenu menu = null;
        try {
            menu = adminMenuMapper.getMenuById(id);
            if (menu == null){
                throw new RuntimeException("菜单不存在");
            }
        } catch (Exception e) {
            throw new BusinessException("获取菜单失败");
        }

        return menu;
    }

    /**
     * 新增菜单
     * @param dto
     */
    @Override
    public void addMenu(MenuSaveDTO dto) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(dto, sysMenu);
        sysMenu.setStatus(1);
        sysMenu.setCreateTime(LocalDateTime.now());
        sysMenu.setUpdateTime(LocalDateTime.now());
        try {
            adminMenuMapper.insertMenu(sysMenu);
        } catch (Exception e) {
            log.error("新增菜单失败", e);
            throw new BusinessException("新增菜单失败");
        }
    }

    /**
     * 编辑菜单
     * @param dto
     */
    @Override
    public void updateMenu(MenuSaveDTO dto) {
        SysMenu sysMenu = new SysMenu();
        BeanUtils.copyProperties(dto, sysMenu);
        sysMenu.setUpdateTime(LocalDateTime.now());
        try {
            adminMenuMapper.updateMenu(sysMenu);
        } catch (Exception e) {
            log.error("编辑菜单失败", e);
            throw new BusinessException("编辑菜单失败");
        }
    }

    /**
     * 删除菜单
     * @param id
     */
    @Override
    public void deleteMenu(Long id) {
        if (id == null) {
            throw new RuntimeException("请输入正确的菜单id");
        }
        //判断当前菜单下是否有子菜单
        List<Integer> childrenIds = adminMenuMapper.getChildren(id);
        if(!childrenIds.isEmpty()){
            //删除子菜单
            adminMenuMapper.deleteChildren(childrenIds);
        }
        //删除目录
        adminMenuMapper.deleteMenu(id);
    }

    /**
     * 递归构建子菜单
      * @param menu 目录（顶级父类）
     * @param children  源数据
     */
    public void buildChildren(MenuTreeVO menu, List<MenuTreeVO> children) {
        List<MenuTreeVO> list = children.stream()
                .filter(childrens -> childrens.getParentId() != null && menu.getId().equals(childrens.getParentId()))
                .sorted(Comparator.comparingInt(MenuTreeVO::getSort))
                .collect(Collectors.toList());
        menu.setChildren(list);

        for (MenuTreeVO child : list) {
            buildChildren(child, children);
        }
    }

}
