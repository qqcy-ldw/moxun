package com.moxun.controller.admin;

import com.moxun.Pojo.Dto.SectionSaveDTO;
import com.moxun.Pojo.Entity.Section;
import com.moxun.Pojo.Vo.SectionVO;
import com.moxun.service.admin.AdminSectionService;
import com.moxun.util.Result;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员 - 课时管理接口
 *
 * 路径前缀：/admin/sections
 * 权限：course:view / add / edit / delete
 */
@Slf4j
@RestController
@RequestMapping("/admin/sections")
public class AdminSectionController {

    @Autowired
    private AdminSectionService adminSectionService;

    /**
     * 获取章节下的课时列表
     * GET /admin/sections/chapter/{chapterId}
     *
     * @param chapterId 章节ID
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:view')")
    @GetMapping("/chapter/{chapterId}")
    public Result<List<SectionVO>> listSectionsByChapter(@PathVariable Long chapterId) {
        List<SectionVO> list = adminSectionService.listSectionsByChapter(chapterId);
        return Result.success(list);
    }

    /**
     * 获取课时详情
     * GET /admin/sections/{id}
     *
     * @param id 课时ID
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:view')")
    @GetMapping("/{id}")
    public Result<Section> getSectionById(@PathVariable Long id) {
        Section section = adminSectionService.getSectionById(id);
        return Result.success(section);
    }

    /**
     * 新增课时
     * POST /admin/sections
     *
     * @param dto 课时信息（chapterId, title, videoUrl, duration, sort）
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:add')")
    @PostMapping
    public Result<String> addSection(@Valid @RequestBody SectionSaveDTO dto) {
        adminSectionService.addSection(dto);
        return Result.success("新增成功");
    }

    /**
     * 编辑课时
     * PUT /admin/sections/{id}
     *
     * @param id  课时ID
     * @param dto 课时信息
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:edit')")
    @PutMapping("/{id}")
    public Result<String> updateSection(@PathVariable Long id, @RequestBody SectionSaveDTO dto) {
        dto.setId(id);
        adminSectionService.updateSection(dto);
        return Result.success("编辑成功");
    }

    /**
     * 删除课时
     * DELETE /admin/sections/{id}
     *
     * @param id 课时ID
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:delete')")
    @DeleteMapping("/{id}")
    public Result<String> deleteSection(@PathVariable Long id) {
        adminSectionService.deleteSection(id);
        return Result.success("删除成功");
    }
}
