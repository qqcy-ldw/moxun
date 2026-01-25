package com.moxun.interceptor;


import com.alibaba.fastjson.JSON;
import com.moxun.Enum.ResultCode;
import com.moxun.util.Jwt;
import com.moxun.util.Result;
import com.moxun.util.UserContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用springSecurity的过滤器
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        String requestURI = request.getRequestURI();
//        String token = request.getHeader("Authorization");
//        log.info("token：{}", token);
//        if (token == null){
//        String jsonString = JSON.toJSONString(Result.error(ResultCode.UNAUTHORIZED.getCode(), ResultCode.UNAUTHORIZED.getMessage()));
//        response.setCharacterEncoding("UTF-8"); // 强制响应体使用 UTF-8 编码
//        response.setContentType("application/json;charset=UTF-8"); // 告诉前端这是 JSON 且编码为 UTF-8
//        response.getWriter().write(jsonString);
//        return false;
//        }
//        try {
//            if (token != null && token.startsWith("Bearer ")) {
//                token = token.substring(7);
//                Claims claims = Jwt.validateToken(token);
//                HashMap<String, Object> map = new HashMap<>();
//                map.put("userId",claims.get("userId"));
//                map.put("userName",claims.get("userName"));
//                map.put("claims",claims.toString());
//                UserContext.setCurrentUser(map);
//            }
//                return true;
//        } catch (ExpiredJwtException e) {
//            response.setStatus(ResultCode.TOKEN_EXPIRED.getCode());
//            response.getWriter().write(JSON.toJSONString("令牌已过期"));
//            return false;
//        }catch (MalformedJwtException e) {
//            log.warn("Token格式错误: {}", requestURI);
//            response.setStatus(ResultCode.TOKEN_INVALID.getCode());
//            response.getWriter().write(JSON.toJSONString("令牌格式错误"));
//            return false;
//        } catch (Exception e) {
//            log.error("Token验证异常: {}, 错误: {}", requestURI, e.getMessage());
//            response.setStatus(ResultCode.TOKEN_INVALID.getCode());
//            response.getWriter().write(JSON.toJSONString("令牌验证异常"));
//            return false;
//        }
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        UserContext.clear();
//    }
}
