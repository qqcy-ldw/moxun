package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 学生提交作业请求DTO
 */
@Data
public class AssignmentSubmitDTO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 作业ID
     */
    @NotNull(message = "作业ID不能为空")
    private Long assignmentId;

    /**
     * 提交内容
     */
    private String content;

    /**
     * 附件ID列表(JSON或逗号分隔字符串，由上传接口返回)
     */
    private String attachmentIds;
}
