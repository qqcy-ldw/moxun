package com.moxun.service.student;

import com.moxun.Pojo.Entity.Notification;

import java.util.List;

/**
 * 学生端 - 通知服务接口
 * 请自行实现 StudentNotificationServiceImpl
 */
public interface StudentNotificationService {

    /**
     * 我的通知列表（可分页）
     *
     * @param isRead 是否已读，null 表示全部
     */
    List<Notification> listMyNotifications(Integer isRead, Integer page, Integer pageSize);

    /**
     * 未读数量
     */
    long countUnread();

    /**
     * 标记已读
     */
    void markAsRead(Long notificationId);

    /**
     * 全部标记已读
     */
    void markAllAsRead();
}
