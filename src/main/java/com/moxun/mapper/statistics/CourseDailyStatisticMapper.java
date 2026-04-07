package com.moxun.mapper.statistics;

import com.moxun.Pojo.Entity.CourseDailyStatistic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 课程每日统计 Mapper
 */
@Mapper
public interface CourseDailyStatisticMapper {

    /**
     * 查询某课程在指定日期是否已有统计记录
     */
    @Select("SELECT COUNT(*) FROM course_daily_statistics WHERE course_id = #{courseId} AND stat_date = #{statDate}")
    int countByCourseAndDate(@Param("courseId") Long courseId, @Param("statDate") String statDate);

    /**
     * 批量插入课程日统计记录
     */
    int batchInsert(@Param("list") java.util.List<CourseDailyStatistic> list);

    /**
     * 批量更新课程日统计记录（按 courseId + statDate 更新统计字段）
     */
    int batchUpdate(@Param("list") java.util.List<CourseDailyStatistic> list);

    /**
     * 查询所有课程ID（用于遍历每日统计）
     */
    @Select("SELECT id FROM courses WHERE status = 1")
    java.util.List<Long> selectAllActiveCourseIds();

    /**
     * 删除指定日期的所有课程统计记录（用于重跑数据）
     */
    int deleteByDate(@Param("statDate") String statDate);
}
