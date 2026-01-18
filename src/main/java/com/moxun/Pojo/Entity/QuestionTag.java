package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTag {
    /**
     * 标签ID
     */
    private Long id;

    /**
     * 问题ID
     */
    private Long questionId;

    /**
     * 标签名称
     */
    private String tagName;
}
