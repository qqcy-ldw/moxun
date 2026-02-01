package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assignment implements Serializable {
    private static final long serialVersionUID = 1L;
    
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
     * 关联课程ID
     */
    private Long courseId;

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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 附件URL列表(JSON格式)
     */
    private String attachments;
}
