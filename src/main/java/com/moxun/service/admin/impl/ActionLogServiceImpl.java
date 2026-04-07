package com.moxun.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moxun.Pojo.Dto.ActionLogDto;
import com.moxun.Pojo.Entity.UserActionLog;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.exception.BusinessException;
import com.moxun.mapper.admin.ActionLogMapper;
import com.moxun.service.admin.IActionLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 用户操作日志 - Service实现
 */
@Slf4j
@Service
public class ActionLogServiceImpl implements IActionLogService {

    @Autowired
    private ActionLogMapper actionLogMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 异步保存操作日志
     * 使用 @Async 实现异步记录，不影响业务性能
     */
    @Async
    @Override
    public void saveLog(UserActionLog userActionLog) {
        try {
            log.info("保存操作日志成功: {}", userActionLog);
            actionLogMapper.insert(userActionLog);
        } catch (Exception e) {
            log.error("保存操作日志失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 分页查询日志列表
     */
    @Override
    public PageResult getLogPage(ActionLogDto dto) {
        try {
            PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
            Page<UserActionLog> page = (Page<UserActionLog>) actionLogMapper.selectByPage(dto);
            return new PageResult(page.getTotal(), page.getResult());
        } catch (Exception e) {
            log.error("分页查询日志失败: {}", e.getMessage(), e);
            throw new BusinessException("分页查询日志失败");
        }
    }

    /**
     * 根据ID查询日志详情
     */
    @Override
    public UserActionLog getLogById(Long id) {
        try {
            return actionLogMapper.selectById(id);
        } catch (Exception e) {
            log.error("查询日志详情失败: {}", e.getMessage(), e);
            throw new BusinessException("查询日志详情失败");
        }
    }

    /**
     * 获取用户最近操作记录
     */
    @Override
    public List<UserActionLog> getRecentByUserId(Long userId, Integer limit) {
        try {
            return actionLogMapper.selectRecentByUserId(userId, limit);
        } catch (Exception e) {
            log.error("查询用户最近操作记录失败: {}", e.getMessage(), e);
            throw new BusinessException("查询用户最近操作记录失败");
        }
    }

    /**
     * 清理指定天数之前的日志
     */
    @Override
    public int cleanOldLogs(Integer days) {
        try {
            LocalDateTime beforeTime = LocalDateTime.now().minusDays(days);
            String timeStr = beforeTime.format(DATE_FORMATTER);
            return actionLogMapper.deleteBeforeTime(timeStr);
        } catch (Exception e) {
            log.error("清理过期日志失败: {}", e.getMessage(), e);
            throw new BusinessException("清理过期日志失败");
        }
    }
}
