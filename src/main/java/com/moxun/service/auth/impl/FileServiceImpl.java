package com.moxun.service.auth.impl;

import com.moxun.service.auth.FileService;
import com.moxun.util.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class FileServiceImpl implements FileService {
    @Autowired
    public AliOssUtil aliOssUtil;
    @Override
    public String upload(MultipartFile file, String fileType) {
        try {
            log.info("类型：{}", fileType);
            String upload = aliOssUtil.upload(file.getBytes(), fileType, file);
            return upload;
        } catch (IOException e) {
            log.info("文件上传失败{}",e);
        }
        return "文件上传失败";
    }
}
