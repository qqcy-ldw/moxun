package com.moxun.service.student.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.moxun.Pojo.Entity.Notification;
import com.moxun.mapper.student.StudentNotificationMapper;
import com.moxun.service.student.StudentNotificationService;
import com.moxun.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 学生端 - 通知服务实现
 *
 * 👑 面试考点：核心功能
 * 1. 通知列表：支持按已读状态筛选
 * 2. 未读数统计：COUNT 查询
 * 3. 已读操作：UPDATE 批量/单条更新
 *
 * 👑 面试考点：技术要点
 * - 用户隔离：所有查询都带 userId 条件
 * - 批量操作：markAllAsRead 用 UPDATE ... WHERE 一次性更新
 * - PageHelper 分页插件
 */
@Slf4j
@Service
public class StudentNotificationServiceImpl implements StudentNotificationService {

    @Autowired
    private StudentNotificationMapper studentNotificationMapper;

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        Map<String, Object> user = UserContext.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("用户未登录");
        }
        return (Long) user.get("userId");
    }

    /**
     * 查询我的通知列表
     * 👑 面试考点：PageHelper 分页插件
     */
    @Override
    public PageInfo<Notification> listMyNotifications(Integer isRead, Integer page, Integer pageSize) {
        Long userId = getCurrentUserId();

        PageHelper.startPage(page, pageSize);
        List<Notification> list = studentNotificationMapper.listMyNotifications(userId, isRead);

        return new PageInfo<>(list);
    }

    /**
     * 统计未读通知数量
     */
    @Override
    public long countUnread() {
        Long userId = getCurrentUserId();
        return studentNotificationMapper.countUnread(userId);
    }

    /**
     * 标记单条通知已读
     */
    @Override
    public void markAsRead(Long notificationId) {
        Long userId = getCurrentUserId();
        studentNotificationMapper.markAsRead(notificationId, userId);
        log.info("用户 {} 标记通知 {} 为已读", userId, notificationId);
    }

    /**
     * 全部标记已读
     */
    @Override
    public void markAllAsRead() {
        Long userId = getCurrentUserId();
        studentNotificationMapper.markAllAsRead(userId);
        log.info("用户 {} 全部标记通知为已读", userId);
    }
}
