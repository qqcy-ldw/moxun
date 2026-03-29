package com.moxun.service.admin.impl;

import com.moxun.Pojo.Dto.SectionSaveDTO;
import com.moxun.Pojo.Entity.Section;
import com.moxun.Pojo.Vo.SectionVO;
import com.moxun.exception.BusinessException;
import com.moxun.mapper.admin.AdminSectionMapper;
import com.moxun.service.admin.AdminSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminSectionServiceImpl implements AdminSectionService {

    @Autowired
    private AdminSectionMapper adminSectionMapper;

    @Override
    public List<SectionVO> listSectionsByChapter(Long chapterId) {
        if (chapterId == null) {
            throw new BusinessException("章节ID不能为空");
        }
        return adminSectionMapper.listByChapterId(chapterId);
    }

    @Override
    public Section getSectionById(Long id) {
        if (id == null) {
            throw new BusinessException("课时ID不能为空");
        }
        Section section = adminSectionMapper.getById(id);
        if (section == null) {
            throw new BusinessException("课时不存在");
        }
        return section;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSection(SectionSaveDTO dto) {
        if (dto == null) {
            throw new BusinessException("参数不能为空");
        }
        if (dto.getChapterId() == null) {
            throw new BusinessException("章节ID不能为空");
        }
        Section section = new Section();
        section.setChapterId(dto.getChapterId());
        section.setTitle(dto.getTitle());
        section.setVideoUrl(dto.getVideoUrl());
        section.setDuration(dto.getDuration());
        section.setIsFree(dto.getIsFree() == null ? 0 : dto.getIsFree());
        section.setSort(dto.getSort() == null ? 0 : dto.getSort());
        section.setCreateTime(LocalDateTime.now());
        section.setUpdateTime(LocalDateTime.now());
        int count = adminSectionMapper.insert(section);
        if (count == 0) {
            throw new BusinessException("新增课时失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSection(SectionSaveDTO dto) {
        if (dto == null || dto.getId() == null) {
            throw new BusinessException("参数错误");
        }
        Section existing = adminSectionMapper.getById(dto.getId());
        if (existing == null) {
            throw new BusinessException("课时不存在");
        }
        Section section = new Section();
        section.setId(dto.getId());
        section.setChapterId(dto.getChapterId() != null ? dto.getChapterId() : existing.getChapterId());
        section.setTitle(dto.getTitle() != null ? dto.getTitle() : existing.getTitle());
        section.setVideoUrl(dto.getVideoUrl() != null ? dto.getVideoUrl() : existing.getVideoUrl());
        section.setDuration(dto.getDuration() != null ? dto.getDuration() : existing.getDuration());
        section.setIsFree(dto.getIsFree() != null ? dto.getIsFree() : existing.getIsFree());
        section.setSort(dto.getSort() != null ? dto.getSort() : existing.getSort());
        section.setUpdateTime(LocalDateTime.now());
        int count = adminSectionMapper.update(section);
        if (count == 0) {
            throw new BusinessException("编辑课时失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSection(Long id) {
        if (id == null) {
            throw new BusinessException("课时ID不能为空");
        }
        Section existing = adminSectionMapper.getById(id);
        if (existing == null) {
            throw new BusinessException("课时不存在");
        }
        int count = adminSectionMapper.deleteById(id);
        if (count == 0) {
            throw new BusinessException("删除课时失败");
        }
    }
}
