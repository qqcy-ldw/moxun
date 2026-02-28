package com.moxun.controller.teacher;

import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.QuestionDetailVO;
import com.moxun.service.teacher.TeacherQuestionService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 教师端 - 问答管理接口
 *
 * 路径前缀：/teacher/questions
 * 权限：TEACHER 或 ADMIN
 * 说明：仅能查看/操作自己课程下的问答
 */
@Slf4j
@RestController
@RequestMapping("/teacher/questions")
@PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
public class TeacherQuestionController {

    @Autowired
    private TeacherQuestionService teacherQuestionService;

    /**
     * 分页查询我课程下的问答列表
     * GET /teacher/questions
     *
     * @param courseId 课程ID（可选）
     * @param keyword  关键词（可选）
     * @param page     页码
     * @param pageSize 每页条数
     */
    @GetMapping
    public Result<PageResult> listMyCourseQuestions(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult result = teacherQuestionService.listMyCourseQuestions(courseId, keyword, page, pageSize);
        return Result.success(result);
    }

    /**
     * 获取问答详情（含问题与回答列表）
     * GET /teacher/questions/{id}
     *
     * @param id 问题ID
     */
    @GetMapping("/{id}")
    public Result<QuestionDetailVO> getQuestionDetail(@PathVariable Long id) {
        QuestionDetailVO vo = teacherQuestionService.getQuestionDetail(id);
        return Result.success(vo);
    }

    /**
     * 采纳回答
     * PUT /teacher/questions/answers/{answerId}/accept
     *
     * @param answerId 回答ID
     */
    @PutMapping("/answers/{answerId}/accept")
    public Result<String> acceptAnswer(@PathVariable Long answerId) {
        teacherQuestionService.acceptAnswer(answerId);
        return Result.success("已采纳");
    }
}
