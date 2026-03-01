package com.moxun.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moxun.Pojo.Dto.CourseCreateDTO;
import com.moxun.Pojo.Dto.CourseUpdateDTO;
import com.moxun.Pojo.Entity.Course;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.mapper.admin.AdminCommentMapper;
import com.moxun.mapper.admin.AdminCourseMapper;
import com.moxun.service.admin.AdminCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理员-课程管理
 */
@Service
public class AdminCourseServiceImpl implements AdminCourseService {

    @Autowired
    private AdminCourseMapper adminCourseMapper;

    /**
     * 课程列表
     *
     * @param title     课程名称
     * @param categoryId 分类ID
     * @param status    课程状态
     * @param page      当前页码
     * @param pageSize  每页条数
     * @return 课程列表
     */
    @Override
    public PageResult listCourses(String title, Long categoryId, String status, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Page<Course> coursePage = adminCourseMapper.listCourses(title, categoryId, status);
        return new PageResult(coursePage.getTotal(), coursePage.getResult());
    }

    /**
     * 课程详情
     *
     * @param id 课程ID
     * @return 课程详情
     */
    @Override
    public CourseDetailVO getCourseById(Long id) {
        if (id == null) throw new RuntimeException("参数错误");
        CourseDetailVO courseDetailVO = adminCourseMapper.getCourseById(id);
        return courseDetailVO;
    }

    /**
     * 新增课程
     *
     * @param dto 课程信息
     */
    @Override
    public void addCourse(CourseCreateDTO dto) {
        // TODO: 实现新增课程（courses 表需 teacher_id，可从当前登录用户获取）
        //获取
        Course course = new Course();
        BeanUtils.copyProperties(dto, course);
    }

    /**
     * 编辑课程
     *
     * @param dto 课程信息
     */
    @Override
    public void updateCourse(CourseUpdateDTO dto) {
        // TODO: 实现编辑课程
    }

    @Override
    public void deleteCourse(Long id) {
        // TODO: 实现删除课程（注意外键关联：chapters、sections 等）
    }
}
