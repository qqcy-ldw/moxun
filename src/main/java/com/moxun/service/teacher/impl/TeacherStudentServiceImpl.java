package com.moxun.service.teacher.impl;

import com.moxun.Pojo.Vo.PageResult;
import com.moxun.service.teacher.TeacherStudentService;
import org.springframework.stereotype.Service;

/**
 * 教师端 - 学生服务实现（占位）
 * 查询选了自己课程的学生（user_courses 关联）
 */
@Service
public class TeacherStudentServiceImpl implements TeacherStudentService {

    @Override
    public PageResult listMyCourseStudents(Long courseId, String username, Integer page, Integer pageSize) {
        // TODO: 查询 user_courses 中 course_id 对应的学生，需校验 course.teacher_id
        return new PageResult(0L, new java.util.ArrayList<>());
    }
}
