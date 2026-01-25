package com.moxun.controller.admin;

import com.moxun.Pojo.Entity.User;
import com.moxun.util.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    
    /**
     * 查询用户列表
     * 
     * 权限：ADMIN 角色 或 system:user:view 权限
     * 
     * 测试方法：
     * 1. 使用 郑静（ROLE_ADMIN）登录，应该能访问
     * 2. 使用 张明（ROLE_STUDENT）登录，应该返回 403
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:user:view')")
    @GetMapping
    public Result<String> listUsers(
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        
        log.info("【权限控制测试】用户 {} 正在查询用户列表 - 关键字: {}, 页码: {}", 
                 currentUser, username, page);
        
        // 打印用户权限
        log.info("用户权限: {}", authentication.getAuthorities());
        
        // TODO: 实现查询逻辑
        // List<User> users = userService.listUsers(username, page, pageSize);
        
        return Result.success("查询成功！当前用户：" + currentUser + "，拥有权限：" + authentication.getAuthorities());
    }
    
    /**
     * 新增用户
     * 
     * 权限：ADMIN 角色 或 system:user:add 权限
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:user:add')")
    @PostMapping
    public Result<String> addUser(@RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        
        log.info("【权限控制测试】用户 {} 正在新增用户 - 用户名: {}", currentUser, user.getUsername());
        
        // TODO: 实现新增逻辑
        // userService.addUser(user);
        
        return Result.success("新增成功！操作人：" + currentUser);
    }
    
    /**
     * 编辑用户
     * 
     * 权限：ADMIN 角色 或 system:user:edit 权限
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:user:edit')")
    @PutMapping("/{id}")
    public Result<String> updateUser(@PathVariable Long id, @RequestBody User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        
        log.info("【权限控制测试】用户 {} 正在编辑用户 - 用户ID: {}", currentUser, id);
        
        // TODO: 实现编辑逻辑
        // userService.updateUser(id, user);
        
        return Result.success("编辑成功！操作人：" + currentUser);
    }
    
    /**
     * 删除用户
     * 
     * 权限：ADMIN 角色 且 system:user:delete 权限（双重验证）
     * 
     * 注意：这里使用 and 连接，表示必须同时满足两个条件
     */
    @PreAuthorize("hasRole('ADMIN') and hasAuthority('system:user:delete')")
    @DeleteMapping("/{id}")
    public Result<String> deleteUser(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();
        
        log.info("【权限控制测试】用户 {} 正在删除用户 - 用户ID: {}", currentUser, id);
        
        // TODO: 实现删除逻辑
        // userService.deleteUser(id);
        
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

