package com.moxun.mapper.student;

import com.moxun.Pojo.Entity.AssignmentSubmission;
import com.moxun.Pojo.Vo.AssignmentSubmissionVO;
import com.moxun.Pojo.Vo.AssignmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生端 - 作业 Mapper
 * 提供作业查询、提交等数据访问操作
 */
@Mapper
public interface StudentAssignmentMapper {

    /**
     * 查询某课程的作业列表
     *
     * @param courseId 课程ID
     * @return 作业列表
     */
    List<AssignmentVO> listAssignmentsByCourse(@Param("courseId") Long courseId);

    /**
     * 查询作业详情
     *
     * @param assignmentId 作业ID
     * @return 作业详情
     */
    AssignmentVO getAssignmentById(@Param("assignmentId") Long assignmentId);

    /**
     * 提交作业
     * 👑 面试考点：INSERT INTO ... VALUES 实现数据新增
     *
     * @param assignmentSubmission 提交记录
     */
    void submitAssignment(AssignmentSubmission assignmentSubmission);

    /**
     * 查询我的提交记录（单个作业）
     *
     * @param assignmentId 作业ID
     * @param userId       用户ID
     * @return 提交记录
     */
    AssignmentSubmissionVO getMySubmission(
            @Param("assignmentId") Long assignmentId,
            @Param("userId") Integer userId
    );

    /**
     * 查询我的所有提交记录（支持按课程筛选）
     * 👑 面试考点：LEFT JOIN 关联查询作业标题
     *
     * @param userId   用户ID
     * @param courseId 课程ID（可选，筛选特定课程的提交）
     * @return 提交记录列表
     */
    List<AssignmentSubmissionVO> listMySubmissions(
            @Param("userId") Long userId,
            @Param("courseId") Long courseId
    );

    /**
     * 检查作业是否存在
     *
     * @param assignmentId 作业ID
     * @return 作业数量
     */
    int countById(@Param("assignmentId") Long assignmentId);
}
