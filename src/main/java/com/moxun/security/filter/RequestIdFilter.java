package com.moxun.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * 请求ID过滤器
 * <p>
 * 【作用】
 * 1. 为每个请求生成唯一请求ID
 * 2. 将请求ID存入MDC，实现日志链路追踪
 * 3. 将请求ID添加到响应头，便于前端排查问题
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdFilter extends OncePerRequestFilter {

    /**
     * 请求ID的MDC key
     */
    private static final String REQUEST_ID_KEY = "requestId";

    /**
     * 响应头中请求ID的key
     */
    private static final String RESPONSE_HEADER_KEY = "X-Request-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. 生成请求ID
        String requestId = generateRequestId();

        try {
            // 2. 将请求ID存入MDC（后续日志可自动打印）
            MDC.put(REQUEST_ID_KEY, requestId);

            // 3. 将请求ID添加到响应头
            response.setHeader(RESPONSE_HEADER_KEY, requestId);

            // 4. 记录请求开始
            log.info("==> 请求开始 | requestId: {} | method: {} | uri: {}",
                    requestId, request.getMethod(), request.getRequestURI());

            // 5. 继续执行过滤器链
            filterChain.doFilter(request, response);

        } finally {
            // 6. 清理MDC（防止内存泄漏）
            MDC.remove(REQUEST_ID_KEY);
        }
    }

    /**
     * 生成请求ID
     * 格式：UUID（32位无横线）
     */
    private String generateRequestId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
