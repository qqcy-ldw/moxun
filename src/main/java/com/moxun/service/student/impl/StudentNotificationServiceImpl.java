package com.moxun.service.student.impl;

import com.moxun.Pojo.Entity.Notification;
import com.moxun.service.student.StudentNotificationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生端 - 通知服务实现（占位）
 * 请在此类中实现具体业务逻辑
 */
@Service
public class StudentNotificationServiceImpl implements StudentNotificationService {

    @Override
    public List<Notification> listMyNotifications(Integer isRead, Integer page, Integer pageSize) {
        // TODO: 实现通知列表分页
        return new ArrayList<>();
    }

    @Override
    public long countUnread() {
        // TODO: 实现未读数量统计
        return 0;
    }

    @Override
    public void markAsRead(Long notificationId) {
        // TODO: 实现标记已读
    }

    @Override
    public void markAllAsRead() {
        // TODO: 实现全部已读
    }
}
