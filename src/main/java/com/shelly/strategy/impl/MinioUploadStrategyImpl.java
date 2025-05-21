package com.shelly.strategy.impl;

import com.shelly.config.properties.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * Minio上传策略
 * @author Shelly
 */
@Service("minioUploadStrategyImpl")
@Slf4j
public class MinioUploadStrategyImpl extends AbstractUploadStrategyImpl{
    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private MinioClient minioClient;
    @Override
    public Boolean exists(String filePath) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(minioProperties.bucketName)
                            .object(filePath)
                            .build()
            );
            return true;
        } catch (MinioException e) {
            log.warn("File not found in MinIO: {}", filePath);
            return false;
        } catch (Exception e) {
            log.error("Error checking existence of file: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream, long size) throws IOException {
        String objectName = path + fileName;
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.bucketName)
                            .object(objectName)
                            .stream(inputStream, size , -1)
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to upload file to MinIO: {}", e.getMessage(), e);
            throw new IOException("Upload to MinIO failed", e);
        }

    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return minioProperties.url + "/" + minioProperties.bucketName + "/" + filePath;
    }
}
