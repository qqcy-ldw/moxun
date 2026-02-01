package com.moxun.security.service;


import com.moxun.Pojo.Entity.User;
import com.moxun.mapper.auth.AuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class SecurityService implements UserDetailsService {

    @Autowired
    private AuthMapper authMapper;

    ConcurrentHashMap<Object, String> hashMap = new ConcurrentHashMap<>();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 查询用户
        User user = authMapper.CommonLogin(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }


        // 2. 检查账号状态
        checkAccountStatus(user);

        // 3. 获取用户权限（从您的 sys_role_menu 等表查询）
        List<GrantedAuthority> authorities = getAuthorities(user.getId());

        // 4. 返回 UserDetails 对象（Spring Security 的标准用户对象）
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())  // 数据库中的密码（MD5加密后的）
                .authorities(authorities)
                .accountExpired(false)      // 账号是否过期
                .accountLocked(user.getStatus() == 2)  // 账号是否锁定
                .credentialsExpired(false)  // 凭证是否过期
                .disabled(user.getStatus() == 0)  // 是否禁用
                .build();
    }

    private void checkAccountStatus(User user) {
        if (user.getStatus() == 0) {
            throw new DisabledException("用户被禁用");
        }

        if (user.getStatus() == 2) {
            // 检查锁定是否过期
            if (user.getPasswordExpireTime() != null &&
                    user.getPasswordExpireTime().isAfter(LocalDateTime.now())) {
                long remaining = LocalDateTime.now().until(
                        user.getPasswordExpireTime(), ChronoUnit.MINUTES
                );
                throw new LockedException("账号已锁定，请" + remaining + "分钟后重试");
            } else {
                // 锁定已过期，重置状态
                user.setStatus(1);
                authMapper.setStatus(user);
            }
        }
    }

    private List<GrantedAuthority> getAuthorities(Long userId) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 1. 查询角色（格式：ROLE_角色名）
        List<String> roles = authMapper.findUserRoles(userId);
        roles.forEach(role ->
                authorities.add(new SimpleGrantedAuthority(role))
        );

        // 2. 查询权限（格式：system:user:view）
        List<String> permissions = authMapper.findUserPermissions(userId);
        permissions.forEach(permission ->
                authorities.add(new SimpleGrantedAuthority(permission))
        );

        return authorities;
    }
}
