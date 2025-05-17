package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.common.Result;
import com.shelly.entity.vo.Response.FileResp;
import com.shelly.entity.vo.Request.FolderReq;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.FileQuery;
import com.shelly.service.BlogFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

/**
 * @description: 博客管理
 * @author: shelly
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class BlogFileController {
    private final BlogFileService blogFileService;
    @PostMapping("/admin/file/createFolder")
    @SaCheckPermission("system:file:createFolder")
    public Result<?> createFolder(@Valid @RequestBody FolderReq folder) {
        blogFileService.createFolder(folder);
        return Result.success();
    }
    @DeleteMapping("/admin/file/delete")
    @SaCheckPermission("system:file:delete")
    public Result<?> delete(@RequestBody List<Integer> fileIdList) {
        blogFileService.delete(fileIdList);
        return Result.success();
    }
    @SaCheckPermission("system:file:list")
    @GetMapping("/admin/file/list")
    public Result<PageResult<FileResp>> list(@Valid @RequestBody FileQuery fileQuery) {
        return Result.success(blogFileService.listFile(fileQuery));
    }
    @PostMapping("/admin/file/upload")
    @SaCheckPermission("system:file:upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
        blogFileService.uploadFile(file, path);
        return Result.success();
    }

    @GetMapping("/file/download/{fileId}")
    public Result<?> downloadFile(@PathVariable("fileId") Integer fileId) {
        blogFileService.downloadFile(fileId);
        return Result.success();
    }
}
