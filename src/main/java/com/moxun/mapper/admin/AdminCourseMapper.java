package com.moxun.mapper.admin;

import com.github.pagehelper.Page;
import com.moxun.Pojo.Entity.Course;
import com.moxun.Pojo.Entity.User;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.CourseListItemVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminCourseMapper{
    Page<CourseListItemVO> listCourses(String title, Long categoryId, String status);

    CourseDetailVO getCourseById(Long id);

    List<String> getTeacher();

    int addCourse(Course course);

    int updateCourse(Course course);

    List<Integer> getChapter(Long id);

    @Delete("DELETE FROM courses WHERE id = #{id}")
    int deleteCourse(Long id);
}
