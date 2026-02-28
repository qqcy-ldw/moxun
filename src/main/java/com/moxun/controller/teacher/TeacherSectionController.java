package com.moxun.controller.teacher;

import com.moxun.Pojo.Dto.SectionSaveDTO;
import com.moxun.Pojo.Entity.Section;
import com.moxun.Pojo.Vo.SectionVO;
import com.moxun.service.teacher.TeacherSectionService;
import com.moxun.util.Result;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 教师端 - 课时管理接口
 *
 * 路径前缀：/teacher/sections
 * 权限：TEACHER 或 ADMIN
 * 说明：仅能操作自己课程下的课时
 */
@Slf4j
@RestController
@RequestMapping("/teacher/sections")
@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
public class TeacherSectionController {

    @Autowired
    private TeacherSectionService teacherSectionService;

    /**
     * 获取章节下的课时列表
     * GET /teacher/sections/chapter/{chapterId}
     *
     * @param chapterId 章节ID
     */
    @GetMapping("/chapter/{chapterId}")
    public Result<List<SectionVO>> listSectionsByChapter(@PathVariable Long chapterId) {
        List<SectionVO> list = teacherSectionService.listSectionsByChapter(chapterId);
        return Result.success(list);
    }

    /**
     * 获取课时详情
     * GET /teacher/sections/{id}
     *
     * @param id 课时ID
     */
    @GetMapping("/{id}")
    public Result<Section> getSectionById(@PathVariable Long id) {
        Section section = teacherSectionService.getSectionById(id);
        return Result.success(section);
    }

    /**
     * 新增课时
     * POST /teacher/sections
     *
     * @param dto 课时信息（chapterId, title, videoUrl, duration, sort）
     */
    @PostMapping
    public Result<String> addSection(@Valid @RequestBody SectionSaveDTO dto) {
        teacherSectionService.addSection(dto);
        return Result.success("新增成功");
    }

    /**
     * 编辑课时
     * PUT /teacher/sections/{id}
     *
     * @param id  课时ID
     * @param dto 课时信息
     */
    @PutMapping("/{id}")
    public Result<String> updateSection(@PathVariable Long id, @RequestBody SectionSaveDTO dto) {
        dto.setId(id);
        teacherSectionService.updateSection(dto);
        return Result.success("编辑成功");
    }

    /**
     * 删除课时
     * DELETE /teacher/sections/{id}
     *
     * @param id 课时ID
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteSection(@PathVariable Long id) {
        teacherSectionService.deleteSection(id);
        return Result.success("删除成功");
    }
}
