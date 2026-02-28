package com.moxun.controller.teacher;

import com.moxun.service.teacher.TeacherStatisticsService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 教师端 - 数据统计接口
 *
 * 路径前缀：/teacher/statistics
 * 权限：TEACHER 或 ADMIN
 * 说明：统计当前教师自己课程的数据
 */
@Slf4j
@RestController
@RequestMapping("/teacher/statistics")
@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
public class TeacherStatisticsController {

    @Autowired
    private TeacherStatisticsService teacherStatisticsService;

    /**
     * 获取教师工作台概览数据
     * GET /teacher/statistics/dashboard
     * 返回课程数、学生数、作业数、问答数等
     */
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getMyDashboardOverview() {
        Map<String, Object> data = teacherStatisticsService.getMyDashboardOverview();
        return Result.success(data);
    }
}
