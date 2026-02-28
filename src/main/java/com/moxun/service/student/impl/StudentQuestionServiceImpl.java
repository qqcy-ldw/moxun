package com.moxun.service.student.impl;

import com.moxun.Pojo.Dto.AnswerCreateDTO;
import com.moxun.Pojo.Dto.QuestionCreateDTO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.QuestionDetailVO;
import com.moxun.service.student.StudentQuestionService;
import org.springframework.stereotype.Service;

/**
 * 学生端 - 问答服务实现（占位）
 * 请在此类中实现具体业务逻辑
 */
@Service
public class StudentQuestionServiceImpl implements StudentQuestionService {

    @Override
    public PageResult listQuestions(Long courseId, String keyword, Integer page, Integer pageSize) {
        // TODO: 实现问题列表分页
        return new PageResult(0L, new java.util.ArrayList<>());
    }

    @Override
    public QuestionDetailVO getQuestionDetail(Long questionId) {
        // TODO: 实现问题详情（含回答）
        return null;
    }

    @Override
    public void createQuestion(QuestionCreateDTO dto) {
        // TODO: 实现提问（questions 表）
    }

    @Override
    public void createAnswer(AnswerCreateDTO dto) {
        // TODO: 实现回答（answers 表）
    }

    @Override
    public void likeAnswer(Long answerId) {
        // TODO: 实现点赞（answer_likes 表）
    }

    @Override
    public void unlikeAnswer(Long answerId) {
        // TODO: 实现取消点赞
    }
}
