package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.common.Result;
import com.shelly.entity.vo.res.FileResp;
import com.shelly.entity.vo.req.FolderReq;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.FileQuery;
import com.shelly.service.BlogFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 博客管理
 * @author shelly
 */
@Slf4j
@RestController
@Tag(name = "文件模块")
@RequiredArgsConstructor
public class BlogFileController {
    private final BlogFileService blogFileService;
    @PostMapping("/admin/file/createFolder")
    @SaCheckPermission("system:file:createFolder")
    @Operation(summary = "创建文件夹")
    public Result<?> createFolder(@Valid @RequestBody FolderReq folder) {
        blogFileService.createFolder(folder);
        return Result.success();
    }
    @DeleteMapping("/admin/file/delete")
    @SaCheckPermission("system:file:delete")
    @Operation(summary = "删除文件")
    public Result<?> delete(@RequestBody List<Integer> fileIdList) {
        blogFileService.delete(fileIdList);
        return Result.success();
    }
    @SaCheckPermission("system:file:list")
    @GetMapping("/admin/file/list")
    @Operation(summary = "获取文件列表")
    public Result<PageResult<FileResp>> list(@Valid @RequestBody FileQuery fileQuery) {
        return Result.success(blogFileService.listFile(fileQuery));
    }
    @PostMapping("/admin/file/upload")
    @SaCheckPermission("system:file:upload")
    @Operation(summary = "上传文件")
    public Result<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
        blogFileService.uploadFile(file, path);
        return Result.success();
    }

    @GetMapping("/file/download/{fileId}")
    @Operation(summary = "下载文件")
    public Result<?> downloadFile(@PathVariable("fileId") Integer fileId) {
        blogFileService.downloadFile(fileId);
        return Result.success();
    }
}
