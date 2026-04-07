package com.moxun.service.admin;

import com.moxun.Pojo.Dto.ActionLogDto;
import com.moxun.Pojo.Entity.UserActionLog;
import com.moxun.Pojo.Vo.PageResult;

import java.util.List;

/**
 * 用户操作日志 - Service接口
 */
public interface IActionLogService {

    /**
     * 记录操作日志
     */
    void saveLog(UserActionLog userActionLog);

    /**
     * 分页查询日志列表
     */
    PageResult getLogPage(ActionLogDto dto);

    /**
     * 根据ID查询日志详情
     */
    UserActionLog getLogById(Long id);

    /**
     * 获取用户最近操作记录
     */
    List<UserActionLog> getRecentByUserId(Long userId, Integer limit);

    /**
     * 清理指定天数之前的日志
     */
    int cleanOldLogs(Integer days);
}
