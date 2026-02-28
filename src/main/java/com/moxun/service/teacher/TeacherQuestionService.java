package com.moxun.service.teacher;

import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.QuestionDetailVO;

/**
 * 教师端 - 问答服务接口
 * 仅操作自己课程下的问题
 */
public interface TeacherQuestionService {

    PageResult listMyCourseQuestions(Long courseId, String keyword, Integer page, Integer pageSize);

    QuestionDetailVO getQuestionDetail(Long questionId);

    void acceptAnswer(Long answerId);
}
