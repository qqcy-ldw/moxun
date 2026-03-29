package com.moxun.service.student.impl;

import com.moxun.Pojo.Dto.AssignmentSubmitDTO;
import com.moxun.Pojo.Entity.Assignment;
import com.moxun.Pojo.Entity.AssignmentSubmission;
import com.moxun.Pojo.Vo.AssignmentSubmissionVO;
import com.moxun.Pojo.Vo.AssignmentVO;
import com.moxun.mapper.student.StudentAssignmentMapper;
import com.moxun.mapper.student.StudentAssignmentSubmissions;
import com.moxun.service.student.StudentAssignmentService;
import com.moxun.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 学生端 - 作业服务实现（占位）
 * 请在此类中实现具体业务逻辑
 */
@Slf4j
@Service
public class StudentAssignmentServiceImpl implements StudentAssignmentService {

    @Autowired
    private StudentAssignmentMapper studentAssignmentMapper;

    @Autowired
    private StudentAssignmentSubmissions studentAssignmentSubmissions;

    /**
     * 根据课程id获取作业列表
     * @param courseId
     * @return
     */
    @Override
    public List<AssignmentVO> listAssignmentsByCourse(Long courseId) {
        if(courseId == null || courseId <= 0){
            throw new RuntimeException("请输入正确的课程id");
        }
        return studentAssignmentMapper.listAssignmentsByCourse(courseId);
    }

    /**
     * 根据作业id获取作业详情
     * @param assignmentId
     * @return
     */
    @Override
    public AssignmentVO getAssignmentById(Long assignmentId) {
        if (assignmentId == null || assignmentId <= 0) {
            throw new RuntimeException("请输入正确的作业id");
        }
        return studentAssignmentMapper.getAssignmentById(assignmentId);
    }

    /**
     * 提交作业
     * @param dto
     */
    @Override
    public void submitAssignment(AssignmentSubmitDTO dto) {
        AssignmentSubmission assignmentSubmission = new AssignmentSubmission();
        BeanUtils.copyProperties(dto, assignmentSubmission);
        Map<String, Object> currentUser = UserContext.getCurrentUser();
        Long userId = (Long) currentUser.get("userId");
        assignmentSubmission.setUserId(userId);
        assignmentSubmission.setSubmitTime(LocalDateTime.now());
        try {
            studentAssignmentMapper.submitAssignment(assignmentSubmission);
        } catch (Exception e) {
            log.error("提交作业失败", e);
            throw new RuntimeException("提交作业失败");
        }
        if (Objects.nonNull(dto.getAttachmentIds())) {
            //TODO 目前还没有确定提交的是json还是图片，后续需要根据实际情况进行处理
//            studentAssignmentSubmissions.batchInsert(assignmentSubmission.getId(), dto.getAttachmentIds());
        }
    }

    /**
     * 获取我的提交记录(根据作业id查询记录)
     * @param assignmentId
     * @return
     */
    @Override
    public AssignmentSubmissionVO getMySubmission(Long assignmentId) {
        if (assignmentId == null || assignmentId <= 0) {
            throw new RuntimeException("请输入正确的作业id");
        }
        Map<String, Object> currentUser = UserContext.getCurrentUser();
        Integer userId = (Integer) currentUser.get("userId");
        AssignmentSubmissionVO assignmentSubmissionVO = studentAssignmentMapper.getMySubmission(assignmentId, userId);
        if (assignmentSubmissionVO == null) {
            throw new RuntimeException("没有找到该提交记录");
        }
        // TODO: 处理附件信息
        return assignmentSubmissionVO;
    }

    /**
     * 获取我的所有提交
     * @param courseId
     * @param page
     * @param pageSize
     * @return
     */
    @Override
    public List<AssignmentSubmissionVO> listMySubmissions(Long courseId, Integer page, Integer pageSize) {
        // TODO: 实现我的所有提交
        return new ArrayList<>();
    }
}
