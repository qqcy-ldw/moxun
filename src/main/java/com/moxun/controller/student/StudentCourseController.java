package com.moxun.controller.student;

import com.github.pagehelper.PageInfo;
import com.moxun.Enum.ActionType;
import com.moxun.Pojo.Dto.CourseCommentCreateDTO;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.CourseListItemVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.annotation.UserAction;
import com.moxun.service.student.StudentCourseService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 学生端 - 课程相关接口
 * 路径前缀：/student/courses
 */
@Slf4j
@RestController
@RequestMapping("/student/courses")
@PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
public class StudentCourseController {

    @Autowired
    private StudentCourseService studentCourseService;

    /**
     * 分页查询课程列表（已发布）
     */
    @GetMapping
    @UserAction(actionType = ActionType.OTHER, description = "查询课程列表")
    public Result<PageResult> listCourses(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String level,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult result = studentCourseService.listCourses(title, categoryId, level, page, pageSize);
        return Result.success(result);
    }

    /**
     * 课程详情（含章节、课时）
     */
    @GetMapping("/{courseId}")
    @UserAction(actionType = ActionType.OTHER, description = "查询课程详情")
    public Result<CourseDetailVO> getCourseDetail(@PathVariable Long courseId) {
        CourseDetailVO vo = studentCourseService.getCourseDetail(courseId);
        return Result.success(vo);
    }

    /**
     * 选课（加入我的课程）
     */
    @PostMapping("/{courseId}/join")
    @UserAction(actionType = ActionType.ENROLL_COURSE, description = "选课", logResult = true)
    public Result<String> joinCourse(@PathVariable Long courseId) {
        studentCourseService.joinCourse(courseId);
        return Result.success("选课成功");
    }

    /**
     * 取消选课
     */
    @DeleteMapping("/{courseId}/join")
    @UserAction(actionType = ActionType.OTHER, description = "取消选课", logResult = true)
    public Result<String> quitCourse(@PathVariable Long courseId) {
        studentCourseService.quitCourse(courseId);
        return Result.success("已取消选课");
    }

    /**
     * 我的课程列表
     */
    @GetMapping("/my")
    @UserAction(actionType = ActionType.OTHER, description = "查询我的课程列表")
    public Result<PageInfo<CourseListItemVO>> listMyCourses(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageInfo<CourseListItemVO> pageInfo = studentCourseService.listMyCourses(page, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 收藏课程
     */
    @PostMapping("/{courseId}/favorite")
    @UserAction(actionType = ActionType.FAVORITE_COURSE, description = "收藏课程", logResult = true)
    public Result<String> favoriteCourse(@PathVariable Long courseId) {
        studentCourseService.favoriteCourse(courseId);
        return Result.success("收藏成功");
    }

    /**
     * 取消收藏
     */
    @DeleteMapping("/{courseId}/favorite")
    @UserAction(actionType = ActionType.UNFAVORITE_COURSE, description = "取消收藏", logResult = true)
    public Result<String> unfavoriteCourse(@PathVariable Long courseId) {
        studentCourseService.unfavoriteCourse(courseId);
        return Result.success("已取消收藏");
    }

    /**
     * 我的收藏列表
     */
    @GetMapping("/my/favorites")
    @UserAction(actionType = ActionType.OTHER, description = "查询我的收藏列表")
    public Result<PageInfo<CourseListItemVO>> listMyFavorites(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageInfo<CourseListItemVO> pageInfo = studentCourseService.listMyFavorites(page, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 课程评论列表
     */
    @GetMapping("/{courseId}/comments")
    @UserAction(actionType = ActionType.OTHER, description = "查询课程评论")
    public Result<PageResult> listCourseComments(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult result = studentCourseService.listCourseComments(courseId, page, pageSize);
        return Result.success(result);
    }

    /**
     * 发表课程评论
     */
    @PostMapping("/{courseId}/comments")
    @UserAction(actionType = ActionType.CREATE_COMMENT, description = "发表课程评论", logResult = true)
    public Result<String> createCourseComment(
            @PathVariable Long courseId,
            @Valid @RequestBody CourseCommentCreateDTO dto
    ) {
        dto.setCourseId(courseId);
        studentCourseService.createCourseComment(dto);
        return Result.success("评论成功");
    }

    /**
     * 我的课程列表（带学习进度）
     * 👑 面试考点：返回进度百分比
     */
    @GetMapping("/my/progress")
    @UserAction(actionType = ActionType.OTHER, description = "查询我的课程（含学习进度）")
    public Result<List<Map<String, Object>>> listMyCoursesWithProgress(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        List<Map<String, Object>> list = studentCourseService.listMyCoursesWithProgress(page, pageSize);
        return Result.success(list);
    }
}
