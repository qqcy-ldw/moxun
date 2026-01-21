package com.moxun.controller.auth;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文件接口
 */
@Slf4j
@RestController
@RequestMapping("/auth/api/v1")
public class FileController {

//    @Autowired
//    private AliOssUtil aliOssUtil;
//
//    /**
//     * 文件上传
//     */
//    @PostMapping("/files/upload")
//    public Result<String> upload(MultipartFile file){
//        try {
//            //获取文件名
//            String originalFilename = file.getOriginalFilename();
//            //获取后缀名
//            String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
//            //UUID.randomUUID() + substring构造新的文件名称
//            //文件访问路径
//            String upload = aliOssUtil.upload(file.getBytes(), AVATAR.toString(), file);
//            return Result.success(upload);
//        } catch (IOException e) {
//            log.info("文件上传失败{}",e);
//        }
//        return Result.error("文件上传失败");
//    }

}
