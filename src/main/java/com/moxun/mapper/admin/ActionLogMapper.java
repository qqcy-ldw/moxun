package com.moxun.mapper.admin;

import com.moxun.Pojo.Dto.ActionLogDto;
import com.moxun.Pojo.Entity.UserActionLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户操作日志 - Mapper接口
 */
@Mapper
public interface ActionLogMapper {

    /**
     * 插入操作日志
     */
    int insert(UserActionLog userActionLog);

    /**
     * 分页查询日志列表（配合 PageHelper 使用）
     */
    List<UserActionLog> selectByPage(@Param("dto") ActionLogDto dto);

    /**
     * 根据ID查询日志
     */
    UserActionLog selectById(@Param("id") Long id);

    /**
     * 根据用户ID查询最近操作
     */
    List<UserActionLog> selectRecentByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);

    /**
     * 清空指定天数之前的日志
     */
    int deleteBeforeTime(@Param("time") String time);
}
