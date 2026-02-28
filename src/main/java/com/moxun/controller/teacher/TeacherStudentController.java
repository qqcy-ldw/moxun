package com.moxun.controller.teacher;

import com.moxun.Pojo.Vo.PageResult;
import com.moxun.service.teacher.TeacherStudentService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 教师端 - 学生管理接口
 *
 * 路径前缀：/teacher/students
 * 权限：TEACHER 或 ADMIN
 * 说明：仅能查看自己课程下的选课学生
 */
@Slf4j
@RestController
@RequestMapping("/teacher/students")
@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
public class TeacherStudentController {

    @Autowired
    private TeacherStudentService teacherStudentService;

    /**
     * 分页查询某课程下的选课学生
     * GET /teacher/students/course/{courseId}
     *
     * @param courseId 课程ID
     * @param username 用户名（模糊查询，可选）
     * @param page     页码
     * @param pageSize 每页条数
     */
    @GetMapping("/course/{courseId}")
    public Result<PageResult> listMyCourseStudents(
            @PathVariable Long courseId,
            @RequestParam(required = false) String username,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult result = teacherStudentService.listMyCourseStudents(courseId, username, page, pageSize);
        return Result.success(result);
    }
}
