package com.moxun.controller.admin;

import com.moxun.service.admin.AdminStatisticsService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理员 - 数据统计接口
 *
 * 路径前缀：/admin/statistics
 * 权限：ADMIN
 */
@Slf4j
@RestController
@RequestMapping("/admin/statistics")
public class AdminStatisticsController {

    @Autowired
    private AdminStatisticsService adminStatisticsService;

    /**
     * 获取管理后台工作台概览数据
     * GET /admin/statistics/dashboard
     * 返回课程数、用户数、作业数等统计信息
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardOverview() {
        Map<String, Object> data = adminStatisticsService.getDashboardOverview();
        return Result.success(data);
    }
}
