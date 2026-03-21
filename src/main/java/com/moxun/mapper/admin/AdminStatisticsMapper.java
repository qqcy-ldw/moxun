package com.moxun.mapper.admin;

import com.moxun.Pojo.Dto.StatisticsDto;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员 - 数据统计 Mapper接口
 传入的 endDate 是 LocalDate 类型，MySQL 会将其隐式转换为当天 00:00:00。要查询 endDate 全天的数据（00:00:00 至 23:59:59），
 需用第二天的 00:00:00 作为上限，因此使用 DATE_ADD(#{endDate}, INTERVAL 1 DAY)。添加一天
 */
@Mapper
public interface AdminStatisticsMapper {

    @Select("SELECT COUNT(*) FROM courses")
    Integer getCourseCount();

    @Select("SELECT COUNT(*) FROM users")
    Integer getUserCount();

    @Select("SELECT COUNT(*) FROM assignments")
    Integer getHomeworkCount();

    //create_time使用函数date()会造成索引失效
    @Select("SELECT COUNT(*) FROM user_action_logs WHERE create_time BETWEEN #{startDate} and DATE_ADD(#{endDate}, INTERVAL 1 DAY)")
    Integer getActiveUserCount(StatisticsDto statisticsDto);

    @Select("SELECT COUNT(*) FROM users WHERE date(create_time) between #{startDate} and #{endDate}")
    Integer getNewUserCount(StatisticsDto statisticsDto);

    @Select("SELECT COUNT(*) from courses where date(create_time) between #{startDate} and #{endDate}")
    Integer getNewCourseCount(StatisticsDto statisticsDto);

    @Select("select count(view_count) from course_daily_statistics where date(create_time) between #{startDate} and #{endDate}")
    Integer getHotCourseCount(StatisticsDto statisticsDto);

    @Select("select count(*) from assignments where date(create_time) between #{startDate} and #{endDate}")
    Integer getNewHomeworkCount(StatisticsDto statisticsDto);

    @Select("select count(*) from questions where date(create_time) between #{startDate} and #{endDate}")
    Integer getNewQuestionCount(StatisticsDto statisticsDto);

    @Select("select count(distinct user_id) from answer_likes where date(create_time) between #{startDate} and #{endDate}")
    Integer getHotQuestionCount(StatisticsDto statisticsDto);

    @Select("select count(*) from user_learning_records where date(create_time) between #{startDate} and #{endDate}")
    Integer getNewStudyCount(StatisticsDto statisticsDto);

    @Select("select count(distinct course_id) from user_learning_records where date(create_time) between #{startDate} and #{endDate}")
    Integer getHotStudyCount(StatisticsDto statisticsDto);


    List<Integer> getTrendStatisticsUsers(List<LocalDate> date);

    List<Integer> getTrendStatisticsCourses(List<LocalDate> dates);

    List<Integer> getTrendStatisticsHomeworks(List<LocalDate> dates);

    List<Integer> getTrendStatisticsQuestions(List<LocalDate> dates);

    List<Integer> getTrendStatisticsStudies(StatisticsDto statisticsDto);

    String getRankStatisticsUsers(StatisticsDto statisticsDto);

    String getRankStatisticsUsersActive(StatisticsDto statisticsDto);

    String getRankStatisticsUsersAnswer(StatisticsDto statisticsDto);

    String getRankStatisticsUsersCourse(StatisticsDto statisticsDto);

    String getRankStatisticsUsersDiscussion(StatisticsDto statisticsDto);

    String getRankStatisticsTeacher(StatisticsDto statisticsDto);
}
