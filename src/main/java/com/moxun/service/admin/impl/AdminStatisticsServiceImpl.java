package com.moxun.service.admin.impl;

import com.moxun.service.admin.AdminStatisticsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员 - 数据统计 Service 实现（占位）
 */
@Service
public class AdminStatisticsServiceImpl implements AdminStatisticsService {

    @Override
    public Map<String, Object> getDashboardOverview() {
        // TODO: 实现仪表盘数据（用户数、课程数、今日活跃等）
        return new HashMap<>();
    }
}
