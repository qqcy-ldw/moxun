package com.moxun.service.admin;

import com.moxun.Pojo.Dto.CourseCreateDTO;
import com.moxun.Pojo.Dto.CourseUpdateDTO;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.CourseListItemVO;
import com.moxun.Pojo.Vo.PageResult;

/**
 * 课程管理 Service 接口
 * 请自行实现 AdminCourseServiceImpl
 */
public interface AdminCourseService {

    /**
     * 分页查询课程列表
     *
     * @param title    课程标题（模糊）
     * @param categoryId 分类ID
     * @param status   状态 draft/published/offline
     */
    PageResult listCourses(String title, Long categoryId, String status, Integer page, Integer pageSize);

    /**
     * 根据ID获取课程详情
     */
    CourseDetailVO getCourseById(Long id);

    /**
     * 新增课程
     */
    void addCourse(CourseCreateDTO dto);

    /**
     * 编辑课程
     */
    void updateCourse(CourseUpdateDTO dto);

    /**
     * 删除课程
     */
    void deleteCourse(Long id);
}
