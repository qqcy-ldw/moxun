package com.moxun.controller.admin;

import com.moxun.Pojo.Dto.LoginDTO;
import com.moxun.Pojo.Dto.UserUpdateDTO;
import com.moxun.Pojo.Entity.User;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.UserListVO;
import com.moxun.Pojo.Vo.UserProfileVO;
import com.moxun.service.admin.AdminUserService;
import com.moxun.service.auth.AuthService;
import com.moxun.util.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员用户管理接口
 * 
 * 【学习目的】
 * 演示 Spring Security 的方法级权限控制
 * 
 * 【权限设计】
 * - 查询用户：需要 ADMIN 角色 或 system:user:view 权限
 * - 新增用户：需要 ADMIN 角色 或 system:user:add 权限
 * - 编辑用户：需要 ADMIN 角色 或 system:user:edit 权限
 * - 删除用户：需要 ADMIN 角色 且 system:user:delete 权限
 */
@Slf4j
@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    @Autowired
    private AdminUserService userService;

    @Autowired
    private AuthService authService;
    
    /**
     * 查询用户列表
     * 
     * 权限：ADMIN 角色 或 system:user:view 权限
     * 测试方法：
     * 1. 使用 郑静（ROLE_ADMIN）登录，应该能访问
     * 2. 使用 张明（ROLE_STUDENT）登录，应该返回 403
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:user:view')")
    @GetMapping
    public Result<PageResult> listUsers(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "1") Integer status
    ) {

        PageResult users = userService.listUsers(username, page, pageSize, status);
        log.info("users:{}",users);
        return Result.success(users);
    }
    
    /**
     * 新增用户
     * 
     * 权限：ADMIN 角色 或 system:user:add 权限
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:user:add')")
    @PostMapping
    public Result<String> addUser(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("【权限控制测试】Authentication:{}", authentication.toString());
        String currentUser = authentication.getName();
        
        log.info("【权限控制测试】用户 {} 正在新增用户 - 用户名: {}", currentUser, loginDTO.getUsername());

        authService.CommonRegister(loginDTO);
        
        return Result.success("新增成功！操作人：" + currentUser);
    }
    
    /**
     * 编辑用户
     * 
     * 权限：ADMIN 角色 或 system:user:edit 权限
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:user:edit')")
    @PutMapping("/{id}")
    public Result<String> updateUser(@RequestBody UserUpdateDTO userUpdateDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        
        log.info("【权限控制测试】用户 {} 正在编辑用户 - 用户姓名: {}", currentUser, userUpdateDTO.getUsername());

        authService.modifyUpdateUser(userUpdateDTO);
        
        return Result.success("编辑成功！操作人：" + currentUser);
    }
    
    /**
     * 删除用户/批量删除用户
     * 
     * 权限：ADMIN 角色 且 system:user:delete 权限（双重验证）
     * 
     * 注意：这里使用 and 连接，表示必须同时满足两个条件
     */
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('system:user:delete')")
    @DeleteMapping()
    public Result<String> deleteUser(@RequestParam List<Integer> id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();

        userService.deleteUser(id);
        
        return Result.success("删除成功！操作人：" + currentUser);
    }
    
    /**
     * 获取当前登录用户的权限信息
     * 
     * 这个接口不需要特殊权限，只要登录即可访问
     * 用于测试和调试
     */
    @GetMapping("/my-permissions")
    public Result<PermissionInfo> getMyPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        PermissionInfo info = new PermissionInfo(
            authentication.getName(),
            authentication.getAuthorities(),
            authentication.getPrincipal()
        );
        
        return Result.success(info);
    }
    
    /**
     * 权限信息VO
     */
    @Data
    @AllArgsConstructor
    static class PermissionInfo {
        private String username;
        private Object authorities;
        private Object principal;
    }
}

