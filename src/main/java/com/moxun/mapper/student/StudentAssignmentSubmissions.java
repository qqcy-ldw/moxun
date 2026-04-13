package com.moxun.mapper.student;

import com.moxun.Pojo.Entity.AssignmentSubmission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生端 - 作业提交 Mapper
 * 👑 面试考点：直接操作 assignment_submissions 表
 * - 查询：提交记录列表
 * - 统计：未评分作业数、已提交作业数
 */
@Mapper
public interface StudentAssignmentSubmissions {

    /**
     * 查询某个作业的所有提交记录（教师端使用）
     * 👑 面试考点：关联查询 users 表获取学生信息
     *
     * @param assignmentId 作业ID
     * @return 提交记录列表（含学生信息）
     */
    List<AssignmentSubmission> listByAssignment(@Param("assignmentId") Long assignmentId);

    /**
     * 查询某课程的作业提交统计
     * 👑 面试考点：聚合查询 COUNT、SUM
     *
     * @param courseId 课程ID
     * @return 统计数据 { total: 总提交数, graded: 已评分数 }
     */
    List<java.util.Map<String, Object>> countByCourse(@Param("courseId") Long courseId);

    /**
     * 获取某学生的所有提交记录（分页）
     *
     * @param userId   学生ID
     * @param offset  偏移量
     * @param limit   每页数量
     * @return 提交记录列表
     */
    List<AssignmentSubmission> listByUser(
            @Param("userId") Long userId,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );

    /**
     * 获取某学生的提交记录总数
     *
     * @param userId 学生ID
     * @return 总数
     */
    int countByUser(@Param("userId") Long userId);

    /**
     * 根据ID查询提交记录
     *
     * @param id 提交ID
     * @return 提交记录
     */
    AssignmentSubmission getById(@Param("id") Long id);

    /**
     * 检查学生是否已提交某作业
     *
     * @param assignmentId 作业ID
     * @param userId      学生ID
     * @return 1-已提交，0-未提交
     */
    int existsByAssignmentAndUser(
            @Param("assignmentId") Long assignmentId,
            @Param("userId") Long userId
    );

    /**
     * 删除提交记录（学生撤销）
     *
     * @param id 提交ID
     */
    void deleteById(@Param("id") Long id);
}
