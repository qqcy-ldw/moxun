package com.moxun.Pojo.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadedFile {
    /**
     * 文件ID
     */
    private Long id;

    /**
     * 上传用户ID
     */
    private Long userId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件URL
     */
    private String fileUrl;

    /**
     * 文件大小(字节)
     */
    private Long fileSize;

    /**
     * 文件类型(image/video)
     */
    private String fileType;

    /**
     * 视频时长(秒)
     */
    private Integer duration;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
