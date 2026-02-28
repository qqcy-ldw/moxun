package com.moxun.controller.student;

import com.moxun.Pojo.Dto.AnswerCreateDTO;
import com.moxun.Pojo.Dto.QuestionCreateDTO;
import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.QuestionDetailVO;
import com.moxun.service.student.StudentQuestionService;
import com.moxun.util.Result;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 学生端 - 问答社区接口
 * 路径前缀：/student/questions
 */
@Slf4j
@RestController
@RequestMapping("/student/questions")
@PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER') or hasRole('ADMIN')")
public class StudentQuestionController {

    @Autowired
    private StudentQuestionService studentQuestionService;

    /**
     * 分页查询问题列表
     */
    @GetMapping
    public Result<PageResult> listQuestions(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult result = studentQuestionService.listQuestions(courseId, keyword, page, pageSize);
        return Result.success(result);
    }

    /**
     * 问题详情（含回答）
     */
    @GetMapping("/{questionId}")
    public Result<QuestionDetailVO> getQuestionDetail(@PathVariable Long questionId) {
        QuestionDetailVO vo = studentQuestionService.getQuestionDetail(questionId);
        return Result.success(vo);
    }

    /**
     * 提问
     */
    @PostMapping
    public Result<String> createQuestion(@Valid @RequestBody QuestionCreateDTO dto) {
        studentQuestionService.createQuestion(dto);
        return Result.success("发布成功");
    }

    /**
     * 回答
     */
    @PostMapping("/answers")
    public Result<String> createAnswer(@Valid @RequestBody AnswerCreateDTO dto) {
        studentQuestionService.createAnswer(dto);
        return Result.success("回答成功");
    }

    /**
     * 点赞回答
     */
    @PostMapping("/answers/{answerId}/like")
    public Result<String> likeAnswer(@PathVariable Long answerId) {
        studentQuestionService.likeAnswer(answerId);
        return Result.success("点赞成功");
    }

    /**
     * 取消点赞
     */
    @DeleteMapping("/answers/{answerId}/like")
    public Result<String> unlikeAnswer(@PathVariable Long answerId) {
        studentQuestionService.unlikeAnswer(answerId);
        return Result.success("已取消点赞");
    }
}
