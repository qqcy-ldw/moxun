package com.moxun.service.admin.impl;

import com.moxun.Pojo.Dto.StatisticsDto;
import com.moxun.Pojo.Vo.TrendStatisticsVo;
import com.moxun.mapper.admin.AdminStatisticsMapper;
import com.moxun.service.admin.AdminStatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * 管理员 - 数据统计 Service 实现
 */
@Slf4j
@Service
public class AdminStatisticsServiceImpl implements AdminStatisticsService {

    @Autowired
    private AdminStatisticsMapper adminStatisticsMapper;


    /**
     * 获取仪表盘概览数据
     * 返回课程数、用户数、作业数等统计信息
     */
    @Override
    public Map<String, Integer> getDashboardOverview() {
        HashMap<String, Integer> dashboardOverview = new HashMap<>();
        // 课程数
        dashboardOverview.put("courseCount", adminStatisticsMapper.getCourseCount());
        // 用户数
        dashboardOverview.put("userCount", adminStatisticsMapper.getUserCount());
        // 作业数
        dashboardOverview.put("homeworkCount", adminStatisticsMapper.getHomeworkCount());
        return dashboardOverview;
    }

    /**
     * 返回活跃用户数、新增用户数等统计信息
     */
    @Override
    public Map<String, Integer> getUserStatistics(StatisticsDto statisticsDto) {
        setDefaultDate(statisticsDto);
        HashMap<String, Integer> userStatistics = new HashMap<>();
        userStatistics.put("activeUserCount", adminStatisticsMapper.getActiveUserCount(statisticsDto));
        userStatistics.put("newUserCount", adminStatisticsMapper.getNewUserCount(statisticsDto));
        return userStatistics;
    }

    /**
     * 返回新增课程数、热门课程等统计信息
     */
    @Override
    public Map<String, Integer> getCourseStatistics(StatisticsDto statisticsDto) {
        setDefaultDate(statisticsDto);
        HashMap<String, Integer> courseStatistics = new HashMap<>();
        courseStatistics.put("newCourseCount", adminStatisticsMapper.getNewCourseCount(statisticsDto));
        courseStatistics.put("hotCourseCount", adminStatisticsMapper.getHotCourseCount(statisticsDto));
        return courseStatistics;
    }

    /**
     * 返回新增作业数、统计信息
     */
    @Override
    public HashMap<String, Integer> getHomeworkStatistics(StatisticsDto statisticsDto) {
        setDefaultDate(statisticsDto);
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("newHomeworkCount", adminStatisticsMapper.getNewHomeworkCount(statisticsDto));
        return hashMap;
    }

    /**
     * 获取返回新增问题数、热门问题等统计信息
     */
    @Override
    public HashMap<String, Integer> getQuestionStatistics(StatisticsDto statisticsDto) {
        setDefaultDate(statisticsDto);
        HashMap<String, Integer> map = new HashMap<>();
        map.put("newQuestionCount", adminStatisticsMapper.getNewQuestionCount(statisticsDto));
        map.put("hotQuestionCount", adminStatisticsMapper.getHotQuestionCount(statisticsDto));
        return map;
    }

    /**
     * 获取新增学习数、热门学习等统计信息
     */
    @Override
    public HashMap<String, Integer> getStudyStatistics(StatisticsDto statisticsDto) {
        setDefaultDate(statisticsDto);
        HashMap<String, Integer> map = new HashMap<>();
        map.put("newStudyCount", adminStatisticsMapper.getNewStudyCount(statisticsDto));
        map.put("hotStudyCount", adminStatisticsMapper.getHotStudyCount(statisticsDto));
        return map;
    }

    /**
     * 返回用户数、课程数、作业数、问题数、学习数等统计信息的趋势数据
     */
    @Override
    public TrendStatisticsVo getTrendStatistics(StatisticsDto statisticsDto) {
        setDefaultDate(statisticsDto);
        TrendStatisticsVo userStatisticsVo = new TrendStatisticsVo();
        // 返回日期列表（x轴）
        ArrayList<LocalDate> list = new ArrayList<>();
        userStatisticsVo.setDates(List.of(statisticsDto.getStartDate()));
        if (statisticsDto.getStartDate().isBefore(statisticsDto.getEndDate())){
            list.add(statisticsDto.getStartDate().plusDays(1));
        }
        userStatisticsVo.setDates(list);
        userStatisticsVo.getDates().add(statisticsDto.getStartDate().plusDays(1));
        // 返回数据列表（y轴）
        userStatisticsVo.setUsers(adminStatisticsMapper.getTrendStatisticsUsers(userStatisticsVo.getDates()));
        userStatisticsVo.setCourses(adminStatisticsMapper.getTrendStatisticsCourses(userStatisticsVo.getDates()));
        userStatisticsVo.setAssignments(adminStatisticsMapper.getTrendStatisticsHomeworks(userStatisticsVo.getDates()));
        userStatisticsVo.setQuestions(adminStatisticsMapper.getTrendStatisticsQuestions(userStatisticsVo.getDates()));
        userStatisticsVo.setStudies(adminStatisticsMapper.getTrendStatisticsStudies(statisticsDto));
        return userStatisticsVo;
    }

    /**
     * 学生学习、学生活跃、学生回答、学生选课课程、讨论最多课程、老师的排行榜数据
     */
    @Override
    public HashMap<String, String> getRankStatistics(StatisticsDto statisticsDto) {
        setDefaultDate(statisticsDto);
        HashMap<String, String> date = new HashMap<>();
        date.put("userstudyRank", adminStatisticsMapper.getRankStatisticsUsers(statisticsDto));
        date.put("useractiveRank", adminStatisticsMapper.getRankStatisticsUsersActive(statisticsDto));
        date.put("useranswerRank", adminStatisticsMapper.getRankStatisticsUsersAnswer(statisticsDto));
        date.put("usercourseRank", adminStatisticsMapper.getRankStatisticsUsersCourse(statisticsDto));
        date.put("userdiscussionRank", adminStatisticsMapper.getRankStatisticsUsersDiscussion(statisticsDto));
        date.put("teacherRank", adminStatisticsMapper.getRankStatisticsTeacher(statisticsDto));
        return date;
    }

    /**
     * 设置默认日期
     */
    public static void setDefaultDate(StatisticsDto statisticsDto) {
        if (Objects.isNull(statisticsDto.getStartDate())){
            statisticsDto.setStartDate(LocalDate.now().minusDays(7));
            statisticsDto.setEndDate(LocalDate.now());
        }
    }
}
