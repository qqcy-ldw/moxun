package com.moxun.mapper.student;

import com.moxun.Pojo.Vo.AnswerVO;
import com.moxun.Pojo.Vo.QuestionDetailVO;
import com.moxun.Pojo.Vo.QuestionListItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 学生端 - 问答 Mapper
 * 提供问答列表、详情、提问、回答、点赞等数据访问操作
 */
@Mapper
public interface StudentQuestionMapper {

    /**
     * 问题列表查询（支持课程筛选和关键词搜索）
     * 👑 面试考点：LIKE 模糊搜索 + 关联查询用户名和课程名
     *
     * @param keyword  关键词（可选）
     * @param courseId 课程ID（可选）
     * @return 问题列表
     */
    List<QuestionListItemVO> listQuestions(
            @Param("keyword") String keyword,
            @Param("courseId") Long courseId
    );

    /**
     * 问题详情查询
     *
     * @param questionId 问题ID
     * @return 问题详情
     */
    QuestionDetailVO getQuestionDetail(@Param("questionId") Long questionId);

    /**
     * 回答列表查询
     * 👑 面试考点：关联查询用户信息 + 判断当前用户是否已点赞
     *
     * @param questionId 问题ID
     * @param userId    当前用户ID（用于判断点赞状态）
     * @return 回答列表
     */
    List<AnswerVO> listAnswers(
            @Param("questionId") Long questionId,
            @Param("userId") Long userId
    );

    /**
     * 创建问题
     * 👑 面试考点：INSERT + LAST_INSERT_ID() 获取自增ID
     *
     * @param title    问题标题
     * @param content  问题内容
     * @param userId   用户ID
     * @param courseId 课程ID
     */
    void createQuestion(
            @Param("title") String title,
            @Param("content") String content,
            @Param("userId") Long userId,
            @Param("courseId") Long courseId
    );

    /**
     * 创建回答
     *
     * @param questionId 问题ID
     * @param userId    用户ID
     * @param content   回答内容
     */
    void createAnswer(
            @Param("questionId") Long questionId,
            @Param("userId") Long userId,
            @Param("content") String content
    );

    /**
     * 更新问题的回答数
     *
     * @param questionId 问题ID
     */
    void incrementAnswerCount(@Param("questionId") Long questionId);

    /**
     * 点赞回答
     * 👑 面试考点：INSERT IGNORE 实现幂等（已点赞则忽略）
     *
     * @param answerId 回答ID
     * @param userId  用户ID
     */
    void likeAnswer(
            @Param("answerId") Long answerId,
            @Param("userId") Long userId
    );

    /**
     * 取消点赞
     *
     * @param answerId 回答ID
     * @param userId  用户ID
     */
    void unlikeAnswer(
            @Param("answerId") Long answerId,
            @Param("userId") Long userId
    );

    /**
     * 更新回答的点赞数
     *
     * @param answerId 回答ID
     * @param delta    增量（+1 或 -1）
     */
    void updateLikeCount(
            @Param("answerId") Long answerId,
            @Param("delta") Integer delta
    );

    /**
     * 检查用户是否已点赞
     *
     * @param answerId 回答ID
     * @param userId  用户ID
     * @return 点赞数量
     */
    int checkLiked(
            @Param("answerId") Long answerId,
            @Param("userId") Long userId
    );

    /**
     * 检查问题是否存在
     *
     * @param questionId 问题ID
     * @return 问题数量
     */
    int countQuestion(@Param("questionId") Long questionId);

    /**
     * 检查回答是否存在
     *
     * @param answerId 回答ID
     * @return 回答数量
     */
    int countAnswer(@Param("answerId") Long answerId);

    /**
     * 更新问题浏览数
     *
     * @param questionId 问题ID
     */
    void incrementViewCount(@Param("questionId") Long questionId);
}
