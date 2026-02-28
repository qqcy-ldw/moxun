package com.moxun.service.admin;

import com.moxun.Pojo.Dto.SectionSaveDTO;
import com.moxun.Pojo.Entity.Section;
import com.moxun.Pojo.Vo.SectionVO;

import java.util.List;

/**
 * 管理员 - 课时管理 Service 接口
 */
public interface AdminSectionService {

    List<SectionVO> listSectionsByChapter(Long chapterId);

    Section getSectionById(Long id);

    void addSection(SectionSaveDTO dto);

    void updateSection(SectionSaveDTO dto);

    void deleteSection(Long id);
}
