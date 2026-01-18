package com.moxun.util;

import jakarta.servlet.http.HttpServletRequest;

public class IpUtil {
    /**
     * 获取客户端IPv4地址
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String ip = null;

        // 尝试按优先级从各个请求头获取IP
        String[] headerNames = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_CLIENT_IP",
                "HTTP_X_FORWARDED_FOR"
        };

        for (String headerName : headerNames) {
            ip = request.getHeader(headerName);
            if (isValidIp(ip)) {
                break;
            }
        }

        // 如果所有请求头都没有有效IP，使用getRemoteAddr()
        if (!isValidIp(ip)) {
            ip = request.getRemoteAddr();
        }

        // 处理多个IP的情况（通过代理）
        ip = extractFirstValidIp(ip);

        // 如果是IPv6的回环地址，转换为IPv4
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }

        return ip;
    }

    /**
     * 检查IP是否有效且不为"unknown"
     */
    private static boolean isValidIp(String ip) {
        return ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip);
    }

    /**
     * 从逗号分隔的IP列表中提取第一个有效的IPv4地址
     */
    private static String extractFirstValidIp(String ipList) {
        if (ipList == null || ipList.length() == 0) {
            return ipList;
        }

        // 按逗号分隔
        String[] ips = ipList.split(",");

        for (String ip : ips) {
            String trimmedIp = ip.trim();

            // 跳过"unknown"
            if ("unknown".equalsIgnoreCase(trimmedIp)) {
                continue;
            }

            // 如果是有效的IPv4地址，返回
            if (isValidIPv4(trimmedIp)) {
                return trimmedIp;
            }
        }

        // 如果没有找到IPv4，返回第一个非unknown的IP
        for (String ip : ips) {
            String trimmedIp = ip.trim();
            if (!"unknown".equalsIgnoreCase(trimmedIp)) {
                return trimmedIp;
            }
        }

        return ipList.split(",")[0].trim();
    }

    /**
     * 验证是否为有效的IPv4地址
     */
    private static boolean isValidIPv4(String ip) {
        if (ip == null || ip.length() == 0) {
            return false;
        }

        // IPv6地址包含冒号，IPv4不包含（除了IPv4映射的IPv6地址）
        if (ip.contains(":")) {
            // 可能是IPv6或IPv4映射的IPv6
            // 检查是否是IPv4映射的IPv6地址 (::ffff:192.168.1.1)
            if (ip.startsWith("::ffff:")) {
                String ipv4Part = ip.substring(7); // 去掉"::ffff:"
                return isPureIPv4(ipv4Part);
            }
            return false; // 纯IPv6
        }

        return isPureIPv4(ip);
    }

    /**
     * 验证是否为纯IPv4地址（格式：xxx.xxx.xxx.xxx）
     */
    private static boolean isPureIPv4(String ip) {
        String ipv4Pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(ipv4Pattern);
    }
}
