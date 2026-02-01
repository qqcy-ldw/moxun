package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 章节VO（包含课时列表）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterVO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 章节ID
     */
    private Long id;

    /**
     * 章节标题
     */
    private String title;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 课时列表
     */
    private List<SectionVO> sections;
}
