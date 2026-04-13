package com.moxun.service.student;

import com.github.pagehelper.PageInfo;
import com.moxun.Pojo.Dto.AssignmentSubmitDTO;
import com.moxun.Pojo.Vo.AssignmentSubmissionVO;
import com.moxun.Pojo.Vo.AssignmentVO;

import java.util.List;

/**
 * 学生端 - 作业服务接口
 */
public interface StudentAssignmentService {

    /**
     * 获取某课程的作业列表
     */
    List<AssignmentVO> listAssignmentsByCourse(Long courseId);

    /**
     * 作业详情
     */
    AssignmentVO getAssignmentById(Long assignmentId);

    /**
     * 提交作业
     */
    void submitAssignment(AssignmentSubmitDTO dto);

    /**
     * 我的提交记录（某作业）
     */
    AssignmentSubmissionVO getMySubmission(Long assignmentId);

    /**
     * 我的所有提交记录（可分页）
     */
    PageInfo<AssignmentSubmissionVO> listMySubmissions(Long courseId, Integer page, Integer pageSize);
}
