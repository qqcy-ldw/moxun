package com.moxun.service.teacher;

import com.moxun.Pojo.Dto.CourseCreateDTO;
import com.moxun.Pojo.Dto.CourseUpdateDTO;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.CourseListItemVO;
import com.moxun.Pojo.Vo.PageResult;

import java.util.List;

/**
 * 教师端 - 课程服务接口
 * 仅操作当前教师自己的课程（teacher_id = 当前用户）
 */
public interface TeacherCourseService {

    PageResult listMyCourses(String title, Integer page, Integer pageSize);

    CourseDetailVO getMyCourseById(Long courseId);

    void addCourse(CourseCreateDTO dto);

    void updateCourse(CourseUpdateDTO dto);
}
