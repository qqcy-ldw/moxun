package com.moxun.service.admin;

import com.moxun.Pojo.Dto.ChapterSaveDTO;
import com.moxun.Pojo.Entity.Chapter;
import com.moxun.Pojo.Vo.ChapterVO;

import java.util.List;

/**
 * 管理员 - 章节管理 Service 接口
 */
public interface AdminChapterService {

    List<ChapterVO> listChaptersByCourse(Long courseId);

    Chapter getChapterById(Long id);

    void addChapter(ChapterSaveDTO dto);

    void updateChapter(ChapterSaveDTO dto);

    void deleteChapter(Long id);
}
