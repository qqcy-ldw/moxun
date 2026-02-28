package com.moxun.service.admin;

import com.moxun.Pojo.Dto.AssignmentCreateDTO;
import com.moxun.Pojo.Dto.AssignmentGradeDTO;
import com.moxun.Pojo.Dto.AssignmentUpdateDTO;
import com.moxun.Pojo.Vo.AssignmentSubmissionVO;
import com.moxun.Pojo.Vo.AssignmentVO;
import com.moxun.Pojo.Vo.PageResult;

import java.util.List;

/**
 * 管理员 - 作业管理 Service 接口
 */
public interface AdminAssignmentService {

    PageResult listAssignments(Long courseId, Integer page, Integer pageSize);

    AssignmentVO getAssignmentById(Long id);

    void addAssignment(AssignmentCreateDTO dto);

    void updateAssignment(AssignmentUpdateDTO dto);

    void deleteAssignment(Long id);

    /**
     * 某作业的提交列表（待批改/已批改）
     */
    PageResult listSubmissions(Long assignmentId, Integer graded, Integer page, Integer pageSize);

    /**
     * 批改作业
     */
    void gradeAssignment(AssignmentGradeDTO dto);
}
