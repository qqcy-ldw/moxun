package com.moxun.controller.teacher;

import com.moxun.Enum.ActionType;
import com.moxun.Pojo.Dto.AssignmentCreateDTO;
import com.moxun.Pojo.Dto.AssignmentGradeDTO;
import com.moxun.Pojo.Dto.AssignmentUpdateDTO;
import com.moxun.Pojo.Vo.AssignmentVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.annotation.UserAction;
import com.moxun.service.teacher.TeacherAssignmentService;
import com.moxun.util.Result;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 教师端 - 作业管理接口
 *
 * 路径前缀：/teacher/assignments
 * 权限：TEACHER 或 ADMIN
 * 说明：仅能操作自己课程下的作业及提交
 */
@Slf4j
@RestController
@RequestMapping("/teacher/assignments")
@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
public class TeacherAssignmentController {

    @Autowired
    private TeacherAssignmentService teacherAssignmentService;

    /**
     * 分页查询我的作业列表
     * GET /teacher/assignments
     *
     * @param courseId 课程ID（可选，筛选某课程）
     * @param page     页码
     * @param pageSize 每页条数
     */
    @GetMapping
    @UserAction(actionType = ActionType.OTHER, description = "查询作业列表")
    public Result<PageResult> listMyAssignments(
            @RequestParam(required = false) Long courseId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult result = teacherAssignmentService.listMyAssignments(courseId, page, pageSize);
        return Result.success(result);
    }

    /**
     * 获取作业详情
     * GET /teacher/assignments/{id}
     *
     * @param id 作业ID
     */
    @GetMapping("/{id}")
    @UserAction(actionType = ActionType.OTHER, description = "查询作业详情")
    public Result<AssignmentVO> getAssignmentById(@PathVariable Long id) {
        AssignmentVO vo = teacherAssignmentService.getAssignmentById(id);
        return Result.success(vo);
    }

    /**
     * 新增作业
     * POST /teacher/assignments
     *
     * @param dto 作业信息（courseId, title, description, deadline 等）
     */
    @PostMapping
    @UserAction(actionType = ActionType.CREATE_ASSIGNMENT, description = "新增作业", logResult = true)
    public Result<String> addAssignment(@Valid @RequestBody AssignmentCreateDTO dto) {
        teacherAssignmentService.addAssignment(dto);
        return Result.success("新增成功");
    }

    /**
     * 编辑作业
     * PUT /teacher/assignments/{id}
     *
     * @param id  作业ID
     * @param dto 作业信息
     */
    @PutMapping("/{id}")
    @UserAction(actionType = ActionType.UPDATE_ASSIGNMENT, description = "编辑作业", logResult = true)
    public Result<String> updateAssignment(@PathVariable Long id, @RequestBody AssignmentUpdateDTO dto) {
        dto.setId(id);
        teacherAssignmentService.updateAssignment(dto);
        return Result.success("编辑成功");
    }

    /**
     * 删除作业
     * DELETE /teacher/assignments/{id}
     *
     * @param id 作业ID
     */
    @DeleteMapping("/{id}")
    @UserAction(actionType = ActionType.DELETE_ASSIGNMENT, description = "删除作业", logResult = true)
    public Result<String> deleteAssignment(@PathVariable Long id) {
        teacherAssignmentService.deleteAssignment(id);
        return Result.success("删除成功");
    }

    /**
     * 分页查询作业提交列表
     * GET /teacher/assignments/{assignmentId}/submissions
     *
     * @param assignmentId 作业ID
     * @param graded      是否已批改（0/1，可选）
     * @param page        页码
     * @param pageSize    每页条数
     */
    @GetMapping("/{assignmentId}/submissions")
    @UserAction(actionType = ActionType.OTHER, description = "查询作业提交列表")
    public Result<PageResult> listSubmissions(
            @PathVariable Long assignmentId,
            @RequestParam(required = false) Integer graded,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult result = teacherAssignmentService.listSubmissions(assignmentId, graded, page, pageSize);
        return Result.success(result);
    }

    /**
     * 批改作业
     * POST /teacher/assignments/grade
     *
     * @param dto 批改信息（submissionId, score, feedback）
     */
    @PostMapping("/grade")
    @UserAction(actionType = ActionType.GRADE_ASSIGNMENT, description = "批改作业", logResult = true)
    public Result<String> gradeAssignment(@Valid @RequestBody AssignmentGradeDTO dto) {
        teacherAssignmentService.gradeAssignment(dto);
        return Result.success("批改成功");
    }
}
