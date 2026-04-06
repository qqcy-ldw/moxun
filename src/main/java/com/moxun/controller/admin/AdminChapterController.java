package com.moxun.controller.admin;

import com.moxun.Enum.ActionType;
import com.moxun.Pojo.Dto.ChapterSaveDTO;
import com.moxun.Pojo.Entity.Chapter;
import com.moxun.Pojo.Vo.ChapterVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.annotation.UserAction;
import com.moxun.service.admin.AdminChapterService;
import com.moxun.util.Result;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员 - 章节管理接口
 *
 * 路径前缀：/admin/chapters
 * 权限：course:view / add / edit / delete
 */
@Slf4j
@RestController
@RequestMapping("/admin/chapters")
public class AdminChapterController {

    @Autowired
    private AdminChapterService adminChapterService;

//    /**
//     * 获取章节列表
//     * GET /admin/chapters
//     */
//    @PreAuthorize("hasRole('ADMIN') and hasAuthority('course:view')")
//    @GetMapping
//    public Result<PageResult<Chapter>> listChapters(
//            @RequestParam(defaultValue = "1") Integer pageNum,
//            @RequestParam(defaultValue = "10") Integer pageSize
//    ) {
//        PageResult<Chapter> list = adminChapterService.listChapters();
//        return Result.success(list);
//    }

    /**
     * 获取课程的章节列表（含课时）
     * GET /admin/chapters/course/{courseId}
     *
     * @param courseId 课程ID
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:view')")
    @GetMapping("/course/{courseId}")
    @UserAction(actionType = ActionType.OTHER, description = "查询课程章节列表")
    public Result<List<ChapterVO>> listChaptersByCourse(@PathVariable Long courseId) {
        List<ChapterVO> list = adminChapterService.listChaptersByCourse(courseId);
        return Result.success(list);
    }

    /**
     * 获取章节详情
     * GET /admin/chapters/{id}
     *
     * @param id 章节ID
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:view')")
    @GetMapping("/{id}")
    @UserAction(actionType = ActionType.OTHER, description = "查询章节详情")
    public Result<Chapter> getChapterById(@PathVariable Long id) {
        Chapter chapter = adminChapterService.getChapterById(id);
        return Result.success(chapter);
    }

    /**
     * 新增章节
     * POST /admin/chapters
     *
     * @param dto 章节信息（courseId, title, sort）
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:add')")
    @PostMapping
    @UserAction(actionType = ActionType.CREATE_CHAPTER, description = "新增章节", logResult = true)
    public Result<String> addChapter(@Valid @RequestBody ChapterSaveDTO dto) {
        adminChapterService.addChapter(dto);
        return Result.success("新增成功");
    }

    /**
     * 编辑章节
     * PUT /admin/chapters/{id}
     *
     * @param id  章节ID
     * @param dto 章节信息
     */
//    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:edit')")
    @PutMapping("/{id}")
    @UserAction(actionType = ActionType.UPDATE_CHAPTER, description = "编辑章节", logResult = true)
    public Result<String> updateChapter(@Valid @PathVariable Long id, @RequestBody ChapterSaveDTO dto) {
        dto.setId(id);
        log.info("控制层dto{}", dto);
        adminChapterService.updateChapter(dto);
        return Result.success("编辑成功");
    }

    /**
     * 删除章节
     * DELETE /admin/chapters/{id}
     *
     * @param id 章节ID
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:delete')")
    @DeleteMapping("/{id}")
    @UserAction(actionType = ActionType.DELETE_CHAPTER, description = "删除章节", logResult = true)
    public Result<String> deleteChapter(@PathVariable Long id) {
        adminChapterService.deleteChapter(id);
        return Result.success("删除成功");
    }
}
