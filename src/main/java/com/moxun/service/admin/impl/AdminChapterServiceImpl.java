package com.moxun.service.admin.impl;

import com.moxun.Pojo.Dto.ChapterSaveDTO;
import com.moxun.Pojo.Entity.Chapter;
import com.moxun.Pojo.Vo.ChapterVO;
import com.moxun.service.admin.AdminChapterService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理员 - 章节管理 Service 实现（占位）
 */
@Service
public class AdminChapterServiceImpl implements AdminChapterService {

    @Override
    public List<ChapterVO> listChaptersByCourse(Long courseId) {
        // TODO: 实现章节列表（含课时）
        return new ArrayList<>();
    }

    @Override
    public Chapter getChapterById(Long id) {
        // TODO: 实现章节详情
        return null;
    }

    @Override
    public void addChapter(ChapterSaveDTO dto) {
        // TODO: 实现新增章节
    }

    @Override
    public void updateChapter(ChapterSaveDTO dto) {
        // TODO: 实现编辑章节
    }

    @Override
    public void deleteChapter(Long id) {
        // TODO: 实现删除章节（需先删除其下课时）
    }
}
