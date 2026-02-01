package com.moxun.mapper.auth;


import com.moxun.Pojo.Entity.User;
import com.moxun.Pojo.Vo.UserProfileVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 认证相关的 Mapper 接口
 * 
 * 【学习笔记】
 * 为什么不用 @Select 等注解？
 * - 简单SQL可以用注解（@Select、@Insert等）
 * - 复杂SQL建议用XML（更易维护、支持动态SQL）
 * - 本项目统一使用XML方式，SQL都在 AuthMapper.xml 中
 */
@Mapper
public interface AuthMapper {

    /**
     * 根据用户名查询用户信息（用于登录）
     * @param username 用户名
     * @return 用户信息
     */
    User CommonLogin(String username);

    /**
     * 用户注册
     * @param user 用户信息
     */
    void CommonRegister(User user);

    /**
     * 更新用户状态（用于锁定/解锁账号）
     * @param loginUser 用户信息（包含status和passwordExpireTime）
     */
    void setStatus(User loginUser);

    /**
     * 更新最后登录信息
     * @param loginUser 用户信息（包含updateTime和lastLoginIp）
     */
    void modifyUpdateTime(User loginUser);

    /**
     * 获取用户详细信息
     * @param id 用户ID
     * @return 用户详情VO
     */
    UserProfileVO getUserInfo(Integer id);

    /**
     * 修改用户信息（动态更新）
     * @param user 用户信息
     */
    void modifyUpdateUser(User user);

    /**
     * 上传用户头像
     * @param upload 头像URL
     * @param username 用户名
     */
    void uploadUserAvatar(@Param("upload") String upload, @Param("username") String username);

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息
     */
    User getByUserName(String username);

    /**
     * 查询用户的所有权限标识
     * 
     * 【查询路径】
     * users → sys_user_role → sys_role → sys_role_menu → sys_menu
     * 
     * 【返回格式】
     * ["system:user:view", "system:user:add", "system:user:edit"]
     * 
     * 【SQL位置】
     * 见 AuthMapper.xml 中的 findUserPermissions
     * 
     * @param userId 用户ID
     * @return 权限标识列表
     */
    List<String> findUserPermissions(Long userId);

    /**
     * 查询用户的所有角色
     * 
     * 【返回格式】
     * ["ROLE_ADMIN", "ROLE_TEACHER"]
     * 
     * 【注意】
     * 角色名必须以 ROLE_ 开头（Spring Security 约定）
     * 
     * 【SQL位置】
     * 见 AuthMapper.xml 中的 findUserRoles
     * 
     * @param userId 用户ID
     * @return 角色列表
     */
    List<String> findUserRoles(Long userId);

    /**
     * 设置用户角色
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    @Insert("insert into sys_user_role(user_id, role_id) values(#{userId}, #{roleId})")
    void setRole(Long userId, Integer roleId);
}
