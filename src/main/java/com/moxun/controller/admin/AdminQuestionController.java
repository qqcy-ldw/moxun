package com.moxun.controller.admin;

import com.moxun.Pojo.Vo.PageResult;
import com.moxun.Pojo.Vo.QuestionDetailVO;
import com.moxun.service.admin.AdminQuestionService;
import com.moxun.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员 - 问答管理接口
 *
 * 路径前缀：/admin/questions
 * 权限：ADMIN
 */
@Slf4j
@RestController
@RequestMapping("/admin/questions")
public class AdminQuestionController {

    @Autowired
    private AdminQuestionService adminQuestionService;

    /**
     * 分页查询问答列表
     * GET /admin/questions
     *
     * @param courseId 课程ID（可选）
     * @param keyword  关键词（可选）
     * @param page     页码
     * @param pageSize 每页条数
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Result<PageResult> listQuestions(
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        PageResult result = adminQuestionService.listQuestions(courseId, keyword, page, pageSize);
        return Result.success(result);
    }

    /**
     * 获取问答详情（含问题与回答列表）
     * GET /admin/questions/{id}
     *
     * @param id 问题ID
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Result<QuestionDetailVO> getQuestionDetail(@PathVariable Long id) {
        QuestionDetailVO vo = adminQuestionService.getQuestionDetail(id);
        return Result.success(vo);
    }

    /**
     * 删除问题
     * DELETE /admin/questions/{id}
     *
     * @param id 问题ID
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Result<String> deleteQuestion(@PathVariable Long id) {
        adminQuestionService.deleteQuestion(id);
        return Result.success("删除成功");
    }

    /**
     * 采纳回答为最佳答案
     * PUT /admin/questions/answers/{answerId}/accept
     *
     * @param answerId 回答ID
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    @PutMapping("/answers/{answerId}/accept")
    public Result<String> acceptAnswer(@PathVariable Long answerId) {
        adminQuestionService.acceptAnswer(answerId);
        return Result.success("已采纳");
    }
}
