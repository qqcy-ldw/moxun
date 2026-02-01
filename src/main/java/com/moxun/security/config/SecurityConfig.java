package com.moxun.security.config;


import com.alibaba.fastjson.JSON;
import com.moxun.Enum.ResultCode;
import com.moxun.security.filter.JwtAuthenticationTokenFilter;
import com.moxun.util.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring Security 核心配置类
 * 
 * 主要功能：
 * 1. 配置哪些URL需要认证、哪些不需要
 * 2. 配置密码加密器（支持MD5和BCrypt）
 * 3. 配置JWT过滤器
 * 4. 配置异常处理（认证失败、权限不足）
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
    prePostEnabled = true,  // 开启方法级权限控制 @PreAuthorize
    securedEnabled = true   // 开启 @Secured 注解支持
)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 白名单：这些路径不需要登录即可访问
     */
    private static final String[] URL_PERMITTED_LIST = {
            "/auth/api/v1/login",
            "/auth/api/v1/register",
            "/auth/api/v1/captcha",
            "/auth/api/v1/getLoginIp",
            "/error",
            "/test",
            "/password",
            "/image/**"
    };

    /**
     * 配置安全过滤链
     * 这是Spring Security的核心配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 关闭CSRF（因为使用JWT，不需要CSRF保护）
                .csrf(csrf -> csrf.disable())

                // 2. 配置Session管理：无状态（不使用Session，使用JWT）
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3. 配置URL访问权限
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(URL_PERMITTED_LIST).permitAll()  // 白名单
                        .anyRequest().authenticated()                      // 其他需要认证
                )

                // 4. 添加JWT过滤器（在UsernamePasswordAuthenticationFilter之前执行）
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class)

                // 5. 配置异常处理
                .exceptionHandling(exception -> exception
                        // 未认证异常处理（401）- 用户未登录或Token无效
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.warn("未认证访问: {}, 错误: {}", request.getRequestURI(), authException.getMessage());
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json;charset=UTF-8");
                            Result<?> result = Result.error(ResultCode.UNAUTHORIZED);
                            String json = JSON.toJSONString(result);
                            response.getWriter().write(json);
                            response.getWriter().flush();
                        })
                        // 权限不足异常处理（403）
                        // 注意：@PreAuthorize 抛出的异常不会经过这里，会被 GlobalExceptionHandler 处理
                        // 这里主要处理 URL 级别的权限控制异常
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.warn("权限不足访问: {}, 错误: {}", request.getRequestURI(), accessDeniedException.getMessage());
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json;charset=UTF-8");
                            Result<?> result = Result.error(ResultCode.FORBIDDEN);
                            String json = JSON.toJSONString(result);
                            response.getWriter().write(json);
                            response.getWriter().flush();
                        })
                );

        return http.build();
    }

    /**
     * 认证管理器
     * 用于在登录时进行用户名密码验证
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * 密码加密器 - 支持多种加密方式
     * 密码存储格式：
     * - BCrypt: {bcrypt}$2a$10$xxxxx
     * - MD5: {MD5}e10adc3949ba59abbe56e057f20f883e
     * - 无前缀: 默认使用bcrypt验证
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 创建加密器映射
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        
        // BCrypt加密器（推荐使用，更安全）
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        
        // MD5加密器（兼容老数据）
        @SuppressWarnings("deprecation")
        MessageDigestPasswordEncoder md5Encoder = new MessageDigestPasswordEncoder("MD5");
        encoders.put("MD5", md5Encoder);
        
        // 默认使用BCrypt
        DelegatingPasswordEncoder delegatingPasswordEncoder = 
                new DelegatingPasswordEncoder("bcrypt", encoders);
        
        // 设置默认编码器（当密码没有{xxx}前缀时使用）
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(encoders.get("bcrypt"));
        
        return delegatingPasswordEncoder;
    }

    /**
     * 配置认证提供者
     * 连接UserDetailsService和PasswordEncoder
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        // 隐藏"用户不存在"异常，统一返回"用户名或密码错误"（安全考虑）
        // true: 用户不存在时也抛出 BadCredentialsException，防止用户名枚举攻击
        // false: 用户不存在时抛出 UsernameNotFoundException，便于调试
        provider.setHideUserNotFoundExceptions(true);
        return provider;
    }
}
