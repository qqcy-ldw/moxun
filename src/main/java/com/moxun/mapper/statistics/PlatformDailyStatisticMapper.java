package com.moxun.mapper.statistics;

import com.moxun.Pojo.Entity.PlatformDailyStatistic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 平台每日统计 Mapper
 */
@Mapper
public interface PlatformDailyStatisticMapper {

    /**
     * 查询指定日期的统计记录是否存在（用于 upsert 判断）
     */
    @Select("SELECT COUNT(*) FROM platform_daily_statistics WHERE stat_date = #{statDate}")
    int countByDate(@Param("statDate") String statDate);

    /**
     * 插入单日平台统计记录
     */
    int insert(PlatformDailyStatistic stat);

    /**
     * 更新指定日期的平台统计记录（仅更新统计字段，不更新 id/createTime）
     */
    int updateByDate(PlatformDailyStatistic stat);

    /**
     * 删除指定日期的记录（用于重跑数据时清理）
     */
    int deleteByDate(@Param("statDate") String statDate);
}
