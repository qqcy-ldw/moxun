package com.moxun.service.admin;

import com.moxun.Pojo.Dto.StatisticsDto;
import com.moxun.Pojo.Vo.TrendStatisticsVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员 - 数据统计 Service 接口
 */
public interface AdminStatisticsService {

    /**
     * 返回课程数、用户数、作业数等统计信息
     */
    Map<String, Integer> getDashboardOverview();

    Map<String, Integer> getUserStatistics(StatisticsDto statisticsDto);

    Map<String, Integer> getCourseStatistics(StatisticsDto statisticsDto);

    HashMap<String, Integer> getHomeworkStatistics(StatisticsDto statisticsDto);

    HashMap<String, Integer> getQuestionStatistics(StatisticsDto statisticsDto);

    HashMap<String, Integer> getStudyStatistics(StatisticsDto statisticsDto);

    TrendStatisticsVo getTrendStatistics(StatisticsDto statisticsDto);

    HashMap<String, HashMap<String, Integer>> getRankStatistics(StatisticsDto statisticsDto);
}
