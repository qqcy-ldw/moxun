package com.moxun.service.student.impl;

import com.moxun.Pojo.Dto.AssignmentSubmitDTO;
import com.moxun.Pojo.Vo.AssignmentSubmissionVO;
import com.moxun.Pojo.Vo.AssignmentVO;
import com.moxun.service.student.StudentAssignmentService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 学生端 - 作业服务实现（占位）
 * 请在此类中实现具体业务逻辑
 */
@Service
public class StudentAssignmentServiceImpl implements StudentAssignmentService {

    @Override
    public List<AssignmentVO> listAssignmentsByCourse(Long courseId) {
        // TODO: 实现课程作业列表
        return new ArrayList<>();
    }

    @Override
    public AssignmentVO getAssignmentById(Long assignmentId) {
        // TODO: 实现作业详情
        return null;
    }

    @Override
    public void submitAssignment(AssignmentSubmitDTO dto) {
        // TODO: 实现提交作业（assignment_submissions 表）
    }

    @Override
    public AssignmentSubmissionVO getMySubmission(Long assignmentId) {
        // TODO: 实现我的提交记录
        return null;
    }

    @Override
    public List<AssignmentSubmissionVO> listMySubmissions(Long courseId, Integer page, Integer pageSize) {
        // TODO: 实现我的所有提交
        return new ArrayList<>();
    }
}
