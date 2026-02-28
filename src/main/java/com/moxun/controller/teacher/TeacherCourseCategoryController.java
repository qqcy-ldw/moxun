package com.moxun.controller.teacher;

import com.moxun.Pojo.Vo.CourseCategoryVO;
import com.moxun.service.admin.AdminCourseCategoryService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 教师端 - 课程分类接口
 *
 * 路径前缀：/teacher/categories
 * 权限：TEACHER 或 ADMIN
 * 说明：用于新增/编辑课程时选择分类，仅读操作
 */
@Slf4j
@RestController
@RequestMapping("/teacher/categories")
@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
public class TeacherCourseCategoryController {

    @Autowired
    private AdminCourseCategoryService adminCourseCategoryService;

    /**
     * 获取课程分类列表（用于新增/编辑课程时选择分类）
     * GET /teacher/categories
     *
     * @param page     页码
     * @param pageSize 每页条数
     */
    @GetMapping
    public Result<List<CourseCategoryVO>> listCategories(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "100") Integer pageSize
    ) {
        List<CourseCategoryVO> list = adminCourseCategoryService.listCategories(page, pageSize);
        return Result.success(list);
    }

    /**
     * 根据父分类ID获取子分类
     * GET /teacher/categories/children/{parentId}
     *
     * @param parentId 父分类ID
     */
    @GetMapping("/children/{parentId}")
    public Result<List<CourseCategoryVO>> listChildren(@PathVariable Integer parentId) {
        List<CourseCategoryVO> list = adminCourseCategoryService.listChildren(parentId);
        return Result.success(list);
    }
}
