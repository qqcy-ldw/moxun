package com.moxun.mapper.student;

import com.moxun.Pojo.Entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生端 - 通知 Mapper
 * 提供通知列表、已读状态等数据访问操作
 */
@Mapper
public interface StudentNotificationMapper {

    /**
     * 查询我的通知列表
     *
     * @param userId 当前用户ID
     * @param isRead 是否已读（null-全部，0-未读，1-已读）
     * @return 通知列表
     */
    List<Notification> listMyNotifications(
            @Param("userId") Long userId,
            @Param("isRead") Integer isRead
    );

    /**
     * 统计未读通知数量
     *
     * @param userId 用户ID
     * @return 未读数量
     */
    long countUnread(@Param("userId") Long userId);

    /**
     * 标记单条通知已读
     *
     * @param notificationId 通知ID
     * @param userId       用户ID（防止操作他人的通知）
     */
    void markAsRead(
            @Param("notificationId") Long notificationId,
            @Param("userId") Long userId
    );

    /**
     * 全部标记已读
     *
     * @param userId 用户ID
     */
    void markAllAsRead(@Param("userId") Long userId);
}
