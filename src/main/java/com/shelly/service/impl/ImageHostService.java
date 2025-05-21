package com.shelly.service.impl;

import com.shelly.common.ServiceException;
import com.shelly.config.properties.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@Slf4j
public class ImageHostService {
    @Autowired
    private MinioClient minioClient;
    public String uploadPicture(MultipartFile file) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (file.getSize() > 3145728L) {
            throw new ServiceException("图片大小最大为3M！");
        }
        boolean b = minioClient.bucketExists(BucketExistsArgs.builder().bucket(MinioProperties.BUCKET_NAME).build());
        if(!b){
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(MinioProperties.BUCKET_NAME).build());
        }
        String originalFilename = file.getOriginalFilename();
        String suffixName;
        if (originalFilename != null) {
            suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (!".jpg".equals(suffixName) && !".png".equals(suffixName) && !".jpeg".equals(suffixName)) {
                throw new ServiceException("图片格式错误！");
            }
            String bucketName = MinioProperties.BUCKET_NAME;
            String url = MinioProperties.URL;
            return uploadFileToMinio(file, originalFilename, bucketName, url);
        }
        throw new ServiceException("上传失败");
    }

    private String uploadFileToMinio(MultipartFile file, String originalFileName, String bucketName, String url) {
        String key = UUID.randomUUID().toString().replace("-", ""); // 随机文件名
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));
        String filename = String.format("%s%s", key, suffix);
        log.info("==> 开始上传文件至 Minio, ObjectName: {}", filename);
        String returnUrl;
        try {
            // 上传文件到 Minio
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filename)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build()
            );
            returnUrl = url + "/" + bucketName + "/" + filename;
        } catch (Exception e) {
            throw new ServiceException("文件上传失败");
        }
        return returnUrl;
    }
}
