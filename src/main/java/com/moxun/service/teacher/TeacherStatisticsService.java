package com.moxun.service.teacher;

import java.util.Map;

/**
 * 教师端 - 统计服务接口
 */
public interface TeacherStatisticsService {

    Map<String, Object> getMyDashboardOverview();
}
