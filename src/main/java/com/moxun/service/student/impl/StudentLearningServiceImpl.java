package com.moxun.service.student.impl;

import com.moxun.Pojo.Dto.LearningProgressUpdateDTO;
import com.moxun.Pojo.Vo.ChapterVO;
import com.moxun.service.student.StudentLearningService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生端 - 学习进度服务实现（占位）
 * 请在此类中实现具体业务逻辑
 */
@Service
public class StudentLearningServiceImpl implements StudentLearningService {

    @Override
    public List<ChapterVO> getCourseCatalog(Long courseId) {
        // TODO: 实现课程目录（chapters + sections）
        return new ArrayList<>();
    }

    @Override
    public void updateProgress(LearningProgressUpdateDTO dto) {
        // TODO: 实现更新学习进度（learning_progress 表）
    }

    @Override
    public Map<String, Object> getCourseProgress(Long courseId) {
        // TODO: 实现课程进度概览
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getSectionProgress(Long sectionId) {
        // TODO: 实现课时进度
        return new HashMap<>();
    }
}
