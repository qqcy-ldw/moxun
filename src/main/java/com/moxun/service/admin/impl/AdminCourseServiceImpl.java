package com.moxun.service.admin.impl;

import com.moxun.Pojo.Dto.CourseCreateDTO;
import com.moxun.Pojo.Dto.CourseUpdateDTO;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.service.admin.AdminCourseService;
import org.springframework.stereotype.Service;

/**
 * 课程管理 Service 实现（占位）
 * 请在此类中实现具体业务逻辑，并注入 AdminCourseMapper
 */
@Service
public class AdminCourseServiceImpl implements AdminCourseService {

    @Override
    public PageResult listCourses(String title, Long categoryId, String status, Integer page, Integer pageSize) {
        // TODO: 实现分页查询，可参考 AdminUserServiceImpl 使用 PageHelper
        return new PageResult(0L, new java.util.ArrayList<>());
    }

    @Override
    public CourseDetailVO getCourseById(Long id) {
        // TODO: 实现课程详情查询（含章节、课时）
        return null;
    }

    @Override
    public void addCourse(CourseCreateDTO dto) {
        // TODO: 实现新增课程（courses 表需 teacher_id，可从当前登录用户获取）
    }

    @Override
    public void updateCourse(CourseUpdateDTO dto) {
        // TODO: 实现编辑课程
    }

    @Override
    public void deleteCourse(Long id) {
        // TODO: 实现删除课程（注意外键关联：chapters、sections 等）
    }
}
