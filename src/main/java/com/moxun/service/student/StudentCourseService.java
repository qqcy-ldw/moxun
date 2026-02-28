package com.moxun.service.student;

import com.moxun.Pojo.Dto.CourseCommentCreateDTO;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.CourseListItemVO;
import com.moxun.Pojo.Vo.PageResult;

import java.util.List;

/**
 * 学生端 - 课程服务接口
 * 请自行实现 StudentCourseServiceImpl
 */
public interface StudentCourseService {

    /**
     * 分页查询课程列表（已发布）
     */
    PageResult listCourses(String title, Long categoryId, String level, Integer page, Integer pageSize);

    /**
     * 课程详情（含章节、课时）
     */
    CourseDetailVO getCourseDetail(Long courseId);

    /**
     * 选课（加入我的课程）
     */
    void joinCourse(Long courseId);

    /**
     * 我的课程列表
     */
    List<CourseListItemVO> listMyCourses(Integer page, Integer pageSize);

    /**
     * 收藏课程
     */
    void favoriteCourse(Long courseId);

    /**
     * 取消收藏
     */
    void unfavoriteCourse(Long courseId);

    /**
     * 我的收藏列表
     */
    List<CourseListItemVO> listMyFavorites(Integer page, Integer pageSize);

    /**
     * 课程评论列表（分页）
     */
    PageResult listCourseComments(Long courseId, Integer page, Integer pageSize);

    /**
     * 发表课程评论
     */
    void createCourseComment(CourseCommentCreateDTO dto);
}
