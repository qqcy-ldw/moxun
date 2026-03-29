package com.moxun.service.admin.impl;

import com.moxun.Pojo.Dto.StatisticsDto;
import com.moxun.Pojo.Vo.TrendStatisticsVo;
import com.moxun.exception.BusinessException;
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
        try {
            // 课程数
            dashboardOverview.put("courseCount", adminStatisticsMapper.getCourseCount());
            // 用户数
            dashboardOverview.put("userCount", adminStatisticsMapper.getUserCount());
            // 作业数
            dashboardOverview.put("homeworkCount", adminStatisticsMapper.getHomeworkCount());
        } catch (Exception e) {
            log.error("获取仪表盘概览数据失败{}", e.getMessage());
            throw new BusinessException("获取仪表盘概览数据失败");
        }
        return dashboardOverview;
    }

    /**
     * 返回活跃用户数、新增用户数等统计信息
     */
    @Override
    public Map<String, Integer> getUserStatistics(StatisticsDto statisticsDto) {
        StatisticsDto statisticsDtos = setDefaultDate(statisticsDto);
        HashMap<String, Integer> userStatistics = new HashMap<>();
        try {
            userStatistics.put("activeUserCount", adminStatisticsMapper.getActiveUserCount(statisticsDtos));
            userStatistics.put("newUserCount", adminStatisticsMapper.getNewUserCount(statisticsDtos));
        } catch (Exception e) {
            log.error("获取用户统计数据失败{}", e.getMessage());
            throw new BusinessException("获取用户统计数据失败");
        }
        return userStatistics;
    }

    /**
     * 返回新增课程数、热门课程等统计信息
     */
    @Override
    public Map<String, Integer> getCourseStatistics(StatisticsDto statisticsDto) {
        StatisticsDto statisticsDtos = setDefaultDate(statisticsDto);
        HashMap<String, Integer> courseStatistics = new HashMap<>();
        try {
            courseStatistics.put("newCourseCount", adminStatisticsMapper.getNewCourseCount(statisticsDtos));
            courseStatistics.put("hotCourseCount", adminStatisticsMapper.getHotCourseCount(statisticsDtos));
        } catch (Exception e) {
            log.error("获取课程统计数据失败{}", e.getMessage());
            throw new BusinessException("获取课程统计数据失败");
        }
        return courseStatistics;
    }

    /**
     * 返回新增作业数、统计信息
     */
    @Override
    public HashMap<String, Integer> getHomeworkStatistics(StatisticsDto statisticsDto) {
        StatisticsDto statisticsDtos = setDefaultDate(statisticsDto);
        HashMap<String, Integer> hashMap = new HashMap<>();
        try {
            hashMap.put("newHomeworkCount", adminStatisticsMapper.getNewHomeworkCount(statisticsDtos));
        } catch (Exception e) {
            log.error("获取作业统计数据失败{}", e.getMessage());
            throw new BusinessException("获取作业统计数据失败");
        }
        return hashMap;
    }

    /**
     * 获取返回新增问题数、热门问题等统计信息
     */
    @Override
    public HashMap<String, Integer> getQuestionStatistics(StatisticsDto statisticsDto) {
        StatisticsDto statisticsDtos = setDefaultDate(statisticsDto);
        HashMap<String, Integer> map = new HashMap<>();
        try {
            map.put("newQuestionCount", adminStatisticsMapper.getNewQuestionCount(statisticsDtos));
            map.put("hotQuestionCount", adminStatisticsMapper.getHotQuestionCount(statisticsDtos));
        } catch (Exception e) {
            log.error("获取问题统计数据失败{}", e.getMessage());
            throw new BusinessException("获取问题统计数据失败");
        }
        return map;
    }

    /**
     * 获取新增学习数、热门学习等统计信息
     */
    @Override
    public HashMap<String, Integer> getStudyStatistics(StatisticsDto statisticsDto) {
        StatisticsDto statisticsDtos = setDefaultDate(statisticsDto);
        HashMap<String, Integer> map = new HashMap<>();
        try {
            map.put("newStudyCount", adminStatisticsMapper.getNewStudyCount(statisticsDtos));
            map.put("hotStudyCount", adminStatisticsMapper.getHotStudyCount(statisticsDtos));
        } catch (Exception e) {
            log.error("获取学习统计数据失败{}", e.getMessage());
            throw new BusinessException("获取学习统计数据失败");
        }
        return map;
    }

    /**
     * 返回用户数、课程数、作业数、问题数、学习数等统计信息的趋势数据
     */
    @Override
    public TrendStatisticsVo getTrendStatistics(StatisticsDto statisticsDto) {
        StatisticsDto statisticsDtos = setDefaultDate(statisticsDto);
        TrendStatisticsVo userStatisticsVo = new TrendStatisticsVo();
        // 返回日期列表（x轴）
//        ArrayList<LocalDate> list = new ArrayList<>();
//        userStatisticsVo.setDates(List.of(statisticsDtos.getStartDate()));
//        if (statisticsDtos.getStartDate().isBefore(statisticsDtos.getEndDate())){
//            list.add(statisticsDtos.getStartDate().plusDays(1));
//        }
        List<LocalDate> list = getDateList(statisticsDto);
        userStatisticsVo.setDates(list);
//        userStatisticsVo.getDates().add(statisticsDtos.getStartDate().plusDays(1));
        // 返回数据列表（y轴）
        try {
            userStatisticsVo.setUsers(adminStatisticsMapper.getTrendStatisticsUsers(getDateList(statisticsDtos)));
            userStatisticsVo.setCourses(adminStatisticsMapper.getTrendStatisticsCourses(getDateList(statisticsDtos)));
            userStatisticsVo.setAssignments(adminStatisticsMapper.getTrendStatisticsHomeworks(getDateList(statisticsDtos)));
            userStatisticsVo.setQuestions(adminStatisticsMapper.getTrendStatisticsQuestions(getDateList(statisticsDtos)));
            userStatisticsVo.setStudies(adminStatisticsMapper.getTrendStatisticsStudies(getDateList(statisticsDtos)));
        } catch (Exception e) {
            log.error("获取趋势数据失败", e);
            throw new BusinessException("获取趋势数据失败");
        }
        return userStatisticsVo;
    }

    /**
     * 学生学习（学生名称、学习时长）、
     * 学生活跃（学生名称、登陆次数）、
     * 学生回答（学生名称、回答次数）、
     * 学生选课课程（课程名称、选课人数）、
     * 讨论最多课程（课程名称、讨论次数）、
     * 老师的排行榜数据（教师名称、课程选课人数）
     *
     */
    @Override
    public HashMap<String, HashMap<String, Integer>> getRankStatistics(StatisticsDto statisticsDto) {
        StatisticsDto statisticsDtos = setDefaultDate(statisticsDto);
        HashMap<String, HashMap<String, Integer>> date = new HashMap<>();
        try {
            date.put("userstudyRank", adminStatisticsMapper.getRankStatisticsUsers(statisticsDtos));
            date.put("useractiveRank", adminStatisticsMapper.getRankStatisticsUsersActive(statisticsDtos));
            date.put("useranswerRank", adminStatisticsMapper.getRankStatisticsUsersAnswer(statisticsDtos));
            date.put("usercourseRank", adminStatisticsMapper.getRankStatisticsUsersCourse(statisticsDtos));
            date.put("userdiscussionRank", adminStatisticsMapper.getRankStatisticsUsersDiscussion(statisticsDtos));
            date.put("teacherRank", adminStatisticsMapper.getRankStatisticsTeacher(statisticsDtos));
        } catch (Exception e) {
            log.error("获取排行榜数据失败{}", e.getMessage());
            throw new BusinessException("获取排行榜数据失败");
        }
        return date;
    }

    /**
     * 设置默认日期(7天前)
     */
    public StatisticsDto setDefaultDate(StatisticsDto statisticsDto) {
        if (Objects.isNull(statisticsDto)){
            statisticsDto = new StatisticsDto();
        }
        if (Objects.isNull(statisticsDto.getStartDate())){
            statisticsDto.setStartDate(LocalDate.now().minusDays(7));
            statisticsDto.setEndDate(LocalDate.now());
        }
        return statisticsDto;
    }

    /**
     * 获取日期列表
     */
    public List<LocalDate> getDateList(StatisticsDto statisticsDto) {
        List<LocalDate> dateList = new ArrayList<>();
        // 设置默认日期
        StatisticsDto statisticsDtos = setDefaultDate(statisticsDto);
        LocalDate startDate = statisticsDtos.getStartDate();
        LocalDate endDate = statisticsDtos.getEndDate();
        while (!startDate.isAfter(endDate)) {
            dateList.add(startDate);
            startDate = startDate.plusDays(1);
        }
        return dateList;
    }
}
