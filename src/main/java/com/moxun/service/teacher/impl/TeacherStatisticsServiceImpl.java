package com.moxun.service.teacher.impl;

import com.moxun.service.teacher.TeacherStatisticsService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 教师端 - 统计服务实现（占位）
 */
@Service
public class TeacherStatisticsServiceImpl implements TeacherStatisticsService {

    @Override
    public Map<String, Object> getMyDashboardOverview() {
        // TODO: 我的课程数、学生数、待批改作业数等
        return new HashMap<>();
    }
}
