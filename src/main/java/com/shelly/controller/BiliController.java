package com.shelly.controller;

import com.shelly.common.Result;
import com.shelly.service.BiliService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@Tag(name = "B站模块")
public class BiliController {

    @Autowired
    private BiliService biliService;

    /**
     * B站图片上传
     *
     * @param file 文件
     * @return {@link Result <String>} 图片地址
     */
    @Operation(summary = "B站图片上传")
    @PostMapping("/bili/upload")
    public Result<String> biliUpload(@RequestParam("file_up") MultipartFile file,
                                     @RequestParam(value = "csrf") String csrf,
                                     @RequestParam(value = "data") String data) {
        return Result.success(biliService.uploadBiliPicture(file, csrf, data));
    }
}