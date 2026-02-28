package com.moxun.service.teacher;

import com.moxun.Pojo.Dto.AssignmentCreateDTO;
import com.moxun.Pojo.Dto.AssignmentGradeDTO;
import com.moxun.Pojo.Dto.AssignmentUpdateDTO;
import com.moxun.Pojo.Vo.AssignmentSubmissionVO;
import com.moxun.Pojo.Vo.AssignmentVO;
import com.moxun.Pojo.Vo.PageResult;

/**
 * 教师端 - 作业服务接口
 * 仅操作自己课程的作业
 */
public interface TeacherAssignmentService {

    PageResult listMyAssignments(Long courseId, Integer page, Integer pageSize);

    AssignmentVO getAssignmentById(Long id);

    void addAssignment(AssignmentCreateDTO dto);

    void updateAssignment(AssignmentUpdateDTO dto);

    void deleteAssignment(Long id);

    PageResult listSubmissions(Long assignmentId, Integer graded, Integer page, Integer pageSize);

    void gradeAssignment(AssignmentGradeDTO dto);
}
