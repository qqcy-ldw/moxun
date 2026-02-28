package com.moxun.service.teacher.impl;

import com.moxun.Pojo.Dto.SectionSaveDTO;
import com.moxun.Pojo.Entity.Section;
import com.moxun.Pojo.Vo.SectionVO;
import com.moxun.service.teacher.TeacherSectionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 教师端 - 课时服务实现（占位）
 * 需校验课时所属课程是否为当前教师
 */
@Service
public class TeacherSectionServiceImpl implements TeacherSectionService {

    @Override
    public List<SectionVO> listSectionsByChapter(Long chapterId) {
        return new ArrayList<>();
    }

    @Override
    public Section getSectionById(Long id) {
        return null;
    }

    @Override
    public void addSection(SectionSaveDTO dto) {
        // TODO: 需校验 chapter->course.teacher_id
    }

    @Override
    public void updateSection(SectionSaveDTO dto) {
        // TODO: 需校验 chapter->course.teacher_id
    }

    @Override
    public void deleteSection(Long id) {
        // TODO: 需校验 chapter->course.teacher_id
    }
}
