package com.moxun.service.admin.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.QuestionDetailVO;
import com.moxun.Pojo.Vo.QuestionListItemVO;
import com.moxun.mapper.admin.AdminQuestionMapper;
import com.moxun.service.admin.AdminQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 管理员 - 问答管理 Service 实现（占位）
 */
@Service
public class AdminQuestionServiceImpl implements AdminQuestionService {

    @Autowired
    private AdminQuestionMapper adminQuestionMapper;

    /**
     * 问题列表分页
     */
    @Override
    public PageResult listQuestions(Long courseId, String keyword, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        Page<QuestionListItemVO> questionListItemVOPage = adminQuestionMapper.listQuestions(courseId, keyword);
        return new PageResult(questionListItemVOPage.getTotal(), questionListItemVOPage.getResult());
    }

    /**
     * 根据问题ID查询问题详情
     */
    @Override
    public QuestionDetailVO getQuestionDetail(Long questionId) {
        if (questionId == null){
            throw new RuntimeException("问题ID不能为空");
        }
        QuestionDetailVO questionDetailVO = adminQuestionMapper.getQuestionDetail(questionId);
        return questionDetailVO;
    }

    /**
     * 删除问题（级联删除回答、点赞等）
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteQuestion(Long questionId) {
        // 删除点赞
        // 根据问题id查询查询回答
        List<Integer> answerIds = adminQuestionMapper.getAnswers(questionId);
        if (answerIds != null && !answerIds.isEmpty()) {
            // 删除点赞
            adminQuestionMapper.deleteLikesByAnswerIds(answerIds);
        }
        // 删除回答
        adminQuestionMapper.deleteAnswersByQuestionId(questionId);
        // 删除问题
        adminQuestionMapper.deleteQuestion(questionId);
    }

    /**
     * 采纳回答（更新 answers.is_accepted）
     */
    @Override
    public void acceptAnswer(Long answerId) {
        if (answerId == null){
            throw new RuntimeException("回答ID不能为空");
        }
        adminQuestionMapper.acceptAnswer(answerId);
    }
}
