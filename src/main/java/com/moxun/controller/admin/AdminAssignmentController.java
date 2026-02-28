package com.moxun.controller.admin;

import com.moxun.Pojo.Dto.AssignmentCreateDTO;
import com.moxun.Pojo.Dto.AssignmentGradeDTO;
import com.moxun.Pojo.Dto.AssignmentUpdateDTO;
import com.moxun.Pojo.Vo.AssignmentVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.service.admin.AdminAssignmentService;
import com.moxun.util.Result;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员 - 作业管理接口
 *
 * 路径前缀：/admin/assignments
 * 权限：course:view / add / edit / delete
 */
@Slf4j
@RestController
@RequestMapping("/admin/assignments")
public class AdminAssignmentController {

    @Autowired
    private AdminAssignmentService adminAssignmentService;

    /**
     * 分页查询作业列表
     * GET /admin/assignments
     *
     * @param courseId 课程ID（可选，筛选某课程）
     * @param page     页码
     * @param pageSize 每页条数
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:view')")
    @GetMapping
    public Result<PageResult> listAssignments(
            @RequestParam(required = false) Long courseId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult result = adminAssignmentService.listAssignments(courseId, page, pageSize);
        return Result.success(result);
    }

    /**
     * 获取作业详情
     * GET /admin/assignments/{id}
     *
     * @param id 作业ID
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:view')")
    @GetMapping("/{id}")
    public Result<AssignmentVO> getAssignmentById(@PathVariable Long id) {
        AssignmentVO vo = adminAssignmentService.getAssignmentById(id);
        return Result.success(vo);
    }

    /**
     * 新增作业
     * POST /admin/assignments
     *
     * @param dto 作业信息（courseId, title, description, deadline 等）
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:add')")
    @PostMapping
    public Result<String> addAssignment(@Valid @RequestBody AssignmentCreateDTO dto) {
        adminAssignmentService.addAssignment(dto);
        return Result.success("新增成功");
    }

    /**
     * 编辑作业
     * PUT /admin/assignments/{id}
     *
     * @param id  作业ID
     * @param dto 作业信息
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:edit')")
    @PutMapping("/{id}")
    public Result<String> updateAssignment(@PathVariable Long id, @RequestBody AssignmentUpdateDTO dto) {
        dto.setId(id);
        adminAssignmentService.updateAssignment(dto);
        return Result.success("编辑成功");
    }

    /**
     * 删除作业
     * DELETE /admin/assignments/{id}
     *
     * @param id 作业ID
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:delete')")
    @DeleteMapping("/{id}")
    public Result<String> deleteAssignment(@PathVariable Long id) {
        adminAssignmentService.deleteAssignment(id);
        return Result.success("删除成功");
    }

    /**
     * 分页查询某作业的提交列表
     * GET /admin/assignments/{assignmentId}/submissions
     *
     * @param assignmentId 作业ID
     * @param graded       0-待批改 1-已批改 null-全部
     * @param page         页码
     * @param pageSize     每页条数
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:view')")
    @GetMapping("/{assignmentId}/submissions")
    public Result<PageResult> listSubmissions(
            @PathVariable Long assignmentId,
            @RequestParam(required = false) Integer graded,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult result = adminAssignmentService.listSubmissions(assignmentId, graded, page, pageSize);
        return Result.success(result);
    }

    /**
     * 批改作业
     * POST /admin/assignments/grade
     *
     * @param dto 批改信息（submissionId, score, feedback）
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('course:edit')")
    @PostMapping("/grade")
    public Result<String> gradeAssignment(@Valid @RequestBody AssignmentGradeDTO dto) {
        adminAssignmentService.gradeAssignment(dto);
        return Result.success("批改成功");
    }
}
