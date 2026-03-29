package com.moxun.mapper.student;

import com.moxun.Pojo.Entity.AssignmentSubmission;
import com.moxun.Pojo.Vo.AssignmentSubmissionVO;
import com.moxun.Pojo.Vo.AssignmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StudentAssignmentMapper {
    @Select("SELECT id, title, description, course_id, deadline, total_score, create_time FROM assignments WHERE course_id = #{courseId} ORDER BY deadline DESC")
    List<AssignmentVO> listAssignmentsByCourse(Long courseId);

    @Select("SELECT id, title, description, course_id, deadline, total_score, create_time FROM assignments WHERE id = #{assignmentId}")
    AssignmentVO getAssignmentById(Long assignmentId);

    void submitAssignment(AssignmentSubmission assignmentSubmission);

    AssignmentSubmissionVO getMySubmission(Long assignmentId, Integer userId);
}
