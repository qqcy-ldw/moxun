package com.moxun.security.filter;

import com.alibaba.fastjson.JSON;
import com.moxun.Enum.ResultCode;
import com.moxun.util.Jwt;
import com.moxun.util.Result;
import com.moxun.util.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * JWT认证过滤器
 * 
 * 【作用】
 * 1. 从请求头获取JWT Token
 * 2. 验证Token有效性
 * 3. 解析Token获取用户信息
 * 4. 将用户信息存入Spring Security上下文
 * 
 * 【继承OncePerRequestFilter的原因】
 * 确保每个请求只执行一次过滤逻辑
 */
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 白名单：这些路径不需要JWT认证
     */
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/auth/api/login",
            "/auth/api/register",
            "/auth/api/captcha",
            "/auth/api/getLoginIp",
            "/error",
            "/test",
            "/password",
            "/image/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String requestURI = request.getRequestURI();
        log.debug("JWT Filter 处理请求: {}", requestURI);

        // 1. 白名单路径直接放行
        if (isWhiteList(requestURI)) {
            log.debug("白名单路径，直接放行: {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 获取Token
        String token = extractToken(request);
        if (token == null) {
            log.warn("请求头中没有Token: {}", requestURI);
            responseUnauthorized(response, "未提供认证令牌");
            return;
        }

        try {
            // 3. 验证Token并解析用户信息
            Claims claims = Jwt.validateToken(token);
            String username = (String) claims.get("userName");
            Long userId = Long.valueOf(claims.get("userId").toString());


            log.info("Token验证成功 - 用户: {}, ID: {}", username, userId);

            // 4. 将用户信息存入ThreadLocal（业务层使用）
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("userId", userId);
            userMap.put("userName", username);
            userMap.put("claims", claims.toString());
            UserContext.setCurrentUser(userMap);

            // 5. 如果Spring Security上下文中还没有认证信息，则加载用户详情
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                // 从数据库加载用户详情（包括权限）
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // 创建认证对象
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                                userDetails,           // principal（主体）
                                null,                  // credentials（凭证）不需要存密码
                                userDetails.getAuthorities()  // authorities（权限列表）
                        );
                
                // 设置请求详情（IP、SessionId等）
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // 将认证信息存入Spring Security上下文
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.info("用户认证成功 - 用户: {}, 权限: {}", username, userDetails.getAuthorities());
            }

            // 6. 继续执行后续过滤器
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException e) {
            log.warn("Token已过期: {}", requestURI);
            responseUnauthorized(response, "令牌已过期，请重新登录");
            
        } catch (MalformedJwtException e) {
            log.warn("Token格式错误: {}", requestURI);
            responseUnauthorized(response, "令牌格式错误");
            
        } catch (Exception e) {
            log.error("Token验证异常: {}, 错误: {}", requestURI, e.getMessage());
            responseUnauthorized(response, "令牌验证失败");
        } finally {
            // 清理ThreadLocal，避免内存泄漏
            // 注意：只在请求结束时清理，这里不清理
        }
    }

    /**
     * 判断是否是白名单路径
     */
    private boolean isWhiteList(String requestURI) {
        return WHITE_LIST.stream().anyMatch(pattern -> {
            if (pattern.endsWith("/**")) {
                String prefix = pattern.substring(0, pattern.length() - 3);
                return requestURI.startsWith(prefix);
            }
            return requestURI.equals(pattern);
        });
    }

    /**
     * 从请求头中提取Token
     * 支持两种格式：
     * 1. Authorization: Bearer xxxxx
     * 2. Authorization: xxxxx
     */
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            if (authHeader.startsWith("Bearer ")) {
                return authHeader.substring(7);
            }
            return authHeader;
        }
        return null;
    }

    /**
     * 返回未认证错误响应
     */
    private void responseUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        Result<?> result = Result.error(ResultCode.UNAUTHORIZED.getCode(), message);
        response.getWriter().write(JSON.toJSONString(result));
    }
}
