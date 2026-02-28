package com.moxun.service.teacher.impl;

import com.moxun.Pojo.Dto.ChapterSaveDTO;
import com.moxun.Pojo.Entity.Chapter;
import com.moxun.Pojo.Vo.ChapterVO;
import com.moxun.service.teacher.TeacherChapterService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 教师端 - 章节服务实现（占位）
 * 需校验章节所属课程是否为当前教师
 */
@Service
public class TeacherChapterServiceImpl implements TeacherChapterService {

    @Override
    public List<ChapterVO> listChaptersByCourse(Long courseId) {
        // TODO: 实现（需校验 course.teacher_id = 当前用户）
        return new ArrayList<>();
    }

    @Override
    public Chapter getChapterById(Long id) {
        return null;
    }

    @Override
    public void addChapter(ChapterSaveDTO dto) {
        // TODO: 需校验 course.teacher_id
    }

    @Override
    public void updateChapter(ChapterSaveDTO dto) {
        // TODO: 需校验 course.teacher_id
    }

    @Override
    public void deleteChapter(Long id) {
        // TODO: 需校验 course.teacher_id
    }
}
