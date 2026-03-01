package com.moxun.mapper.admin;

import com.github.pagehelper.Page;
import com.moxun.Pojo.Entity.Course;
import com.moxun.Pojo.Vo.CourseDetailVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminCourseMapper{
    Page<Course> listCourses(String title, Long categoryId, String status);

    CourseDetailVO getCourseById(Long id);
}
