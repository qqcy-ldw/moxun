package com.moxun.controller.admin;

import com.moxun.Pojo.Entity.CourseComment;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.service.admin.AdminCommentService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员 - 课程评论管理接口
 *
 * 路径前缀：/admin/comments
 * 权限：course:view / delete
 */
@Slf4j
@RestController
@RequestMapping("/admin/comments")
public class AdminCommentController {

    @Autowired
    private AdminCommentService adminCommentService;

    /**
     * 分页查询课程评论列表
     * GET /admin/comments
     *
     * @param courseId 课程ID（可选，筛选某课程）
     * @param page     页码
     * @param pageSize 每页条数
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:view')")
    @GetMapping
    public Result<PageResult> listComments(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Integer rating,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult result = adminCommentService.listComments(courseId, rating, page, pageSize);
        return Result.success(result);
    }

    /**
     * 获取评论详情
     * GET /admin/comments/{id}
     *
     * @param id 评论ID
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:view')")
    @GetMapping("/{id}")
    public Result<CourseComment> getCommentById(@PathVariable Long id) {
        CourseComment comment = adminCommentService.getCommentById(id);
        return Result.success(comment);
    }

    /**
     * 删除评论
     * DELETE /admin/comments/{id}
     *
     * @param id 评论ID
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:delete')")
    @DeleteMapping("/{id}")
    public Result<String> deleteComment(@PathVariable Long id) {
        adminCommentService.deleteComment(id);
        return Result.success("删除成功");
    }
}
