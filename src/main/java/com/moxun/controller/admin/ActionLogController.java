package com.moxun.controller.admin;

import com.moxun.Enum.ActionType;
import com.moxun.Pojo.Dto.ActionLogDto;
import com.moxun.Pojo.Entity.UserActionLog;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.service.admin.IActionLogService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员 - 操作日志接口
 */
@Slf4j
@RestController
@RequestMapping("/admin/action-log")
public class ActionLogController {

    @Autowired
    private IActionLogService actionLogService;

    /**
     * 分页查询操作日志列表
     * POST /admin/action-log/list
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/list")
    public Result<PageResult> getLogPage(@RequestBody @Validated ActionLogDto dto) {
        PageResult pageResult = actionLogService.getLogPage(dto);
        return Result.success(pageResult);
    }

    /**
     * 获取日志详情
     * GET /admin/action-log/{id}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Result<UserActionLog> getLogDetail(@PathVariable Long id) {
        UserActionLog log = actionLogService.getLogById(id);
        if (log == null) {
            return Result.error("日志记录不存在");
        }
        return Result.success(log);
    }

    /**
     * 获取用户最近操作记录
     * GET /admin/action-log/user/{userId}
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public Result<List<UserActionLog>> getUserRecentLogs(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "10") Integer limit) {
        List<UserActionLog> list = actionLogService.getRecentByUserId(userId, limit);
        return Result.success(list);
    }

    /**
     * 获取所有操作类型
     * GET /admin/action-log/types
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/types")
    public Result<List<Map<String, String>>> getActionTypes() {
        ActionType[] types = ActionType.values();
        List<Map<String, String>> result = Arrays.stream(types)
                .map(type -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("code", type.getCode());
                    map.put("description", type.getDescription());
                    return map;
                })
                .toList();
        return Result.success(result);
    }

    /**
     * 清理过期日志
     * DELETE /admin/action-log/clean
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/clean")
    public Result<String> cleanOldLogs(@RequestParam(defaultValue = "30") Integer days) {
        int count = actionLogService.cleanOldLogs(days);
        return Result.success("成功清理 " + count + " 条过期日志");
    }
}
