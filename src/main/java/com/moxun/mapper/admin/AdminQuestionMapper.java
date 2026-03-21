package com.moxun.mapper.admin;

import com.github.pagehelper.Page;
import com.moxun.Pojo.Vo.QuestionDetailVO;
import com.moxun.Pojo.Vo.QuestionListItemVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AdminQuestionMapper {
    Page<QuestionListItemVO> listQuestions(Long courseId, String keyword);

    @Select("SELECT * FROM questions WHERE id = #{questionId}")
    QuestionDetailVO getQuestionDetail(Long questionId);

    @Select("SELECT id from answers WHERE question_id = #{questionId}")
    List<Integer> getAnswers(Long questionId);

    void deleteLikesByAnswerIds(List<Integer> answerIds);

    @Delete("delete from answers where question_id = #{questionId}")
    void deleteAnswersByQuestionId(Long questionId);

    @Delete("delete from questions where id = #{questionId}")
    void deleteQuestion(Long questionId);

    @Update("update answers set is_accepted = 1 where id = #{answerId}")
    void acceptAnswer(Long answerId);
}
