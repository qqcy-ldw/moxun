package com.moxun.service.teacher.impl;

import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.QuestionDetailVO;
import com.moxun.service.teacher.TeacherQuestionService;
import org.springframework.stereotype.Service;

/**
 * 教师端 - 问答服务实现（占位）
 * 仅操作自己课程下的问题
 */
@Service
public class TeacherQuestionServiceImpl implements TeacherQuestionService {

    @Override
    public PageResult listMyCourseQuestions(Long courseId, String keyword, Integer page, Integer pageSize) {
        return new PageResult(0L, new java.util.ArrayList<>());
    }

    @Override
    public QuestionDetailVO getQuestionDetail(Long questionId) {
        return null;
    }

    @Override
    public void acceptAnswer(Long answerId) {
        // TODO: 需校验 question->course.teacher_id
    }
}
