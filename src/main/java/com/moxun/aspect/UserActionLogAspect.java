package com.moxun.aspect;

import cn.hutool.core.lang.hash.Hash;
import cn.hutool.json.JSONUtil;
import com.moxun.Enum.ResultCode;
import com.moxun.Pojo.Dto.LoginDTO;
import com.moxun.Pojo.Entity.User;
import com.moxun.Pojo.Entity.UserActionLog;
import com.moxun.annotation.UserAction;
import com.moxun.exception.BusinessException;
import com.moxun.mapper.auth.AuthMapper;
import com.moxun.service.admin.IActionLogService;
import com.moxun.util.IpUtil;
import com.moxun.util.Result;
import com.moxun.util.UserContext;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用户操作日志 - AOP切面
 * 拦截标注了 @UserAction 注解的方法，自动记录操作日志
 */
@Slf4j
@Aspect
@Component
public class UserActionLogAspect {

    /**
     * 请求ID的MDC key
     */
    private static final String REQUEST_ID_KEY = "requestId";

    @Autowired
    private IActionLogService actionLogService;

    @Autowired
    private AuthMapper authMapper;

    /**
     * 环绕通知：拦截所有标注了 @UserAction 注解的方法
     * <p>
     * 切点表达式说明：
     * - @annotation(userAction)：拦截标注了 UserAction 注解的方法
     * - execution(* com.moxun.controller..*(..))：拦截 controller 包下的所有方法
     */
    @Around("@annotation(userAction)")
    public Object around(ProceedingJoinPoint joinPoint, UserAction userAction) throws Throwable {
        long startTime = System.currentTimeMillis();
        UserActionLog actionLog = new UserActionLog();
        Integer responseCode = 200;
        String requestId = MDC.get(REQUEST_ID_KEY);
        // 👑 面试考点：提取到 try 外部，使 finally 块可以访问
//        User currentUser = null;

        try {
            // 1. 获取请求信息
            HttpServletRequest request = getRequest();
            if (request != null) {
                actionLog.setIpAddress(IpUtil.getClientIpAddress(request));
                actionLog.setUserAgent(request.getHeader("User-Agent"));
                actionLog.setRequestUrl(request.getRequestURI());
                actionLog.setRequestMethod(request.getMethod());
            }

            // 2. 设置请求ID（从MDC获取）
            actionLog.setRequestId(requestId);

            // 3. 获取用户信息
//            currentUser = getCurrentUser();

            // 3. 设置操作类型和描述
            actionLog.setActionType(userAction.actionType().getCode());
            actionLog.setActionDesc(userAction.description());
            actionLog.setResourceType(userAction.resourceType());

            // 4. 记录请求参数
            if (userAction.logParams()) {
                String params = getRequestParams(joinPoint);
                actionLog.setRequestParams(params);
            }

            // 5. 执行目标方法
            Object result = joinPoint.proceed();

            // 6. 从响应结果中提取状态码
            Integer resultCode = extractResponseCode(result);
            responseCode = resultCode != null ? resultCode : responseCode;
            actionLog.setResponseCode(responseCode);
            actionLog.setLoginStatus(responseCode != null && responseCode >= 200 && responseCode < 300 ? 1 : 0);

            // 记录错误响应结果（仅当配置开启且返回错误时记录）
            if (userAction.logResult() && result != null) {
                try {
                    if (result instanceof com.moxun.util.Result<?> r && r.getCode() != null && r.getCode() != 200) {
                        String resultStr = JSONUtil.toJsonStr(result);
                        if (resultStr.length() > 2000) {
                            resultStr = resultStr.substring(0, 2000) + "...[truncated]";
                        }
                        actionLog.setResponseResult(resultStr);
                        log.warn("错误响应结果: {}", resultStr);
                    }
                } catch (Exception e) {
                    log.error("序列化响应结果失败", e);
                }
            }

            return result;

        } catch (Exception e) {
            // 根据异常类型动态获取HTTP状态码
            responseCode = getHttpStatusCode(e);
            actionLog.setResponseCode(responseCode);
            actionLog.setLoginStatus(0);
            actionLog.setFailReason(e.getMessage());
            throw e;
        } finally {
            // 7. 计算执行时间
            long duration = System.currentTimeMillis() - startTime;
            actionLog.setDuration((int) duration);
            actionLog.setCreateTime(LocalDateTime.now());

            // 8. 异步保存日志（不阻塞业务）
            try {
                Map<String, Object> userContext = UserContext.getCurrentUser();
                if (userContext != null) {
                    Long userId = (Long) userContext.get("userId");
                    if (userId != null) {
                        actionLog.setUserId(userId);
                    }
                } else {
                    // 处理 userContext 为 null 的情况
                    Long userIdFromArgs = extractUserIdFromArgs(joinPoint);
                    if (userIdFromArgs != null) {
                        actionLog.setUserId(userIdFromArgs);
                        actionLogService.saveLog(actionLog);
                    }
                }
            } catch (Exception e) {
                log.error("保存操作日志失败", e);
            }

            // 打印日志（方便调试）
            if (log.isInfoEnabled()) {
                log.info("操作日志 | requestId: {} | 用户: {} | 操作: {} | 描述: {} | 耗时: {}ms | 状态: {}",
                        requestId,
                        actionLog.getUserId(),
                        actionLog.getActionType(),
                        actionLog.getActionDesc(),
                        duration,
                        actionLog.getLoginStatus() == 1 ? "成功" : "失败");
            }
        }
    }

