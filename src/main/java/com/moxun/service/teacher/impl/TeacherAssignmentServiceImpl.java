package com.moxun.service.teacher.impl;

import com.moxun.Pojo.Dto.AssignmentCreateDTO;
import com.moxun.Pojo.Dto.AssignmentGradeDTO;
import com.moxun.Pojo.Dto.AssignmentUpdateDTO;
import com.moxun.Pojo.Vo.AssignmentVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.service.teacher.TeacherAssignmentService;
import org.springframework.stereotype.Service;

/**
 * 教师端 - 作业服务实现（占位）
 * 仅操作自己课程的作业
 */
@Service
public class TeacherAssignmentServiceImpl implements TeacherAssignmentService {

    @Override
    public PageResult listMyAssignments(Long courseId, Integer page, Integer pageSize) {
        return new PageResult(0L, new java.util.ArrayList<>());
    }

    @Override
    public AssignmentVO getAssignmentById(Long id) {
        return null;
    }

    @Override
    public void addAssignment(AssignmentCreateDTO dto) {
        // TODO: 需校验 course.teacher_id
    }

    @Override
    public void updateAssignment(AssignmentUpdateDTO dto) {
        // TODO: 需校验 course.teacher_id
    }

    @Override
    public void deleteAssignment(Long id) {
        // TODO: 需校验 course.teacher_id
    }

    @Override
    public PageResult listSubmissions(Long assignmentId, Integer graded, Integer page, Integer pageSize) {
        return new PageResult(0L, new java.util.ArrayList<>());
    }

    @Override
    public void gradeAssignment(AssignmentGradeDTO dto) {
        // TODO: 需校验 submission->assignment->course.teacher_id
    }
}
