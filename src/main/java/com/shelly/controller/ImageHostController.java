package com.shelly.controller;

import com.shelly.common.Result;
import com.shelly.common.ServiceException;
import com.shelly.service.impl.ImageHostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


/**
 * 图片上传接口
 * @author shelly
 */
@RestController
@Tag(name = "图床模块")
public class ImageHostController {
    @Autowired
    private ImageHostService imageHostService;


    /**
     * 图片上传
     *
     * @param file 文件
     * @return {@link Result <String>} 图片地址
     */
    @Operation(summary = "图片上传")
    @PostMapping("/host/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        String url;
        try {
           url = imageHostService.uploadPicture(file);
        } catch (Exception e) {
            throw new ServiceException("上传失败");
        }
        return Result.success(url);
    }
}
