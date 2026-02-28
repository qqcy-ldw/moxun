package com.moxun.service.admin;

import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.QuestionDetailVO;

/**
 * 管理员 - 问答管理 Service 接口
 */
public interface AdminQuestionService {

    PageResult listQuestions(Long courseId, String keyword, Integer page, Integer pageSize);

    QuestionDetailVO getQuestionDetail(Long questionId);

    void deleteQuestion(Long questionId);

    /**
     * 采纳回答为最佳答案
     */
    void acceptAnswer(Long answerId);
}
