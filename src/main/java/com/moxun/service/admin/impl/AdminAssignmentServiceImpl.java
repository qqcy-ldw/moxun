package com.moxun.service.admin.impl;

import com.moxun.Pojo.Dto.AssignmentCreateDTO;
import com.moxun.Pojo.Dto.AssignmentGradeDTO;
import com.moxun.Pojo.Dto.AssignmentUpdateDTO;
import com.moxun.Pojo.Vo.AssignmentVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.service.admin.AdminAssignmentService;
import org.springframework.stereotype.Service;

/**
 * 管理员 - 作业管理 Service 实现（占位）
 */
@Service
public class AdminAssignmentServiceImpl implements AdminAssignmentService {

    @Override
    public PageResult listAssignments(Long courseId, Integer page, Integer pageSize) {
        // TODO: 实现作业列表分页
        return new PageResult(0L, new java.util.ArrayList<>());
    }

    @Override
    public AssignmentVO getAssignmentById(Long id) {
        // TODO: 实现作业详情
        return null;
    }

    @Override
    public void addAssignment(AssignmentCreateDTO dto) {
        // TODO: 实现新增作业
    }

    @Override
    public void updateAssignment(AssignmentUpdateDTO dto) {
        // TODO: 实现编辑作业
    }

    @Override
    public void deleteAssignment(Long id) {
        // TODO: 实现删除作业
    }

    @Override
    public PageResult listSubmissions(Long assignmentId, Integer graded, Integer page, Integer pageSize) {
        // TODO: 实现提交列表分页
        return new PageResult(0L, new java.util.ArrayList<>());
    }

    @Override
    public void gradeAssignment(AssignmentGradeDTO dto) {
        // TODO: 实现批改作业
    }
}
