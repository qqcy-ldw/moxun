package com.moxun.service.student.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.moxun.Pojo.Dto.CourseCommentCreateDTO;
import com.moxun.Pojo.Vo.CommentVO;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.CourseListItemVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.mapper.student.StudentCourseMapper;
import com.moxun.service.student.StudentCourseService;
import com.moxun.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 学生端 - 课程服务实现
 *
 * 👑 面试考点：核心功能
 * 1. 课程浏览：支持分类/难度筛选，仅返回已发布课程
 * 2. 选课/取消选课：通过 user_courses 表管理选课关系
 * 3. 收藏/取消收藏：通过 course_favorites 表管理收藏关系
 * 4. 评论：course_comments 表存储评论和评分
 *
 * 👑 面试考点：技术要点
 * - ThreadLocal 存储当前用户上下文
 * - 事务注解保证数据一致性
 * - MyBatis 关联查询实现课程列表
 * - PageHelper 分页插件
 */
@Slf4j
@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    @Autowired
    private StudentCourseMapper studentCourseMapper;

    /**
     * 获取当前登录用户ID
     * 👑 面试考点：ThreadLocal 存储用户上下文，请求结束时必须清理
     */
    private Long getCurrentUserId() {
        Map<String, Object> user = UserContext.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("用户未登录");
        }
        return (Long) user.get("userId");
    }

    /**
     * 课程列表查询（支持分类、难度筛选）
     * 👑 面试考点：PageHelper 分页插件
     */
    @Override
    public PageResult listCourses(String title, Long categoryId, String level, Integer page, Integer pageSize) {
        Long userId = getCurrentUserId();

        // 使用 PageHelper 分页
        PageHelper.startPage(page, pageSize);
        List<CourseListItemVO> list = studentCourseMapper.listCourses(userId, title, categoryId, level);
        PageInfo<CourseListItemVO> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 课程详情
     * 👑 面试考点：关联查询章节和课时，MyBatis resultMap 嵌套 collection
     */
    @Override
    public CourseDetailVO getCourseDetail(Long courseId) {
        Long userId = getCurrentUserId();

        // 查询课程详情
        CourseDetailVO detail = studentCourseMapper.getCourseDetail(courseId);
        if (detail == null) {
            throw new RuntimeException("课程不存在");
        }

        // 设置选课和收藏状态
        detail.setIsJoined(studentCourseMapper.checkJoined(userId, courseId));
        detail.setIsFavorited(studentCourseMapper.checkFavorited(userId, courseId));

        return detail;
    }

    /**
     * 选课（加入我的课程）
     * 👑 面试考点：@Transactional 保证选课操作的原子性
     *             ON DUPLICATE KEY UPDATE 实现幂等（已选课则更新时间）
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void joinCourse(Long courseId) {
        Long userId = getCurrentUserId();

        // 检查课程是否存在
        CourseDetailVO course = studentCourseMapper.getCourseDetail(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        // 选课
        studentCourseMapper.joinCourse(userId, courseId);
        log.info("用户 {} 选课成功，课程ID: {}", userId, courseId);
    }

    /**
     * 取消选课
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void quitCourse(Long courseId) {
        Long userId = getCurrentUserId();
        studentCourseMapper.quitCourse(userId, courseId);
        log.info("用户 {} 取消选课，课程ID: {}", userId, courseId);
    }

    /**
     * 我的课程列表
     */
    @Override
    public PageInfo<CourseListItemVO> listMyCourses(Integer page, Integer pageSize) {
        Long userId = getCurrentUserId();

        PageHelper.startPage(page, pageSize);
        List<CourseListItemVO> list = studentCourseMapper.listMyCourses(userId);

        return new PageInfo<>(list);
    }

    /**
     * 收藏课程
     * 👑 面试考点：ON DUPLICATE KEY UPDATE 保证幂等
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void favoriteCourse(Long courseId) {
        Long userId = getCurrentUserId();

        // 检查课程是否存在
        CourseDetailVO course = studentCourseMapper.getCourseDetail(courseId);
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        studentCourseMapper.favoriteCourse(userId, courseId);
        log.info("用户 {} 收藏课程成功，课程ID: {}", userId, courseId);
    }

    /**
     * 取消收藏
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unfavoriteCourse(Long courseId) {
        Long userId = getCurrentUserId();
        studentCourseMapper.unfavoriteCourse(userId, courseId);
        log.info("用户 {} 取消收藏，课程ID: {}", userId, courseId);
    }

    /**
     * 我的收藏列表
     */
    @Override
    public PageInfo<CourseListItemVO> listMyFavorites(Integer page, Integer pageSize) {
        Long userId = getCurrentUserId();

        PageHelper.startPage(page, pageSize);
        List<CourseListItemVO> list = studentCourseMapper.listMyFavorites(userId);

        return new PageInfo<>(list);
    }

    /**
     * 课程评论列表
     */
    @Override
    public PageResult listCourseComments(Long courseId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<CommentVO> list = studentCourseMapper.listCourseComments(courseId);
        PageInfo<CommentVO> pageInfo = new PageInfo<>(list);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 发表课程评论
     * 👑 面试考点：校验 DTO 使用 @Valid 注解自动校验
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createCourseComment(CourseCommentCreateDTO dto) {
        Long userId = getCurrentUserId();

        // 校验课程是否存在
        CourseDetailVO course = studentCourseMapper.getCourseDetail(dto.getCourseId());
        if (course == null) {
            throw new RuntimeException("课程不存在");
        }

        // 添加评论
        studentCourseMapper.createComment(
                dto.getCourseId(),
                userId,
                dto.getContent(),
                dto.getRating() != null ? dto.getRating() : 5
        );

        log.info("用户 {} 发表课程评论，课程ID: {}, 评分: {}",
                userId, dto.getCourseId(), dto.getRating());
    }

    /**
     * 我的课程列表（带学习进度）
     * 👑 面试考点：关联 learning_progress 表计算进度百分比
     */
    @Override
    public List<Map<String, Object>> listMyCoursesWithProgress(Integer page, Integer pageSize) {
        Long userId = getCurrentUserId();

        PageHelper.startPage(page, pageSize);
        List<Map<String, Object>> list = studentCourseMapper.listMyCoursesWithProgress(userId);

        // 计算进度百分比
        for (Map<String, Object> course : list) {
            Object totalObj = course.get("total_sections");
            Object finishedObj = course.get("finished_sections");
            int total = totalObj != null ? ((Number) totalObj).intValue() : 0;
            int finished = finishedObj != null ? ((Number) finishedObj).intValue() : 0;
            double progress = total > 0 ? (finished * 100.0 / total) : 0;
            course.put("progress", Math.round(progress * 10) / 10.0);
        }

        return list;
    }
}
