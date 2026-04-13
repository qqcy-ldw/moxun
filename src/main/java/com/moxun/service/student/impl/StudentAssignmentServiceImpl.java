package com.moxun.service.student.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.moxun.Pojo.Dto.AssignmentSubmitDTO;
import com.moxun.Pojo.Entity.AssignmentSubmission;
import com.moxun.Pojo.Vo.AssignmentSubmissionVO;
import com.moxun.Pojo.Vo.AssignmentVO;
import com.moxun.Pojo.Vo.CourseDetailVO;
import com.moxun.exception.BusinessException;
import com.moxun.mapper.student.StudentAssignmentMapper;
import com.moxun.service.admin.AdminAssignmentService;
import com.moxun.service.admin.AdminCourseService;
import com.moxun.service.student.StudentAssignmentService;
import com.moxun.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 学生端 - 作业服务实现
 *
 * 👑 面试考点：核心功能
 * 1. 作业列表/详情查询
 * 2. 作业提交：一条 INSERT 记录提交历史
 * 3. 提交记录查询：关联作业标题和课程名称
 *
 * 👑 面试考点：技术要点
 * - 校验作业是否存在、是否已截止
 * - 事务保证提交数据一致性
 * - PageHelper 分页插件
 */
@Slf4j
@Service
public class StudentAssignmentServiceImpl implements StudentAssignmentService {

    @Autowired
    private StudentAssignmentMapper studentAssignmentMapper;

    @Autowired
    private AdminCourseService adminCourseService;

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        Map<String, Object> user = UserContext.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("用户未登录");
        }
        return (Long) user.get("userId");
    }

    /**
     * 获取某课程的作业列表
     */
    @Override
    public List<AssignmentVO> listAssignmentsByCourse(Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new RuntimeException("请输入正确的课程ID");
        }
        return studentAssignmentMapper.listAssignmentsByCourse(courseId);
    }

    /**
     * 作业详情
     */
    @Override
    public AssignmentVO getAssignmentById(Long assignmentId) {
        if (assignmentId == null || assignmentId <= 0) {
            throw new RuntimeException("请输入正确的作业ID");
        }
        AssignmentVO vo = studentAssignmentMapper.getAssignmentById(assignmentId);
        if (vo == null) {
            throw new RuntimeException("作业不存在");
        }
        return vo;
    }

    /**
     * 提交作业
     * 👑 面试考点：
     * - 校验作业存在性
     * - 校验截止时间
     * - 事务保证 INSERT 成功
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void submitAssignment(AssignmentSubmitDTO dto) {
        Long userId = getCurrentUserId();

        // 校验作业存在
        AssignmentVO assignment = getAssignmentById(dto.getAssignmentId());

        // 校验截止时间：已截止的作业不允许提交
        if (assignment.getDeadline() != null && LocalDateTime.now().isAfter(assignment.getDeadline())) {
            throw new RuntimeException("作业已截止，无法提交");
        }

        // 构造提交记录
        AssignmentSubmission submission = new AssignmentSubmission();
        submission.setAssignmentId(dto.getAssignmentId());
        submission.setUserId(userId);
        submission.setContent(dto.getContent());
        submission.setSubmitTime(LocalDateTime.now());

        try {
            studentAssignmentMapper.submitAssignment(submission);
            log.info("用户 {} 提交作业成功，作业ID: {}", userId, dto.getAssignmentId());
        } catch (Exception e) {
            log.error("提交作业失败", e);
            throw new RuntimeException("提交作业失败");
        }
    }

    /**
     * 获取我的提交记录（单个作业）
     */
    @Override
    public AssignmentSubmissionVO getMySubmission(Long assignmentId) {
        Long userId = getCurrentUserId();

        AssignmentSubmissionVO vo = studentAssignmentMapper.getMySubmission(assignmentId, userId.intValue());
        if (vo == null) {
            throw new RuntimeException("暂无提交记录");
        }
        return vo;
    }

    /**
     * 我的所有提交记录（支持按课程筛选）
     * 👑 面试考点：PageHelper 分页插件使用
     * - PageHelper.startPage(page, pageSize) 自动拦截下一条 SQL
     * - PageInfo 包含分页信息和数据列表
     */
    @Override
    public PageInfo<AssignmentSubmissionVO> listMySubmissions(Long courseId, Integer page, Integer pageSize) {
        Long userId = getCurrentUserId();

        // 使用 PageHelper 进行分页
        PageHelper.startPage(page, pageSize);
//        CourseDetailVO courseById = adminCourseService.getCourseById(courseId);
//        if (courseById == null) {
//            throw new BusinessException("课程不存在");
//        }
        List<AssignmentSubmissionVO> list = studentAssignmentMapper.listMySubmissions(userId, courseId);

        return new PageInfo<>(list);
    }
}
