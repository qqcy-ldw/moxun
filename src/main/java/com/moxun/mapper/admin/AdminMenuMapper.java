package com.moxun.mapper.admin;

import com.github.pagehelper.Page;
import com.moxun.Pojo.Entity.SysMenu;
import com.moxun.Pojo.Vo.MenuTreeVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMenuMapper {
    @Select("select id, id, parent_id, menu_name, menu_type, permission, icon, path, component, sort, status, create_time, update_time from sys_menu where menu_type = 'DIR' or menu_type = 'MENU'")
    List<MenuTreeVO> listMenuTree();

    @Select("select id, parent_id, menu_name, menu_type, permission, icon, path, component, sort, status, create_time, update_time from sys_menu")
    Page<SysMenu> listMenus();

    @Select("select id, parent_id, menu_name, menu_type, permission, icon, path, component, sort, status, create_time, update_time from sys_menu where id = #{id}")
    SysMenu getMenuById(Long id);

    void insertMenu(SysMenu sysMenu);

    void updateMenu(SysMenu sysMenu);

    @Select("select id from sys_menu where parent_id = #{id}")
    List<Integer> getChildren(Long id);

    @Delete("delete from sys_menu where id = #{id}")
    void deleteMenu(Long id);

    void deleteChildren(List<Integer> childrenIds);
}
