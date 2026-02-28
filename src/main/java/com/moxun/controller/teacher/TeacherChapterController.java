package com.moxun.controller.teacher;

import com.moxun.Pojo.Dto.ChapterSaveDTO;
import com.moxun.Pojo.Entity.Chapter;
import com.moxun.Pojo.Vo.ChapterVO;
import com.moxun.service.teacher.TeacherChapterService;
import com.moxun.util.Result;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 教师端 - 章节管理接口
 *
 * 路径前缀：/teacher/chapters
 * 权限：TEACHER 或 ADMIN
 * 说明：仅能操作自己课程下的章节
 */
@Slf4j
@RestController
@RequestMapping("/teacher/chapters")
@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
public class TeacherChapterController {

    @Autowired
    private TeacherChapterService teacherChapterService;

    /**
     * 获取课程的章节列表（含课时）
     * GET /teacher/chapters/course/{courseId}
     *
     * @param courseId 课程ID
     */
    @GetMapping("/course/{courseId}")
    public Result<List<ChapterVO>> listChaptersByCourse(@PathVariable Long courseId) {
        List<ChapterVO> list = teacherChapterService.listChaptersByCourse(courseId);
        return Result.success(list);
    }

    /**
     * 获取章节详情
     * GET /teacher/chapters/{id}
     *
     * @param id 章节ID
     */
    @GetMapping("/{id}")
    public Result<Chapter> getChapterById(@PathVariable Long id) {
        Chapter chapter = teacherChapterService.getChapterById(id);
        return Result.success(chapter);
    }

    /**
     * 新增章节
     * POST /teacher/chapters
     *
     * @param dto 章节信息（courseId, title, sort）
     */
    @PostMapping
    public Result<String> addChapter(@Valid @RequestBody ChapterSaveDTO dto) {
        teacherChapterService.addChapter(dto);
        return Result.success("新增成功");
    }

    /**
     * 编辑章节
     * PUT /teacher/chapters/{id}
     *
     * @param id  章节ID
     * @param dto 章节信息
     */
    @PutMapping("/{id}")
    public Result<String> updateChapter(@PathVariable Long id, @RequestBody ChapterSaveDTO dto) {
        dto.setId(id);
        teacherChapterService.updateChapter(dto);
        return Result.success("编辑成功");
    }

    /**
     * 删除章节
     * DELETE /teacher/chapters/{id}
     * 需先删除其下课时
     *
     * @param id 章节ID
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteChapter(@PathVariable Long id) {
        teacherChapterService.deleteChapter(id);
        return Result.success("删除成功");
    }
}
