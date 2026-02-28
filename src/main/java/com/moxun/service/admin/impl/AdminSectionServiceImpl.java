package com.moxun.service.admin.impl;

import com.moxun.Pojo.Dto.SectionSaveDTO;
import com.moxun.Pojo.Entity.Section;
import com.moxun.Pojo.Vo.SectionVO;
import com.moxun.service.admin.AdminSectionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理员 - 课时管理 Service 实现（占位）
 */
@Service
public class AdminSectionServiceImpl implements AdminSectionService {

    @Override
    public List<SectionVO> listSectionsByChapter(Long chapterId) {
        // TODO: 实现课时列表
        return new ArrayList<>();
    }

    @Override
    public Section getSectionById(Long id) {
        // TODO: 实现课时详情
        return null;
    }

    @Override
    public void addSection(SectionSaveDTO dto) {
        // TODO: 实现新增课时
    }

    @Override
    public void updateSection(SectionSaveDTO dto) {
        // TODO: 实现编辑课时
    }

    @Override
    public void deleteSection(Long id) {
        // TODO: 实现删除课时
    }
}
