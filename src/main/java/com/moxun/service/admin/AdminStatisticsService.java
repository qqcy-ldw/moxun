package com.moxun.service.admin;

import java.util.Map;

/**
 * 管理员 - 数据统计 Service 接口
 */
public interface AdminStatisticsService {

    /**
     * 仪表盘概览数据
     */
    Map<String, Object> getDashboardOverview();
}
