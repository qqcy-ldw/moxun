package com.moxun.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moxun.Pojo.Dto.AssignmentCreateDTO;
import com.moxun.Pojo.Dto.AssignmentGradeDTO;
import com.moxun.Pojo.Dto.AssignmentUpdateDTO;
import com.moxun.Pojo.Entity.Assignment;
import com.moxun.Pojo.Entity.AssignmentSubmission;
import com.moxun.Pojo.Vo.AssignmentSubmissionVO;
import com.moxun.Pojo.Vo.AssignmentVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.SubmissionAttachmentVO;
import com.moxun.exception.BusinessException;
import com.moxun.mapper.admin.AdminAssignmentMapper;
import com.moxun.service.admin.AdminAssignmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员 - 作业管理 Service 实现
 */
@Service
public class AdminAssignmentServiceImpl implements AdminAssignmentService {

    @Autowired
    private AdminAssignmentMapper adminAssignmentMapper;

    /**
     * 分页查询作业列表
     *
     * @param courseId 课程ID（可选，为null时查询全部）
     * @param page     页码（从1开始）
     * @param pageSize 每页条数
     * @return 分页结果（包含total总数 + records数据列表）
     */
    @Override
    public PageResult listAssignments(Long courseId, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Page<AssignmentVO> assignmentPage = adminAssignmentMapper.listAssignments(courseId);
        return new PageResult(assignmentPage.getTotal(), assignmentPage.getResult());
    }

    /**
     * 获取作业详情
     * @param id 作业ID
     * @return 作业详情VO
     * @throws
     */
    @Override
    public AssignmentVO getAssignmentById(Long id) {
        if (id == null) {
            throw new BusinessException("作业ID不能为空");
        }
        AssignmentVO assignmentVO = adminAssignmentMapper.getAssignmentById(id);
        if (assignmentVO == null) {
            throw new BusinessException("作业不存在");
        }
        return assignmentVO;
    }

    /**
     * 新增作业
     * @param
     * @throws BusinessException 当插入失败时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addAssignment(AssignmentCreateDTO dto) {
        Assignment assignment = new Assignment();
        BeanUtils.copyProperties(dto, assignment);
        // 总分默认为100分
        if (assignment.getTotalScore() == null) {
            assignment.setTotalScore(100);
        }
        LocalDateTime now = LocalDateTime.now();
        assignment.setCreateTime(now);
        assignment.setUpdateTime(now);
        int rows = adminAssignmentMapper.insertAssignment(assignment);
        if (rows == 0) {
            throw new BusinessException("新增作业失败");
        }
    }

    /**
     * 编辑作业
     * @param
     * @throws
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAssignment(AssignmentUpdateDTO dto) {
        if (dto.getId() == null) {
            throw new BusinessException("作业ID不能为空");
        }
        Long exists = adminAssignmentMapper.checkAssignmentExists(dto.getId());
        if (exists == null) {
            throw new BusinessException("作业不存在");
        }
        Assignment assignment = new Assignment();
        BeanUtils.copyProperties(dto, assignment);
        assignment.setUpdateTime(LocalDateTime.now());
        int rows = adminAssignmentMapper.updateAssignment(assignment);
        if (rows == 0) {
            throw new BusinessException("编辑作业失败");
        }
    }

    /**
     * 删除作业
     * @param id 作业ID
     * @throws
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAssignment(Long id) {
        if (id == null) {
            throw new BusinessException("作业ID不能为空");
        }

        //删除作业附件
        adminAssignmentMapper.deleteAssignmentAttachments(id);

        //删除作业提交记录
        adminAssignmentMapper.deleteAssignmentSubmissions(id);

        //删除作业
        int rows = adminAssignmentMapper.deleteAssignment(id);

        if (rows == 0) {
            throw new BusinessException("删除作业失败，作业不存在或已被删除");
        }
    }

    /**
     * 分页查询某作业的提交列表
     * @param assignmentId 作业ID
     * @param graded       批改状态（0-待批改，1-已批改，null-全部）
     * @param page         页码
     * @param pageSize     每页条数
     * @return 分页结果
     */
    @Override
    public PageResult listSubmissions(Long assignmentId, Integer graded, Integer page, Integer pageSize) {
        if (assignmentId == null) {
            throw new BusinessException("作业ID不能为空");
        }

        // 校验作业是否存在
        Long exists = adminAssignmentMapper.checkAssignmentExists(assignmentId);
        if (exists == null) {
            throw new BusinessException("作业不存在");
        }

        PageHelper.startPage(page, pageSize);
        Page<AssignmentSubmissionVO> submissionPage = adminAssignmentMapper.listSubmissions(assignmentId, graded);

        // 为每条提交记录加载附件列表
        // 注意：这里是简单实现，大数据量时可优化为批量查询
        List<AssignmentSubmissionVO> submissions = submissionPage.getResult();
        for (AssignmentSubmissionVO submission : submissions) {
            List<SubmissionAttachmentVO> attachments = adminAssignmentMapper.listAttachmentsBySubmissionId(submission.getId());
            submission.setAttachments(attachments);
        }

        return new PageResult(submissionPage.getTotal(), submissions);
    }

    /**
     * 批改作业
     * @param dto 批改信息DTO（包含submissionId、score、feedback）
     * @throws BusinessException 当提交ID为空、提交记录不存在或批改失败时抛出
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void gradeAssignment(AssignmentGradeDTO dto) {
        if (dto.getSubmissionId() == null) {
            throw new BusinessException("提交ID不能为空");
        }

        AssignmentSubmission submission = adminAssignmentMapper.getSubmissionById(dto.getSubmissionId());
        if (submission == null) {
            throw new BusinessException("提交记录不存在");
        }

        submission.setScore(dto.getScore());
        submission.setFeedback(dto.getFeedback());
        submission.setGradedTime(LocalDateTime.now()); // 记录批改时间，作为已批改标识

        int rows = adminAssignmentMapper.gradeSubmission(submission);
        if (rows == 0) {
            throw new BusinessException("批改作业失败");
        }
    }
}
