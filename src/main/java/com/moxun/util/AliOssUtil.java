package com.moxun.util;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@Slf4j
public class AliOssUtil {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    /**
     * 上传文件到OSS，按「根文件夹+年月日」层级存储
     * @param bytes 文件字节数组
     * @param fileType 业务文件类型（传入FOLDER_AVATAR或FOLDER_COURSE_COVER）
     * @param file 上传的MultipartFile文件（用于获取ContentType）
     * @return 文件在OSS的访问URL
     */
    public  String upload(byte[] bytes, String fileType, MultipartFile file) {
        // 校验参数
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("文件字节数组不能为空");
        }
        if (file == null) {
            throw new IllegalArgumentException("MultipartFile文件不能为空");
        }
        if (fileType == null || !fileType.endsWith("/")) {
            throw new IllegalArgumentException("文件类型必须是指定的根文件夹（以/结尾），如：头像/、课程封面/");
        }

        // 1. 生成按「根文件夹+年月日」的文件路径
        String objectName = generateObjectName(fileType, file.getOriginalFilename());

        OSS ossClient = null;
        try {
            // 2. 创建OSSClient实例
            ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 3. 设置文件元数据（支持预览、指定ContentType）
            ObjectMetadata objectMetadata = new ObjectMetadata();
            // 设置ContentDisposition为inline，支持浏览器预览
            objectMetadata.setContentDisposition("inline");
            // 设置文件ContentType（保证预览/下载格式正确）
            String contentType = file.getContentType();
            if (contentType == null || contentType.isEmpty()) {
                contentType = "application/octet-stream"; // 默认二进制流
            }
            objectMetadata.setContentType(contentType);
            // 设置文件长度
            objectMetadata.setContentLength(bytes.length);

            // 4. 构建上传请求并执行上传
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucketName,
                    objectName,
                    new ByteArrayInputStream(bytes),
                    objectMetadata
            );
            ossClient.putObject(putObjectRequest);

        } catch (OSSException oe) {
            log.error("OSS服务端异常-文件上传失败", oe);
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            throw new RuntimeException("OSS文件上传失败：" + oe.getErrorMessage(), oe);
        } catch (ClientException ce) {
            log.error("OSS客户端异常-文件上传失败", ce);
            System.out.println("Error Message:" + ce.getMessage());
            throw new RuntimeException("OSS客户端通信失败：" + ce.getMessage(), ce);
        } finally {
            // 5. 释放OSS客户端资源
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        // 6. 拼接文件访问URL（规则：https://BucketName.Endpoint/ObjectName）
        String accessUrl = String.format("https://%s.%s/%s", bucketName, endpoint.replace("https://", ""), objectName);
        log.info("文件上传成功，访问路径：{}", accessUrl);
        return accessUrl;
    }

    /**
     * 生成OSS对象名称（路径）：根文件夹 + 年月日/ + 随机文件名（避免重复）
     * @param rootFolder 根文件夹（如：头像/、课程封面/）
     * @param originalFileName 原始文件名（用于获取后缀）
     * @return 完整的OSS对象名称，例：头像/2026/01/08/8f9e7d6c5b4a3s2d1f8g9h0j7k8l9m0n.jpg
     */
    private  String generateObjectName(String rootFolder, String originalFileName) {
        // 1. 获取年月日格式（yyyy/MM/dd）
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String datePath = dateFormat.format(new Date());

        // 2. 生成随机文件名（UUID + 原文件后缀，避免重名）
        String fileSuffix = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            //获取文件文件名后缀
            fileSuffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String randomFileName = UUID.randomUUID().toString().replace("-", "") + fileSuffix;

        // 3. 拼接完整路径：根文件夹 + 年月日/ + 随机文件名
        return rootFolder + datePath + "/" + randomFileName;
    }


    /**
     * 文件下载
     */
    public void downloadFileFromOSS(String region,
                                    String objectName,
                                    String localPath) throws Exception {
        // 1. 初始化访问凭证提供者（从环境变量获取OSS密钥）
        EnvironmentVariableCredentialsProvider credentialsProvider =
                CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 2. 初始化OSS客户端配置（设置签名版本V4）
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);

        // 3. 声明OSS客户端实例（后续在finally中关闭）
        OSS ossClient = null;

        try {
            // 4. 创建OSS客户端实例
            ossClient = OSSClientBuilder.create()
                    .endpoint(endpoint)
                    .credentialsProvider(credentialsProvider)
                    .clientConfiguration(clientBuilderConfiguration)
                    .region(region)
                    .build();

            // 5. 执行文件下载操作（覆盖已存在的本地文件，不存在则新建）
            ossClient.getObject(new GetObjectRequest(bucketName, objectName), new File(localPath));

            log.info("文件下载成功！OSS对象路径：" + objectName + "，本地保存路径：" + localPath);

        } catch (OSSException oe) {
            // 6. 捕获OSS服务端异常（请求已到达OSS但被拒绝）
            System.err.println("OSS服务端异常，下载失败！");
            System.err.println("错误信息：" + oe.getErrorMessage());
            System.err.println("错误编码：" + oe.getErrorCode());
            System.err.println("请求ID：" + oe.getRequestId());
            System.err.println("主机ID：" + oe.getHostId());
            throw new Exception("OSS服务端异常：" + oe.getErrorMessage(), oe); // 包装异常并向上抛出

        } catch (ClientException ce) {
            // 7. 捕获OSS客户端异常（客户端与OSS通信失败，如网络问题）
            System.err.println("OSS客户端异常，下载失败！");
            System.err.println("错误信息：" + ce.getMessage());
            throw new Exception("OSS客户端异常：" + ce.getMessage(), ce); // 包装异常并向上抛出

        } finally {
            // 8. 最终确保OSS客户端实例关闭，释放资源（无论是否发生异常）
            if (ossClient != null) {
                ossClient.shutdown();
                System.out.println("OSS客户端实例已关闭，资源释放完成。");
            }
        }
    }

}

