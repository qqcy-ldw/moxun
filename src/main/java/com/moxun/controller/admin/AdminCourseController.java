package com.moxun.controller.admin;

import com.moxun.Enum.ActionType;
import com.moxun.Pojo.Dto.CourseCreateDTO;
import com.moxun.Pojo.Dto.CourseUpdateDTO;
import com.moxun.Pojo.Entity.User;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.annotation.UserAction;
import com.moxun.service.admin.AdminCourseService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员 - 课程管理接口
 *
 * 权限对应 sys_menu 表：course:view / add / edit / delete
 */
@Slf4j
@RestController
@RequestMapping("/admin/courses")
public class AdminCourseController {

    @Autowired
    private AdminCourseService adminCourseService;

    /**
     * 分页查询课程列表
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:view')")
    @GetMapping
    @UserAction(actionType = ActionType.OTHER, description = "查询课程列表")
    public Result<PageResult> listCourses(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult result = adminCourseService.listCourses(title, categoryId, status, page, pageSize);
        return Result.success(result);
    }

    /**
     * 根据ID获取课程详情（含章节、课时）
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:view')")
    @GetMapping("/{id}")
    @UserAction(actionType = ActionType.OTHER, description = "查询课程详情")
    public Result<CourseDetailVO> getCourseById(@PathVariable Long id) {
        CourseDetailVO vo = adminCourseService.getCourseById(id);
        return Result.success(vo);
    }

    /**
     * 新增课程
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:add')")
    @PostMapping
    @UserAction(actionType = ActionType.CREATE_COURSE, description = "新增课程", logResult = true)
    public Result<String> addCourse( @RequestBody CourseCreateDTO dto) {
        adminCourseService.addCourse(dto);
        return Result.success("新增成功");
    }

    /**
     * 编辑课程
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:edit')")
    @PutMapping("/{id}")
    @UserAction(actionType = ActionType.UPDATE_COURSE, description = "编辑课程", logResult = true)
    public Result<String> updateCourse(@PathVariable Long id, @RequestBody CourseUpdateDTO dto) {
        dto.setId(id);
        adminCourseService.updateCourse(dto);
        return Result.success("编辑成功");
    }

    /**
     * 删除课程
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:delete')")
    @DeleteMapping("/{id}")
    @UserAction(actionType = ActionType.DELETE_COURSE, description = "删除课程", logResult = true)
    public Result<String> deleteCourse(@PathVariable Long id) {
        adminCourseService.deleteCourse(id);
        return Result.success("删除成功");
    }

    /**
     * 获取讲师列表
     * 为添加课程时选择讲师显示
     */
    @GetMapping("/teacher")
    public Result<List<String>> getTeacher() {
        return Result.success(adminCourseService.getTeacher());
    }
}
