package com.moxun.service.admin.impl;

import com.moxun.Pojo.Entity.CourseComment;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.service.admin.AdminCommentService;
import org.springframework.stereotype.Service;

/**
 * 管理员 - 课程评论管理 Service 实现（占位）
 */
@Service
public class AdminCommentServiceImpl implements AdminCommentService {

    @Override
    public PageResult listComments(Long courseId, Integer page, Integer pageSize) {
        // TODO: 实现评论列表分页
        return new PageResult(0L, new java.util.ArrayList<>());
    }

    @Override
    public CourseComment getCommentById(Long id) {
        // TODO: 实现评论详情
        return null;
    }

    @Override
    public void deleteComment(Long id) {
        // TODO: 实现删除评论
    }
}
