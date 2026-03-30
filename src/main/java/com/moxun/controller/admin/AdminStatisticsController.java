package com.moxun.controller.admin;

import com.moxun.Pojo.Dto.StatisticsDto;
import com.moxun.Pojo.Vo.TrendStatisticsVo;
import com.moxun.service.admin.AdminStatisticsService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员 - 数据统计接口
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
     * 返回全部课程数、用户数、作业数等统计信息
     * TODO 返回字段不完整，缺少 questionCount
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public Result<Map<String, Integer>> getDashboardOverview() {
        Map<String, Integer> data = adminStatisticsService.getDashboardOverview();
        return Result.success(data);
    }

    /**
     * 获取用户相关统计数据
     * GET /admin/statistics/user
     * 返回用户数、活跃用户数、新增用户数等统计信息
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/user")
    public Result<Map<String, Integer>> getUserStatistics(@Validated @RequestBody StatisticsDto statisticsDto) {
        Map<String, Integer> data = adminStatisticsService.getUserStatistics(statisticsDto);
        return Result.success(data);
    }


    /**
     * 获取课程相关统计数据
     * GET /admin/statistics/course
     * 返回课程数、新增课程数、热门课程等统计信息
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/course")
    public Result<Map<String, Integer>> getCourseStatistics(@Validated @RequestBody StatisticsDto statisticsDto) {
        Map<String, Integer> data = adminStatisticsService.getCourseStatistics(statisticsDto);
        return Result.success(data);
    }

    /**
     * 获取作业相关统计数据
     * GET /admin/statistics/homework
     * 返回作业数、新增作业数、等统计信息
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/homework")
    public Result<Map<String, Integer>> getHomeworkStatistics(@Validated @RequestBody StatisticsDto statisticsDto) {
        HashMap<String, Integer> data = adminStatisticsService.getHomeworkStatistics(statisticsDto);
        return Result.success(data);
    }

    /**
     * 获取问答相关统计数据
     * GET /admin/statistics/question
     * 返回问题数、新增问题数、热门问题等统计信息
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/question")
    public Result<Map<String, Integer>> getQuestionStatistics(@Validated @RequestBody StatisticsDto statisticsDto) {
        HashMap<String, Integer> map =  adminStatisticsService.getQuestionStatistics(statisticsDto);
        return Result.success(map);
    }

    /**
     * 获取学习相关统计数据
     * GET /admin/statistics/study
     * 返回学习数、新增学习数、热门学习等统计信息
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/study")
    public Result<Map<String, Integer>> getStudyStatistics(@Validated @RequestBody StatisticsDto statisticsDto) {
        HashMap<String, Integer> map = adminStatisticsService.getStudyStatistics(statisticsDto);
        return Result.success(map);
    }

    /**
     * 获取趋势数据
     * GET /admin/statistics/trend
     * 返回用户数、课程数、作业数、问题数、学习数等统计信息的趋势数据
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/trend")
    public Result<TrendStatisticsVo> getTrendStatistics(@Validated @RequestBody(required = false) StatisticsDto statisticsDto) {
        TrendStatisticsVo data = adminStatisticsService.getTrendStatistics(statisticsDto);
        log.info("趋势数据: {}", data);
        return Result.success(data);
    }

    /**
     * 获取排行榜数据
     * GET /admin/statistics/rank
     * 学生学习、学生活跃、学生回答、学生选课课程、讨论最多课程、老师的排行榜数据
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/rank")
    public Result<HashMap<String, List<HashMap<String, Integer>>>> getRankStatistics(@Validated @RequestBody(required = false) StatisticsDto statisticsDto) {
        HashMap<String, List<HashMap<String, Integer>>> data = adminStatisticsService.getRankStatistics(statisticsDto);
        log.info("排行榜数据: {}", data);
        return Result.success(data);
    }

}
