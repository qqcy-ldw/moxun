package com.moxun.util;

import java.util.Map;

/**
 * 获取用户信息
 */
public class UserContext {
    private static final ThreadLocal<Map<String, Object>> CURRENT_USER = new ThreadLocal<>();

    public static void setCurrentUser(Map<String, Object> user) {
        CURRENT_USER.set(user);
    }

    public static Map<String, Object> getCurrentUser() {
        return CURRENT_USER.get();
    }

    public static void clear() {
        CURRENT_USER.remove();
    }
}
