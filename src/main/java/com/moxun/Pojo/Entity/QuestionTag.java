package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionTag implements Serializable {
    private static final long serialVersionUID = 1L;
    
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
