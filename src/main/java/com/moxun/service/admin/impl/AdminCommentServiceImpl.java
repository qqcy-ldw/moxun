package com.moxun.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moxun.Pojo.Entity.CourseComment;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.mapper.admin.AdminCommentMapper;
import com.moxun.service.admin.AdminCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

/**
 * 管理员 - 课程评论管理
 */
@Service
public class AdminCommentServiceImpl implements AdminCommentService {

    @Autowired
    private AdminCommentMapper adminCommentMapper;

    /**
     * 获取课程评论列表
     *
     * @param courseId 课程ID
     * @param rating   评分
     * @param page     页码
     * @param pageSize 页面大小
     * @return 课程评论列表
     */
    @Override
    public PageResult listComments(Long courseId,Integer rating, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Page<CourseComment> courseCommentPage = adminCommentMapper.pageListComments(rating,courseId);
        return new PageResult(courseCommentPage.getTotal(),courseCommentPage.getResult());
    }

    /**
     * 获取课程评论详情
     *
     * @param id 课程评论ID
     * @return 课程评论
     */
    @Override
    public CourseComment getCommentById(Long id) {
        if (id == null) {
            throw new RuntimeException("参数错误");
        }
        CourseComment courseComment = adminCommentMapper.getCommentById(id);
        return courseComment;
    }

    @Override
    public void deleteComment(Long id) {
        if (id == null) {
            throw new RuntimeException("参数错误");
        }
        int i = adminCommentMapper.deleteComment(id);
        if (i != 1) {
            throw new RuntimeException("删除失败");
        }
    }
}
