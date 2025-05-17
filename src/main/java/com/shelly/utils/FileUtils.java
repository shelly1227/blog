package com.shelly.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;

@Slf4j
public class FileUtils {
    /**
     * 获取文件md5值
     *
     * @param inputStream 文件输入流
     * @return {@link String} 文件md5值
     */
    //获取文件md5值
    public static String getMd5(InputStream inputStream) {
        String md5 = null;
        try {
            md5 = DigestUtils.md5DigestAsHex(inputStream);
        } catch (Exception e) {
            log.error("get md5 error, {}", e.getMessage());
        }
        return md5;
    }
    public static boolean mkdir(File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return false;
        }
        return file.mkdirs();
    }

    public static void deleteFile(File src) {
        for (File file : src.listFiles()) {
            if (file.isFile()) {
                file.delete();
            } else {
                deleteFile(file);
            }
        }
        src.delete();
    }
    public static String getExtension(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension)) {
            extension = MimeTypeUtils.getExtension(Objects.requireNonNull(file.getContentType()));
        }
        return extension;
    }
}
