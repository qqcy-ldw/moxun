package com.moxun.service.admin;

import com.moxun.Pojo.Entity.CourseComment;
import com.moxun.Pojo.Vo.PageResult;

/**
 * 管理员 - 课程评论管理 Service 接口
 */
public interface AdminCommentService {

    PageResult listComments(Long courseId,Integer rating, Integer page, Integer pageSize);

    CourseComment getCommentById(Long id);

    void deleteComment(Long id);
}
