package com.moxun.service.student;

import com.moxun.Pojo.Dto.LearningProgressUpdateDTO;
import com.moxun.Pojo.Vo.ChapterVO;

import java.util.List;
import java.util.Map;

/**
 * 学生端 - 学习进度服务接口
 * 请自行实现 StudentLearningServiceImpl
 */
public interface StudentLearningService {

    /**
     * 获取课程学习目录（章节+课时）
     */
    List<ChapterVO> getCourseCatalog(Long courseId);

    /**
     * 更新学习进度（播放位置、是否完成）
     */
    void updateProgress(LearningProgressUpdateDTO dto);

    /**
     * 获取某课程的学习进度概览
     * 返回：{ "totalSections": 20, "finishedCount": 5, "lastSectionId": 3 }
     */
    Map<String, Object> getCourseProgress(Long courseId);

    /**
     * 获取某课时的学习进度
     */
    Map<String, Object> getSectionProgress(Long sectionId);
}
