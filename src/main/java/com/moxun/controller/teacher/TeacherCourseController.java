package com.moxun.controller.teacher;

import com.moxun.Pojo.Dto.CourseCreateDTO;
import com.moxun.Pojo.Dto.CourseUpdateDTO;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.service.teacher.TeacherCourseService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 教师端 - 课程管理接口
 *
 * 路径前缀：/teacher/courses
 * 权限：TEACHER 或 ADMIN
 * 说明：教师仅能操作自己创建的课程（teacher_id = 当前用户）
 */
@Slf4j
@RestController
@RequestMapping("/teacher/courses")
@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
public class TeacherCourseController {

    @Autowired
    private TeacherCourseService teacherCourseService;

    /**
     * 分页查询我的课程列表
     * GET /teacher/courses
     *
     * @param title    课程标题（模糊查询）
     * @param page     页码
     * @param pageSize 每页条数
     */
    @GetMapping
    public Result<PageResult> listMyCourses(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult result = teacherCourseService.listMyCourses(title, page, pageSize);
        return Result.success(result);
    }

    /**
     * 获取课程详情（含章节、课时）
     * GET /teacher/courses/{id}
     *
     * @param id 课程ID
     */
    @GetMapping("/{id}")
    public Result<CourseDetailVO> getMyCourseById(@PathVariable Long id) {
        CourseDetailVO vo = teacherCourseService.getMyCourseById(id);
        return Result.success(vo);
    }

    /**
     * 新增课程
     * POST /teacher/courses
     * teacher_id 由系统从当前登录用户自动填充
     *
     * @param dto 课程信息
     */
    @PostMapping
    public Result<String> addCourse(@RequestBody CourseCreateDTO dto) {
        teacherCourseService.addCourse(dto);
        return Result.success("新增成功");
    }

    /**
     * 编辑课程
     * PUT /teacher/courses/{id}
     * 仅能编辑自己创建的课程
     *
     * @param id  课程ID
     * @param dto 课程信息
     */
    @PutMapping("/{id}")
    public Result<String> updateCourse(@PathVariable Long id, @RequestBody CourseUpdateDTO dto) {
        dto.setId(id);
        teacherCourseService.updateCourse(dto);
        return Result.success("编辑成功");
    }
}
