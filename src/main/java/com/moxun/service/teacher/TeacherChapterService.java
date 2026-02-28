package com.moxun.service.teacher;

import com.moxun.Pojo.Dto.ChapterSaveDTO;
import com.moxun.Pojo.Entity.Chapter;
import com.moxun.Pojo.Vo.ChapterVO;

import java.util.List;

/**
 * 教师端 - 章节服务接口
 * 仅操作自己课程下的章节
 */
public interface TeacherChapterService {

    List<ChapterVO> listChaptersByCourse(Long courseId);

    Chapter getChapterById(Long id);

    void addChapter(ChapterSaveDTO dto);

    void updateChapter(ChapterSaveDTO dto);

    void deleteChapter(Long id);
}
