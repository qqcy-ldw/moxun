package com.moxun.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moxun.Pojo.Dto.CourseCreateDTO;
import com.moxun.Pojo.Dto.CourseUpdateDTO;
import com.moxun.Pojo.Entity.Course;
import com.moxun.Pojo.Entity.Section;
import com.moxun.Pojo.Entity.User;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.CourseListItemVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.exception.BusinessException;
import com.moxun.mapper.admin.AdminChapterMapper;
import com.moxun.mapper.admin.AdminCommentMapper;
import com.moxun.mapper.admin.AdminCourseMapper;
import com.moxun.service.admin.AdminChapterService;
import com.moxun.service.admin.AdminCourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 管理员-课程管理
 */
@Service
public class AdminCourseServiceImpl implements AdminCourseService {

    @Autowired
    private AdminCourseMapper adminCourseMapper;

    @Autowired
    private AdminChapterMapper adminChapterMapper;

    @Autowired
    private AdminChapterService adminChapterService;

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
        Page<CourseListItemVO> coursePage = adminCourseMapper.listCourses(title, categoryId, status);
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
        if (Objects.isNull(dto.getTeacherId())) throw new RuntimeException("教师ID不能为空");
        if(Objects.isNull(dto.getPrice())) throw new RuntimeException("价格不能为空");
        Course course = new Course();
        BeanUtils.copyProperties(dto, course);
        course.setCreateTime(java.time.LocalDateTime.now());
        course.setUpdateTime(java.time.LocalDateTime.now());
        int count = adminCourseMapper.addCourse(course);
        if (count == 0) throw new RuntimeException("新增课程失败");
    }

    /**
     * 编辑课程
     *
     * @param dto 课程信息
     */
    @Override
    public void updateCourse(CourseUpdateDTO dto) {
        if(Objects.isNull(dto.getPrice())) throw new RuntimeException("价格不能为空");
        Course course = new Course();
        BeanUtils.copyProperties(dto, course);
        course.setUpdateTime(java.time.LocalDateTime.now());
        int count = adminCourseMapper.updateCourse(course);
        if (count == 0) throw new RuntimeException("编辑课程失败");

    }

    /**
     * 删除课程
     *
     * @param id 课程ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCourse(Long id) {
        //获取课程下的所有章节ID
        List<Integer> chapterIds = adminCourseMapper.getChapter(id);
        if (chapterIds != null && !chapterIds.isEmpty()) {
            //删除章节表
            chapterIds.forEach(chapter -> adminChapterService.deleteChapter(Long.valueOf(chapter)));
        }
        //最后删除课程本身
        int deleted = adminCourseMapper.deleteCourse(id);
        if (deleted == 0) {
            // 根据业务，可能课程不存在，可抛异常或记录日志
            throw new BusinessException("删除课程失败，课程不存在或已被删除");
        }
    }

    /**
     * 获取教师信息
     * 为添加课程准备教师信息
     */
    @Override
    public List getTeacher() {
        return adminCourseMapper.getTeacher();
    }
}
