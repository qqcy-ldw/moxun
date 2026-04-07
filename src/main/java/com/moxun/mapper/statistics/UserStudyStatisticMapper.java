package com.moxun.mapper.statistics;

import com.moxun.Pojo.Entity.UserStudyStatistic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户学习统计 Mapper
 */
@Mapper
public interface UserStudyStatisticMapper {

    /**
     * 查询某用户在指定日期是否已有统计记录
     */
    @Select("SELECT COUNT(*) FROM user_study_statistics WHERE user_id = #{userId} AND stat_date = #{statDate}")
    int countByUserAndDate(@Param("userId") Long userId, @Param("statDate") String statDate);

    /**
     * 批量插入用户学习统计记录
     */
    int batchInsert(@Param("list") List<UserStudyStatistic> list);

    /**
     * 批量更新用户学习统计记录
     */
    int batchUpdate(@Param("list") List<UserStudyStatistic> list);

    /**
     * 删除指定日期的所有用户学习统计记录（用于重跑数据）
     */
    int deleteByDate(@Param("statDate") String statDate);
}
