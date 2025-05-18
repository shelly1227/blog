package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.common.Result;
import com.shelly.entity.vo.query.AlbumQuery;
import com.shelly.entity.vo.req.AlbumReq;
import com.shelly.entity.vo.res.AlbumBackResp;
import com.shelly.entity.vo.res.AlbumResp;
import com.shelly.service.AlbumService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.shelly.entity.vo.PageResult;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "相册模块")
public class AlbumController {
    private final AlbumService albumService;

    @SaCheckPermission("web:album:add")
    @PostMapping("/admin/album/add")
    @Operation(summary = "添加相册")
    public Result<?> addAlbum(@Validated @RequestBody AlbumReq album) {
        albumService.addAlbum(album);
        return Result.success();
    }

    @SaCheckPermission("web:album:delete")
    @DeleteMapping("/admin/album/delete/{albumId}")
    @Operation(summary = "删除相册")
    public Result<?> deleteAlbum(@PathVariable("albumId") Integer albumId) {
        albumService.deleteAlbum(albumId);
        return Result.success();
    }

    @SaCheckPermission("web:album:edit")
    @GetMapping("/admin/album/edit/{albumId}")
    @Operation(summary = "编辑相册")
    public Result<AlbumReq> editAlbum(@PathVariable("albumId") Integer albumId) {
        return Result.success(albumService.editAlbum(albumId));
    }

    @SaCheckPermission("web:album:list")
    @Operation(summary = "获取相册列表")
    @GetMapping("/admin/album/list")
    public Result<PageResult<AlbumBackResp>> listAlbumBackVO(AlbumQuery albumQuery) {
        return Result.success(albumService.listAlbumBackVO(albumQuery));
    }
    @SaCheckPermission("web:album:update")
    @PutMapping("/admin/album/update")
    @Operation(summary = "修改相册")
    public Result<?> updateAlbum(@Validated @RequestBody AlbumReq album) {
        albumService.updateAlbum(album);
        return Result.success();
    }

    @ApiImplicitParam(name = "file", value = "相册封面", required = true, dataType = "MultipartFile")
    @SaCheckPermission("web:album:upload")
    @PostMapping("/admin/album/upload")
    @Operation(summary = "上传相册封面")
    public Result<String> uploadAlbumCover(@RequestParam("file") MultipartFile file) {
        return Result.success(albumService.uploadAlbumCover(file));
    }

    @GetMapping("/album/list")
    @Operation(summary = "获取相册列表")
    public Result<List<AlbumResp>> listAlbumVO() {
        return Result.success(albumService.listAlbumVO());
    }

}