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


@Slf4j
@RequestMapping("/admin/courseCategories")
@RestController
public class AdminCourseCategoryController {

    @Autowired
    private AdminCourseCategoryService adminCourseCategoryService;

    /**
     * 获取所有分类
     *
     * @return
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
     * 添加分类
     *
     * @return
     */
    @GetMapping("/category/add")
    public Result addCategory(@RequestBody CourseCategoryDTO courseCategoryDTO) {
        log.info("添加分类:{}", courseCategoryDTO);
        adminCourseCategoryService.addCategory(courseCategoryDTO);
        return Result.success();
    }

    /**
     * 修改分类
     *
     * @return
     */
    @PostMapping("/category/update")
    public Result updateCategory(@RequestBody CourseCategoryDTO courseCategoryDTO) {
        log.info("修改分类:{}", courseCategoryDTO);
        adminCourseCategoryService.updateCategory(courseCategoryDTO);
        return Result.success();
    }

    /**
     * 删除分类
     *
     * @return
     */
    @GetMapping("/category/delete/{parentId}")
    public Result deleteCategory(@RequestParam Integer parentId) {
        log.info("删除分类:{}", parentId);
        adminCourseCategoryService.deleteCategory(parentId);
        return Result.success();
    }
}
