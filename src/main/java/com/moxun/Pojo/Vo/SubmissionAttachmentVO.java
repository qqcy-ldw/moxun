package com.moxun.Pojo.Vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 作业提交附件VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionAttachmentVO implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 附件ID
     */
    private Long id;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件大小
     */
    private Long fileSize;
}