    /**
     * 获取当前请求
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }


    /**
     * 获取请求参数
     * 支持 @RequestBody 和普通参数
     */
    private String getRequestParams(ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Object[] args = joinPoint.getArgs();

            // 获取方法参数名
            String[] paramNames = signature.getParameterNames();
            if (paramNames == null || paramNames.length == 0) {
                return null;
            }

            // 构建参数映射
            StringBuilder paramsBuilder = new StringBuilder();
            for (int i = 0; i < paramNames.length; i++) {
                String paramName = paramNames[i];
                Object paramValue = args[i];

                // 过滤敏感字段
                if (isSensitiveField(paramName)) {
                    paramsBuilder.append(paramName).append("=[FILTERED]");
                } else if (paramValue != null) {
                    // 过滤密码等敏感信息
                    String valueStr = paramValue.toString();
                    if (valueStr.length() > 500) {
                        valueStr = valueStr.substring(0, 500) + "...[truncated]";
                    }
                    paramsBuilder.append(paramName).append("=").append(valueStr);
                }

                if (i < paramNames.length - 1) {
                    paramsBuilder.append(", ");
                }
            }

            return paramsBuilder.length() > 0 ? paramsBuilder.toString() : null;
        } catch (Exception e) {
            log.warn("获取请求参数失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 判断是否为敏感字段
     * 敏感字段不会被记录到日志中
     */
    private boolean isSensitiveField(String fieldName) {
        if (fieldName == null) {
            return false;
        }
        String lowerField = fieldName.toLowerCase();
        return lowerField.contains("password") ||
                lowerField.contains("pwd") ||
                lowerField.contains("secret") ||
                lowerField.contains("token") ||
                lowerField.contains("key") ||
                lowerField.contains("credential");
    }

    /**
     * 从响应结果中提取状态码
     * 支持 Result<?> 和直接返回的 Integer
     */
    private Integer extractResponseCode(Object result) {
        if (result == null) {
            return null;
        }
        // 如果是 Result 对象
        if (result instanceof com.moxun.util.Result<?> r) {
            return r.getCode();
        }
        // 如果直接返回 Integer
        if (result instanceof Integer i) {
            return i;
        }
        return null;
    }

    /**
     * 根据异常类型获取HTTP状态码
     */
    private Integer getHttpStatusCode(Exception e) {
        String className = e.getClass().getSimpleName();
        return switch (className) {
            case "AccessDeniedException", "SecurityException" -> 403;
            case "AuthenticationException" -> 401;
            case "MethodArgumentNotValidException", "BindException" -> 400;
            case "MissingServletRequestParameterException", "HttpMessageNotReadableException" -> 400;
            case "NoHandlerFoundException" -> 404;
            case "HttpRequestMethodNotSupportedException" -> 405;
            case "HttpMediaTypeNotSupportedException" -> 415;
            default -> 500;
        };
    }

    /**
     * 从请求参数中解析用户名并查询用户ID
     * 用于未登录时通过登录接口的参数反查用户ID
     * 支持：
     * 1. 简单类型参数：String username
     * 2. 对象类型参数：LoginDTO，通过反射获取 username 字段
     */
    private Long extractUserIdFromArgs(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof LoginDTO loginDto) {
                if (StringUtils.isNotBlank(loginDto.getUsername())) {
                    String username = ((LoginDTO) arg).getUsername();
                    User byUserName = authMapper.getByUserName(username);
                    if (Objects.isNull(byUserName)) {
                        log.error("用户名不存在");
                        throw new BusinessException(ResultCode.PARAM_ERROR, "用户名不存在");
                    }
                    return byUserName.getId();
                }
            }
        }
        return null;
    }
}
