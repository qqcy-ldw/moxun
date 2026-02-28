package com.moxun.controller.student;

import com.moxun.Pojo.Dto.AssignmentSubmitDTO;
import com.moxun.Pojo.Vo.AssignmentSubmissionVO;
import com.moxun.Pojo.Vo.AssignmentVO;
import com.moxun.service.student.StudentAssignmentService;
import com.moxun.util.Result;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生端 - 作业相关接口
 * 路径前缀：/student/assignments
 */
@Slf4j
@RestController
@RequestMapping("/student/assignments")
@PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
public class StudentAssignmentController {

    @Autowired
    private StudentAssignmentService studentAssignmentService;

    /**
     * 获取某课程的作业列表
     */
    @GetMapping("/course/{courseId}")
    public Result<List<AssignmentVO>> listAssignmentsByCourse(@PathVariable Long courseId) {
        List<AssignmentVO> list = studentAssignmentService.listAssignmentsByCourse(courseId);
        return Result.success(list);
    }

    /**
     * 作业详情
     */
    @GetMapping("/{assignmentId}")
    public Result<AssignmentVO> getAssignmentById(@PathVariable Long assignmentId) {
        AssignmentVO vo = studentAssignmentService.getAssignmentById(assignmentId);
        return Result.success(vo);
    }

    /**
     * 提交作业
     */
    @PostMapping("/submit")
    public Result<String> submitAssignment(@Valid @RequestBody AssignmentSubmitDTO dto) {
        studentAssignmentService.submitAssignment(dto);
        return Result.success("提交成功");
    }

    /**
     * 我的提交记录（某作业）
     */
    @GetMapping("/{assignmentId}/my-submission")
    public Result<AssignmentSubmissionVO> getMySubmission(@PathVariable Long assignmentId) {
        AssignmentSubmissionVO vo = studentAssignmentService.getMySubmission(assignmentId);
        return Result.success(vo);
    }

    /**
     * 我的所有提交记录
     */
    @GetMapping("/my")
    public Result<List<AssignmentSubmissionVO>> listMySubmissions(
            @RequestParam(required = false) Long courseId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        List<AssignmentSubmissionVO> list = studentAssignmentService.listMySubmissions(courseId, page, pageSize);
        return Result.success(list);
    }
}
