package com.moxun.mapper.admin;

import com.moxun.Pojo.Dto.CourseCategoryDTO;
import com.moxun.Pojo.Vo.CourseCategoryVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminCourseCategoryMapper {
    @Select("select id, name, icon, parent_id, sort, create_time, update_time from course_categories")
    List<CourseCategoryVO> listCategories();

    @Select("select id, name, icon, parent_id, sort, create_time, update_time from course_categories where parent_id = #{parentId}")
    List<CourseCategoryVO> listChildren(Integer parentId);

    void addCategory(CourseCategoryDTO courseCategoryDTO);

    void updateCategory(CourseCategoryDTO courseCategoryDTO);

    void delParentCourseCategory(Integer parentId);

    @Delete("delete from course_categories where id = #{parentId}")
    void delCourseCategory(Integer parentId);

//    @Delete("delete from course_categories where parent_id = #{id}")
    void delPosterityCategory(List<Long> posterityId);
}
