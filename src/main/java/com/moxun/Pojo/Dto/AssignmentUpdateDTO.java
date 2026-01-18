package com.moxun.Pojo.Dto;

import java.time.LocalDateTime;

/**
 * 更新作业请求DTO
 */
public class AssignmentUpdateDTO {

    /**
     * 作业ID
     */
    private Long id;

    /**
     * 作业标题
     */
    private String title;

    /**
     * 作业描述
     */
    private String description;

    /**
     * 截止日期
     */
    private LocalDateTime deadline;

    /**
     * 总分
     */
    private Integer totalScore;

    /**
     * 作业内容
     */
    private String content;
}
