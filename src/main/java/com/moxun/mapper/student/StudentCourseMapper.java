package com.moxun.mapper.student;

import com.moxun.Pojo.Vo.CommentVO;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.CourseListItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 学生端 - 课程 Mapper
 * 提供课程浏览、选课、收藏、评论等数据访问操作
 */
@Mapper
public interface StudentCourseMapper {

    /**
     * 课程列表查询（支持分类、难度筛选）
     * 👑 面试考点：关联查询 course_categories 和 users 表，获取分类名和讲师名
     * 👑 面试考点：使用 CASE WHEN EXISTS 子查询判断当前用户是否已选课/已收藏
     *
     * @param userId     当前用户ID
     * @param title      课程标题（模糊搜索）
     * @param categoryId  分类ID
     * @param level      难度级别
     * @return 课程列表
     */
    List<CourseListItemVO> listCourses(
            @Param("userId") Long userId,
            @Param("title") String title,
            @Param("categoryId") Long categoryId,
            @Param("level") String level
    );

    /**
     * 我的课程列表（已选课）
     *
     * @param userId 当前用户ID
     * @return 我的课程列表
     */
    List<CourseListItemVO> listMyCourses(@Param("userId") Long userId);

    /**
     * 我的收藏列表
     *
     * @param userId 当前用户ID
     * @return 我的收藏课程列表
     */
    List<CourseListItemVO> listMyFavorites(@Param("userId") Long userId);

    /**
     * 课程详情查询（含章节和课时）
     * 👑 面试考点：MyBatis resultMap 嵌套 collection 实现一对多查询
     *
     * @param courseId 课程ID
     * @return 课程详情VO（含章节和课时）
     */
    CourseDetailVO getCourseDetail(@Param("courseId") Long courseId);

    /**
     * 选课
     * 👑 面试考点：ON DUPLICATE KEY UPDATE 实现 upsert（存在则更新，不存在则插入）
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     */
    void joinCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 取消选课
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     */
    void quitCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 收藏课程
     * 👑 面试考点：ON DUPLICATE KEY UPDATE 实现幂等操作，避免重复收藏
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     */
    void favoriteCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 取消收藏
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     */
    void unfavoriteCourse(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 课程评论列表
     *
     * @param courseId 课程ID
     * @return 评论列表
     */
    List<CommentVO> listCourseComments(@Param("courseId") Long courseId);

    /**
     * 添加课程评论
     *
     * @param courseId 课程ID
     * @param userId    用户ID
     * @param content   评论内容
     * @param rating    评分（1-5）
     */
    void createComment(
            @Param("courseId") Long courseId,
            @Param("userId") Long userId,
            @Param("content") String content,
            @Param("rating") Integer rating
    );

    /**
     * 检查用户是否已选课
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 1-已选课，0-未选课
     */
    Integer checkJoined(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 检查用户是否已收藏
     *
     * @param userId   用户ID
     * @param courseId 课程ID
     * @return 1-已收藏，0-未收藏
     */
    Integer checkFavorited(@Param("userId") Long userId, @Param("courseId") Long courseId);

    /**
     * 获取用户已选课程列表（带学习进度）
     * 👑 面试考点：关联 user_courses 和 learning_progress 表
     *
     * @param userId 当前用户ID
     * @return 我的课程列表（含进度百分比）
     */
    List<Map<String, Object>> listMyCoursesWithProgress(@Param("userId") Long userId);
}
