package com.moxun.service.teacher.impl;

import com.moxun.Pojo.Dto.CourseCreateDTO;
import com.moxun.Pojo.Dto.CourseUpdateDTO;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.service.teacher.TeacherCourseService;
import org.springframework.stereotype.Service;

/**
 * 教师端 - 课程服务实现（占位）
 * 需从 UserContext 获取当前教师ID，仅操作 teacher_id=当前用户的课程
 */
@Service
public class TeacherCourseServiceImpl implements TeacherCourseService {

    @Override
    public PageResult listMyCourses(String title, Integer page, Integer pageSize) {
        // TODO: 实现我的课程列表（teacher_id = 当前用户）
        return new PageResult(0L, new java.util.ArrayList<>());
    }

    @Override
    public CourseDetailVO getMyCourseById(Long courseId) {
        // TODO: 实现课程详情（需校验 teacher_id）
        return null;
    }

    @Override
    public void addCourse(CourseCreateDTO dto) {
        // TODO: 新增课程，teacher_id 从当前用户获取
    }

    @Override
    public void updateCourse(CourseUpdateDTO dto) {
        // TODO: 编辑课程（需校验 teacher_id）
    }
}
