package com.moxun.mapper.admin;

import com.github.pagehelper.Page;
import com.moxun.Pojo.Entity.CourseComment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminCommentMapper {
    Page<CourseComment> pageListComments(Integer rating, Long courseId);

    @Select("select id, course_id, user_id, content, rating, create_time, update_time from course_comments where id = #{id}")
    CourseComment getCommentById(Long id);

    @Delete("delete from course_comments where id = #{id}")
    int deleteComment(Long id);
}
