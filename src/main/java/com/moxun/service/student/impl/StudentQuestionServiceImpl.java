package com.moxun.service.student.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.moxun.Pojo.Dto.AnswerCreateDTO;
import com.moxun.Pojo.Dto.QuestionCreateDTO;
import com.moxun.Pojo.Vo.AnswerVO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.QuestionDetailVO;
import com.moxun.Pojo.Vo.QuestionListItemVO;
import com.moxun.mapper.student.StudentQuestionMapper;
import com.moxun.service.student.StudentQuestionService;
import com.moxun.util.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 学生端 - 问答社区服务实现
 *
 * 👑 面试考点：核心功能
 * 1. 问题列表：支持关键词搜索、课程筛选
 * 2. 问题详情：包含回答列表和点赞状态
 * 3. 提问/回答：INSERT 数据新增
 * 4. 点赞/取消点赞：INSERT IGNORE + DELETE 实现幂等
 *
 * 👑 面试考点：技术要点
 * - 点赞去重：数据库唯一索引 + INSERT IGNORE
 * - 浏览数自增：UPDATE SET count = count + 1
 * - PageHelper 分页插件
 */
@Slf4j
@Service
public class StudentQuestionServiceImpl implements StudentQuestionService {

    @Autowired
    private StudentQuestionMapper studentQuestionMapper;

    /**
     * 获取当前登录用户ID
     */
    private Long getCurrentUserId() {
        Map<String, Object> user = UserContext.getCurrentUser();
        if (user == null) {
            throw new RuntimeException("用户未登录");
        }
        return (Long) user.get("userId");
    }

    /**
     * 问题列表查询（支持关键词搜索和课程筛选）
     * 👑 面试考点：PageHelper 分页插件
     */
    @Override
    public PageResult listQuestions(Long courseId, String keyword, Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<QuestionListItemVO> lists = studentQuestionMapper.listQuestions(keyword, courseId);
        PageInfo<QuestionListItemVO> pageInfo = new PageInfo<>(lists);

        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    /**
     * 问题详情（含回答列表）
     * 👑 面试考点：两步查询（问题详情 + 回答列表），保证数据一致性
     */
    @Override
    public QuestionDetailVO getQuestionDetail(Long questionId) {
        Long userId = getCurrentUserId();

        // 查询问题详情
        QuestionDetailVO detail = studentQuestionMapper.getQuestionDetail(questionId);
        if (detail == null) {
            throw new RuntimeException("问题不存在");
        }

        // 浏览数 +1
        studentQuestionMapper.incrementViewCount(questionId);

        // 查询回答列表（带点赞状态）
        List<AnswerVO> answers = studentQuestionMapper.listAnswers(questionId, userId);
        detail.setAnswers(answers);

        return detail;
    }

    /**
     * 提问
     * 👑 面试考点：INSERT 实现数据新增
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createQuestion(QuestionCreateDTO dto) {
        Long userId = getCurrentUserId();

        // 创建问题
        studentQuestionMapper.createQuestion(
                dto.getTitle(),
                dto.getContent(),
                userId,
                dto.getCourseId()
        );

        log.info("用户 {} 发布问题: {}", userId, dto.getTitle());
    }

    /**
     * 回答
     * 👑 面试考点：事务保证回答新增 + 问题回答数更新同时成功或失败
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createAnswer(AnswerCreateDTO dto) {
        Long userId = getCurrentUserId();

        // 校验问题是否存在
        int count = studentQuestionMapper.countQuestion(dto.getQuestionId());
        if (count == 0) {
            throw new RuntimeException("问题不存在");
        }

        // 创建回答
        studentQuestionMapper.createAnswer(
                dto.getQuestionId(),
                userId,
                dto.getContent()
        );

        // 更新问题回答数
        studentQuestionMapper.incrementAnswerCount(dto.getQuestionId());

        log.info("用户 {} 回答问题 {}", userId, dto.getQuestionId());
    }

    /**
     * 点赞回答
     * 👑 面试考点：INSERT IGNORE 保证幂等（已点赞则忽略，不抛异常）
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void likeAnswer(Long answerId) {
        Long userId = getCurrentUserId();

        // 校验回答是否存在
        int count = studentQuestionMapper.countAnswer(answerId);
        if (count == 0) {
            throw new RuntimeException("回答不存在");
        }

        // 点赞（INSERT IGNORE 保证幂等）
        studentQuestionMapper.likeAnswer(answerId, userId);

        // 检查是否真的插入了（INSERT IGNORE 不抛异常，需要手动判断）
        int liked = studentQuestionMapper.checkLiked(answerId, userId);
        if (liked > 0) {
            // 真正新增了点赞，更新点赞数
            studentQuestionMapper.updateLikeCount(answerId, 1);
            log.info("用户 {} 点赞回答 {}", userId, answerId);
        }
    }

    /**
     * 取消点赞
     * 👑 面试考点：DELETE + 判断影响行数，避免重复删除
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void unlikeAnswer(Long answerId) {
        Long userId = getCurrentUserId();

        // 检查是否已点赞
        int liked = studentQuestionMapper.checkLiked(answerId, userId);
        if (liked == 0) {
            // 未点赞，无需取消
            return;
        }

        // 取消点赞
        studentQuestionMapper.unlikeAnswer(answerId, userId);

        // 更新点赞数 -1
        studentQuestionMapper.updateLikeCount(answerId, -1);

        log.info("用户 {} 取消点赞回答 {}", userId, answerId);
    }
}
