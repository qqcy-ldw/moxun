package com.moxun.controller.admin;

import com.moxun.Pojo.Dto.CourseCreateDTO;
import com.moxun.Pojo.Dto.CourseUpdateDTO;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.service.admin.AdminCourseService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public Result<CourseDetailVO> getCourseById(@PathVariable Long id) {
        CourseDetailVO vo = adminCourseService.getCourseById(id);
        return Result.success(vo);
    }

    /**
     * 新增课程
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:add')")
    @PostMapping
    public Result<String> addCourse(@RequestBody CourseCreateDTO dto) {
        adminCourseService.addCourse(dto);
        return Result.success("新增成功");
    }

    /**
     * 编辑课程
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:edit')")
    @PutMapping("/{id}")
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
    public Result<String> deleteCourse(@PathVariable Long id) {
        adminCourseService.deleteCourse(id);
        return Result.success("删除成功");
    }
}
