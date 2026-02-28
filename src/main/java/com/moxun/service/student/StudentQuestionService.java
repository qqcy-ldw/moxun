package com.moxun.service.student;

import com.moxun.Pojo.Dto.AnswerCreateDTO;
import com.moxun.Pojo.Dto.QuestionCreateDTO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.QuestionDetailVO;
import com.moxun.Pojo.Vo.QuestionListItemVO;

/**
 * 学生端 - 问答社区服务接口
 * 请自行实现 StudentQuestionServiceImpl
 */
public interface StudentQuestionService {

    /**
     * 分页查询问题列表（可按课程筛选）
     */
    PageResult listQuestions(Long courseId, String keyword, Integer page, Integer pageSize);

    /**
     * 问题详情（含回答列表）
     */
    QuestionDetailVO getQuestionDetail(Long questionId);

    /**
     * 提问
     */
    void createQuestion(QuestionCreateDTO dto);

    /**
     * 回答
     */
    void createAnswer(AnswerCreateDTO dto);

    /**
     * 点赞回答
     */
    void likeAnswer(Long answerId);

    /**
     * 取消点赞
     */
    void unlikeAnswer(Long answerId);
}
