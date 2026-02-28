package com.moxun.service.teacher;

import com.moxun.Pojo.Dto.SectionSaveDTO;
import com.moxun.Pojo.Entity.Section;
import com.moxun.Pojo.Vo.SectionVO;

import java.util.List;

/**
 * 教师端 - 课时服务接口
 * 仅操作自己课程下的课时
 */
public interface TeacherSectionService {

    List<SectionVO> listSectionsByChapter(Long chapterId);

    Section getSectionById(Long id);

    void addSection(SectionSaveDTO dto);

    void updateSection(SectionSaveDTO dto);

    void deleteSection(Long id);
}
