package com.moxun.controller.student;

import com.github.pagehelper.PageInfo;
import com.moxun.Enum.ActionType;
import com.moxun.Pojo.Entity.Notification;
import com.moxun.annotation.UserAction;
import com.moxun.service.student.StudentNotificationService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 学生端 - 通知相关接口
 * 路径前缀：/student/notifications
 */
@Slf4j
@RestController
@RequestMapping("/student/notifications")
@PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
public class StudentNotificationController {

    @Autowired
    private StudentNotificationService studentNotificationService;

    /**
     * 我的通知列表
     */
    @GetMapping
    @UserAction(actionType = ActionType.OTHER, description = "查询通知列表")
    public Result<PageInfo<Notification>> listMyNotifications(
            @RequestParam(required = false) Integer isRead,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageInfo<Notification> pageInfo = studentNotificationService.listMyNotifications(isRead, page, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 未读数量
     */
    @GetMapping("/unread-count")
    public Result<Map<String, Long>> countUnread() {
        long count = studentNotificationService.countUnread();
        return Result.success(Map.of("count", count));
    }

    /**
     * 标记已读
     */
    @PutMapping("/{id}/read")
    @UserAction(actionType = ActionType.OTHER, description = "标记通知已读")
    public Result<String> markAsRead(@PathVariable Long id) {
        studentNotificationService.markAsRead(id);
        return Result.success("已标记为已读");
    }

    /**
     * 全部标记已读
     */
    @PutMapping("/read-all")
    @UserAction(actionType = ActionType.OTHER, description = "全部标记已读")
    public Result<String> markAllAsRead() {
        studentNotificationService.markAllAsRead();
        return Result.success("已全部标记为已读");
    }
}
