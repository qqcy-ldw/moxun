package com.moxun.service.student.impl;

import com.moxun.Pojo.Dto.LearningProgressUpdateDTO;
import com.moxun.Pojo.Vo.ChapterVO;
import com.moxun.Pojo.Vo.LearningProgressVO;
import com.moxun.Pojo.Vo.SectionVO;
import com.moxun.mapper.student.StudentLearningMapper;
import com.moxun.service.student.StudentLearningService;
import com.moxun.util.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生端 - 学习进度服务实现
 *
 * 👑 面试考点：核心功能
 * 1. 课程目录查询：章节+课时结构
 * 2. 进度同步：INSERT OR UPDATE 策略（首次学习插入，已学习则更新）
 * 3. 完成度计算：finishedCount / totalSections
 *
 * 👑 面试考点：技术要点
 * - 课时进度通过 position（播放位置）判断是否完成
 * - 课程完成度通过统计已完成的课时数计算
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentLearningServiceImpl implements StudentLearningService {

    private final StudentLearningMapper studentLearningMapper;

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
     * 获取课程学习目录（章节+课时）
     * 👑 面试考点：MyBatis resultMap 嵌套 collection 实现一对多查询
     */
    @Override
    public List<ChapterVO> getCourseCatalog(Long courseId) {
        if (courseId == null || courseId <= 0) {
            throw new RuntimeException("请输入正确的课程ID");
        }
        return studentLearningMapper.getCourseCatalog(courseId);
    }

    /**
     * 更新学习进度
     * 👑 面试考点：INSERT OR UPDATE 策略（MySQL REPLACE 或 先查后插/更新）
     * 👑 面试考点：进度判断：position >= duration * 0.9 视为已完成
     *
     * @param dto 进度更新请求
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateProgress(LearningProgressUpdateDTO dto) {
        Long userId = getCurrentUserId();
        Long sectionId = dto.getSectionId();

        // 获取课时信息
        SectionVO section = studentLearningMapper.getSectionById(sectionId);
        if (section == null) {
            throw new RuntimeException("课时不存在");
        }

        // 通过课时ID获取课程ID
        Long courseId = getCourseIdBySectionId(sectionId);

        // 播放位置（秒）
        Integer position = dto.getPosition() != null ? dto.getPosition() : 0;

        // 是否完成：如果前端传了 finished，用前端值；否则根据播放位置判断（播放90%以上视为完成）
        Integer finished;
        if (dto.getFinished() != null) {
            finished = dto.getFinished() ? 1 : 0;
        } else {
            // 根据播放位置判断：position >= duration * 0.9
            finished = (section.getDuration() != null && position >= section.getDuration() * 0.9) ? 1 : 0;
        }

        // 校验是否已有学习记录
        LearningProgressVO existingProgress = studentLearningMapper.getProgress(userId, sectionId);

        if (existingProgress == null) {
            // 首次学习，插入记录
            studentLearningMapper.insertProgress(userId, courseId, sectionId, position, finished);
            log.info("用户 {} 首次学习课时 {}，进度: {}，完成: {}", userId, sectionId, position, finished);
        } else {
            // 已有记录，更新进度
            studentLearningMapper.updateProgress(userId, sectionId, position, finished);
            log.info("用户 {} 更新课时 {} 进度: {}，完成: {}", userId, sectionId, position, finished);
        }
    }

    /**
     * 通过课时ID获取课程ID（工具方法）
     * 👑 面试考点：跨表关联查询 sections -> chapters
     */
    private Long getCourseIdBySectionId(Long sectionId) {
        Long courseId = studentLearningMapper.getCourseIdBySectionId(sectionId);
        if (courseId == null) {
            throw new RuntimeException("课时所属课程不存在");
        }
        return courseId;
    }

    /**
     * 获取课程学习进度概览
     * 👑 面试考点：返回课程完成度、已学课时数、最后学习位置
     */
    @Override
    public Map<String, Object> getCourseProgress(Long courseId) {
        Long userId = getCurrentUserId();

        // 统计总课时数
        int totalSections = studentLearningMapper.countSections(courseId);

        // 统计已完成课时数
        int finishedCount = studentLearningMapper.countFinishedSections(userId, courseId);

        // 获取最后学习的课时ID
        Long lastSectionId = studentLearningMapper.getLastSectionId(userId, courseId);

        // 计算完成度
        double progressPercent = totalSections > 0 ? (finishedCount * 100.0 / totalSections) : 0;

        Map<String, Object> result = new HashMap<>();
        result.put("totalSections", totalSections);
        result.put("finishedCount", finishedCount);
        result.put("lastSectionId", lastSectionId);
        result.put("progressPercent", Math.round(progressPercent * 10) / 10.0); // 保留1位小数

        return result;
    }

    /**
     * 获取某课时的学习进度
     */
    @Override
    public Map<String, Object> getSectionProgress(Long sectionId) {
        Long userId = getCurrentUserId();

        // 获取课时信息
        SectionVO section = studentLearningMapper.getSectionById(sectionId);
        if (section == null) {
            throw new RuntimeException("课时不存在");
        }

        // 获取学习进度
        LearningProgressVO progress = studentLearningMapper.getProgress(userId, sectionId);

        Map<String, Object> result = new HashMap<>();
        result.put("section", section);
        result.put("position", progress != null ? progress.getPosition() : 0);
        result.put("finished", progress != null ? progress.getFinished() : 0);

        return result;
    }
}
