package com.moxun.service.admin.impl;

import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.QuestionDetailVO;
import com.moxun.service.admin.AdminQuestionService;
import org.springframework.stereotype.Service;

/**
 * 管理员 - 问答管理 Service 实现（占位）
 */
@Service
public class AdminQuestionServiceImpl implements AdminQuestionService {

    @Override
    public PageResult listQuestions(Long courseId, String keyword, Integer page, Integer pageSize) {
        // TODO: 实现问题列表分页
        return new PageResult(0L, new java.util.ArrayList<>());
    }

    @Override
    public QuestionDetailVO getQuestionDetail(Long questionId) {
        // TODO: 实现问题详情
        return null;
    }

    @Override
    public void deleteQuestion(Long questionId) {
        // TODO: 实现删除问题（级联删除回答、点赞等）
    }

    @Override
    public void acceptAnswer(Long answerId) {
        // TODO: 实现采纳回答（更新 answers.is_accepted）
    }
}
