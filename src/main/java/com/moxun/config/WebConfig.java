package com.moxun.config;

import com.moxun.interceptor.AuthInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 使用springSecurity的过滤器
 */
@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Autowired
//    private AuthInterceptor authInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        log.info("开始注册自定义拦截器...");
//        registry.addInterceptor(authInterceptor)
//                .addPathPatterns("/**") // 拦截路径
//                .excludePathPatterns(
//                    "/auth/api/v1/login",
//                    "/auth/api/v1/register",
//                    "/auth/api/v1/captcha",
//                    "/error",           // 排除错误页面
//                    "/test"             // 修正没有/的路径
//            );
//    }
}
