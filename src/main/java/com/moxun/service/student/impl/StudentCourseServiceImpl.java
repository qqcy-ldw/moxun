package com.moxun.service.student.impl;

import com.moxun.Pojo.Dto.CourseCommentCreateDTO;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.CourseListItemVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.service.student.StudentCourseService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生端 - 课程服务实现（占位）
 * 请在此类中实现具体业务逻辑
 */
@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    @Override
    public PageResult listCourses(String title, Long categoryId, String level, Integer page, Integer pageSize) {
        // TODO: 实现课程列表分页查询（仅已发布）
        return new PageResult(0L, new ArrayList<>());
    }

    @Override
    public CourseDetailVO getCourseDetail(Long courseId) {
        // TODO: 实现课程详情（含章节、课时）
        return null;
    }

    @Override
    public void joinCourse(Long courseId) {
        // TODO: 实现选课（user_courses 表）
    }

    @Override
    public List<CourseListItemVO> listMyCourses(Integer page, Integer pageSize) {
        // TODO: 实现我的课程列表
        return new ArrayList<>();
    }

    @Override
    public void favoriteCourse(Long courseId) {
        // TODO: 实现收藏（course_favorites 表）
    }

    @Override
    public void unfavoriteCourse(Long courseId) {
        // TODO: 实现取消收藏
    }

    @Override
    public List<CourseListItemVO> listMyFavorites(Integer page, Integer pageSize) {
        // TODO: 实现我的收藏列表
        return new ArrayList<>();
    }

    @Override
    public PageResult listCourseComments(Long courseId, Integer page, Integer pageSize) {
        // TODO: 实现课程评论列表分页
        return new PageResult(0L, new ArrayList<>());
    }

    @Override
    public void createCourseComment(CourseCommentCreateDTO dto) {
        // TODO: 实现发表课程评论（course_comments 表）
    }
}
