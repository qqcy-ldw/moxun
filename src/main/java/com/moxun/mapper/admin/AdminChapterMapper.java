package com.moxun.mapper.admin;

import com.moxun.Pojo.Dto.ChapterSaveDTO;
import com.moxun.Pojo.Entity.Chapter;
import com.moxun.Pojo.Entity.Section;
import com.moxun.Pojo.Vo.ChapterVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface AdminChapterMapper {

    ArrayList<ChapterVO> listChaptersByCourse(Long courseId);

    @Select("select * from chapters where id = #{id}")
    Chapter getChapterById(Long id);

    void addChapter(Chapter chapter);

    void updateChapter(ChapterSaveDTO dto);

    @Delete("delete from sections where chapter_id = #{id}")
    void deleteSections(Long id);

    @Delete("delete from chapters where id = #{id}")
    int deleteChapter(Long id);

    Section isSections(Long id);

//    @Select("")
//    boolean selectById(Long id);
}
