package com.moxun.controller.admin;


import com.moxun.Pojo.Dto.CourseCategoryDTO;
import com.moxun.Pojo.Vo.CourseCategoryVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.service.admin.AdminCourseCategoryService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员 - 课程分类管理接口
 *
 * 路径前缀：/admin/courseCategories
 * 权限：system:courseCategory:view 等
 */
@Slf4j
@RequestMapping("/admin/courseCategories")
@RestController
public class AdminCourseCategoryController {

    @Autowired
    private AdminCourseCategoryService adminCourseCategoryService;

    /**
     * 分页获取课程分类列表
     * GET /admin/courseCategories/list
     *
     * @param page     页码
     * @param pageSize 每页条数
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('system:courseCategory:view')")
    @GetMapping("/list")
    public Result<List<CourseCategoryVO>> listCategories(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        List<CourseCategoryVO> courseCategoryVO = adminCourseCategoryService.listCategories(page,pageSize);
        log.info("获取所有分类:{}", courseCategoryVO);
        return Result.success(courseCategoryVO);
    }

    /**
     * 获取子分类
     *
     * @param parentId
     * @return
     */
    @GetMapping("/children/{parentId}")
    public Result listChildren(@PathVariable Integer parentId) {
        log.info("获取子分类:{}", parentId);
        List<CourseCategoryVO> courseCategoryVO = adminCourseCategoryService.listChildren(parentId);
        return Result.success(courseCategoryVO);
    }

    /**
     * 新增课程分类
     * POST /admin/courseCategories/category/add
     *
     * @param courseCategoryDTO 分类信息
     */
    @PostMapping("/category/add")
    public Result addCategory(@RequestBody CourseCategoryDTO courseCategoryDTO) {
        log.info("添加分类:{}", courseCategoryDTO);
        adminCourseCategoryService.addCategory(courseCategoryDTO);
        return Result.success();
    }

    /**
     * 修改课程分类
     * POST /admin/courseCategories/category/update
     *
     * @param courseCategoryDTO 分类信息
     */
    @PostMapping("/category/update")
    public Result updateCategory(@RequestBody CourseCategoryDTO courseCategoryDTO) {
        log.info("修改分类:{}", courseCategoryDTO);
        adminCourseCategoryService.updateCategory(courseCategoryDTO);
        return Result.success();
    }

    /**
     * 删除课程分类
     * GET /admin/courseCategories/category/delete/{parentId}
     *
     * @param parentId 分类ID
     */
    @GetMapping("/category/delete/{parentId}")
    public Result deleteCategory(@RequestParam Integer parentId) {
        log.info("删除分类:{}", parentId);
        adminCourseCategoryService.deleteCategory(parentId);
        return Result.success();
    }
}
