package com.moxun.controller.student;

import com.moxun.Pojo.Vo.CourseCategoryVO;
import com.moxun.service.admin.AdminCourseCategoryService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生端 - 课程分类接口
 * 用于课程列表页的分类筛选（只读）
 */
@Slf4j
@RestController
@RequestMapping("/student/categories")
@PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
public class StudentCourseCategoryController {

    @Autowired
    private AdminCourseCategoryService adminCourseCategoryService;

    /**
     * 获取课程分类树（用于筛选下拉）
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
     */
    @GetMapping("/children/{parentId}")
    public Result<List<CourseCategoryVO>> listChildren(@PathVariable Integer parentId) {
        List<CourseCategoryVO> list = adminCourseCategoryService.listChildren(parentId);
        return Result.success(list);
    }
}
