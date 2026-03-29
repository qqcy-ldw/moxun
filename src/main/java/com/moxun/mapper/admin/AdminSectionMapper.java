package com.moxun.mapper.admin;

import com.moxun.Pojo.Entity.Section;
import com.moxun.Pojo.Vo.SectionVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdminSectionMapper {

    @Select("<script>" +
            "SELECT id, chapter_id AS chapterId, title, video_url AS videoUrl, " +
            "       duration, is_free AS isFree, sort, create_time AS createTime " +
            "FROM sections WHERE 1=1 " +
            "<if test='chapterId != null'> AND chapter_id = #{chapterId}</if>" +
            " ORDER BY sort ASC, id ASC" +
            "</script>")
    List<SectionVO> listByChapterId(@Param("chapterId") Long chapterId);

    @Select("SELECT id, chapter_id AS chapterId, title, video_url AS videoUrl, " +
            "       duration, is_free AS isFree, sort, create_time AS createTime, " +
            "       update_time AS updateTime " +
            "FROM sections WHERE id = #{id}")
    Section getById(Long id);

    @Insert("INSERT INTO sections (chapter_id, title, video_url, duration, is_free, sort, create_time, update_time) " +
            "VALUES (#{chapterId}, #{title}, #{videoUrl}, #{duration}, #{isFree}, #{sort}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Section section);

    @Update("UPDATE sections SET chapter_id = #{chapterId}, title = #{title}, " +
            "video_url = #{videoUrl}, duration = #{duration}, is_free = #{isFree}, " +
            "sort = #{sort}, update_time = #{updateTime} WHERE id = #{id}")
    int update(Section section);

    @Delete("DELETE FROM sections WHERE id = #{id}")
    int deleteById(Long id);

    @Select("SELECT COUNT(*) FROM sections WHERE chapter_id = #{chapterId}")
    int countByChapterId(Long chapterId);
}
