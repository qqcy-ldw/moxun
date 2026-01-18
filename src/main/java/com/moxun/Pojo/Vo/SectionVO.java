package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 课时VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionVO {

    /**
     * 课时ID
     */
    private Long id;

    /**
     * 课时标题
     */
    private String title;

    /**
     * 视频URL
     */
    private String videoUrl;

    /**
     * 时长(分钟)
     */
    private Integer duration;

    /**
     * 是否免费试看
     */
    private Integer isFree;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
