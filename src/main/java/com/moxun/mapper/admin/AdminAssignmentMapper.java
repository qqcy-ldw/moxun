package com.moxun.mapper.admin;

import com.github.pagehelper.Page;
import com.moxun.Pojo.Entity.Assignment;
import com.moxun.Pojo.Entity.AssignmentSubmission;
import com.moxun.Pojo.Vo.AssignmentSubmissionVO;
import com.moxun.Pojo.Vo.AssignmentVO;
import com.moxun.Pojo.Vo.SubmissionAttachmentVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 管理员 - 作业管理 Mapper接口
 * 
 * 功能说明：
 * 1. 作业的CRUD操作
 * 2. 学生提交记录的查询
 * 3. 作业批改相关操作
 * 
 * @author moxun
 */
@Mapper
public interface AdminAssignmentMapper {

    /**
     * 分页查询作业列表
     * 
     * @param courseId 课程ID（可为null，查全部）
     * @return 作业分页列表
     */
    Page<AssignmentVO> listAssignments(@Param("courseId") Long courseId);

    /**
     * 根据ID查询作业详情
     * 
     * @param id 作业ID
     * @return 作业详情VO
     */
    AssignmentVO getAssignmentById(@Param("id") Long id);

    /**
     * 新增作业
     * 
     * @param assignment 作业实体
     * @return 影响行数
     */
    int insertAssignment(Assignment assignment);

    /**
     * 更新作业
     * 
     * @param assignment 作业实体
     * @return 影响行数
     */
    int updateAssignment(Assignment assignment);

    /**
     * 删除作业
     * 
     * @param id 作业ID
     * @return 影响行数
     */
    @Delete("DELETE FROM assignments WHERE id = #{id}")
    int deleteAssignment(@Param("id") Long id);

    /**
     * 检查作业是否存在
     * 
     * @param id 作业ID
     * @return 作业ID（存在）或 null（不存在）
     */
    @Select("SELECT id FROM assignments WHERE id = #{id}")
    Long checkAssignmentExists(@Param("id") Long id);

    /**
     * 分页查询某作业的提交列表
     * 
     * @param assignmentId 作业ID
     * @param graded 是否已批改：0-待批改，1-已批改，null-全部
     * @return 提交分页列表
     */
    Page<AssignmentSubmissionVO> listSubmissions(@Param("assignmentId") Long assignmentId,
                                                  @Param("graded") Integer graded);

    /**
     * 根据提交ID查询提交记录
     * 
     * @param submissionId 提交ID
     * @return 提交记录实体
     */
    @Select("SELECT id, assignment_id, user_id, content, score, feedback, submit_time, graded_time " +
            "FROM assignment_submissions WHERE id = #{submissionId}")
    AssignmentSubmission getSubmissionById(@Param("submissionId") Long submissionId);

    /**
     * 批改作业（更新提交记录的分数、反馈和批改时间）
     * 
     * @param submission 提交实体
     * @return 影响行数
     */
    int gradeSubmission(AssignmentSubmission submission);

    /**
     * 查询提交的附件列表
     * 
     * @param submissionId 提交ID
     * @return 附件列表
     */
    List<SubmissionAttachmentVO> listAttachmentsBySubmissionId(@Param("submissionId") Long submissionId);

    /**
     * 删除作业的附件
     *
     * @param id 作业ID
     */
    @Delete("DELETE FROM assignment_attachments WHERE assignment_id = #{id}")
    void deleteAssignmentAttachments(Long id);

    /**
     * 删除作业的提交记录
     *
     * @param id 作业ID
     */
    @Delete("DELETE FROM assignment_submissions WHERE assignment_id = #{id}")
    void deleteAssignmentSubmissions(Long id);
}
