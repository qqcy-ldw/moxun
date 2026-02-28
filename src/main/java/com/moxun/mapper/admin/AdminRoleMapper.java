package com.moxun.mapper.admin;

import com.moxun.Pojo.Entity.SysRole;
import com.moxun.Pojo.Vo.RoleVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminRoleMapper {

    @Select("SELECT id, role_name, role_desc FROM sys_role ORDER BY id")
    List<RoleVO> listRoles();

    @Select("SELECT id, role_name, role_desc FROM sys_role WHERE id = #{id}")
    SysRole getById(Long id);

    @Select("SELECT role_name FROM sys_role WHERE role_name = #{roleName} AND (#{id} IS NULL OR id != #{id})")
    String existsByRoleName(@Param("roleName") String roleName, @Param("id") Long id);

    @Insert("INSERT INTO sys_role (role_name, role_desc) VALUES (#{roleName}, #{roleDesc})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(SysRole role);

    @Update("UPDATE sys_role SET role_name = #{roleName}, role_desc = #{roleDesc} WHERE id = #{id}")
    void update(SysRole role);

    @Delete("DELETE FROM sys_role WHERE id = #{id}")
    void deleteById(Long id);

    @Select("SELECT menu_id FROM sys_role_menu WHERE role_id = #{roleId}")
    List<Long> getMenuIdsByRoleId(Long roleId);

    @Delete("DELETE FROM sys_role_menu WHERE role_id = #{roleId}")
    void deleteRoleMenus(Long roleId);

    void insertRoleMenus(@Param("roleId") Long roleId, @Param("menuIds") List<Long> menuIds);

    @Select("SELECT COUNT(*) FROM sys_user_role WHERE role_id = #{roleId}")
    int countUsersByRoleId(Long roleId);
}
