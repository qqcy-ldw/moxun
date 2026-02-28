package com.moxun.Pojo.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * 创建回答请求DTO
 */
@Data
public class AnswerCreateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 问题ID
     */
    @NotNull(message = "问题ID不能为空")
    private Long questionId;

    /**
     * 回答内容
     */
    @NotBlank(message = "回答内容不能为空")
    private String content;
}
