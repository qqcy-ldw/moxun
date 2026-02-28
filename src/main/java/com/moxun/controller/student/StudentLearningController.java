package com.moxun.controller.student;

import com.moxun.Pojo.Dto.LearningProgressUpdateDTO;
import com.moxun.Pojo.Vo.ChapterVO;
import com.moxun.service.student.StudentLearningService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 学生端 - 学习进度相关接口
 * 路径前缀：/student/learning
 */
@Slf4j
@RestController
@RequestMapping("/student/learning")
@PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
public class StudentLearningController {

    @Autowired
    private StudentLearningService studentLearningService;

    /**
     * 获取课程学习目录（章节+课时）
     */
    @GetMapping("/courses/{courseId}/catalog")
    public Result<List<ChapterVO>> getCourseCatalog(@PathVariable Long courseId) {
        List<ChapterVO> list = studentLearningService.getCourseCatalog(courseId);
        return Result.success(list);
    }

    /**
     * 更新学习进度
     */
    @PostMapping("/progress")
    public Result<String> updateProgress(@RequestBody LearningProgressUpdateDTO dto) {
        studentLearningService.updateProgress(dto);
        return Result.success("更新成功");
    }

    /**
     * 获取某课程的学习进度概览
     */
    @GetMapping("/courses/{courseId}/progress")
    public Result<Map<String, Object>> getCourseProgress(@PathVariable Long courseId) {
        Map<String, Object> progress = studentLearningService.getCourseProgress(courseId);
        return Result.success(progress);
    }

    /**
     * 获取某课时的学习进度
     */
    @GetMapping("/sections/{sectionId}/progress")
    public Result<Map<String, Object>> getSectionProgress(@PathVariable Long sectionId) {
        Map<String, Object> progress = studentLearningService.getSectionProgress(sectionId);
        return Result.success(progress);
    }
}
