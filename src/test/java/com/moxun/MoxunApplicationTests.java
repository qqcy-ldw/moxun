package com.moxun;

import com.moxun.mapper.auth.AuthMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MoxunApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthMapper authMapper;

    @Test
    void contextLoads() {
        System.out.println(passwordEncoder.encode("123456"));
        System.out.println(passwordEncoder.matches("123456", "$2a$10$IabdDxiiexD1R/mhJB2L/.wM6SOwDLcdVtjGDjSkxOVPph/K/UHGO"));
        System.out.println(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("郑静", "123456")));
    }

    @Test
    void test() {
        List<String> authorities = new ArrayList<>();

        // 1. 查询角色（格式：ROLE_角色名）
        List<String> roles = authMapper.findUserRoles(10L);
        roles.forEach(role ->
                authorities.add(role)
        );

        // 2. 查询权限（格式：system:user:view）
        List<String> permissions = authMapper.findUserPermissions(10L);
        permissions.forEach(permission ->
                authorities.add(permission)
        );

        System.out.println(permissions);

    }
}
