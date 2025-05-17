package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.common.Result;
import com.shelly.entity.vo.Query.AlbumQuery;
import com.shelly.entity.vo.Request.AlbumReq;
import com.shelly.entity.vo.Response.AlbumBackResp;
import com.shelly.entity.vo.Response.AlbumResp;
import com.shelly.service.AlbumService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.shelly.entity.vo.PageResult;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumService albumService;

    @SaCheckPermission("web:album:add")
    @PostMapping("/admin/album/add")
    public Result<?> addAlbum(@Validated @RequestBody AlbumReq album) {
        albumService.addAlbum(album);
        return Result.success();
    }

    @SaCheckPermission("web:album:delete")
    @DeleteMapping("/admin/album/delete/{albumId}")
    public Result<?> deleteAlbum(@PathVariable("albumId") Integer albumId) {
        albumService.deleteAlbum(albumId);
        return Result.success();
    }

    @SaCheckPermission("web:album:edit")
    @GetMapping("/admin/album/edit/{albumId}")
    public Result<AlbumReq> editAlbum(@PathVariable("albumId") Integer albumId) {
        return Result.success(albumService.editAlbum(albumId));
    }

    @SaCheckPermission("web:album:list")
    @GetMapping("/admin/album/list")
    public Result<PageResult<AlbumBackResp>> listAlbumBackVO(AlbumQuery albumQuery) {
        return Result.success(albumService.listAlbumBackVO(albumQuery));
    }
    @SaCheckPermission("web:album:update")
    @PutMapping("/admin/album/update")
    public Result<?> updateAlbum(@Validated @RequestBody AlbumReq album) {
        albumService.updateAlbum(album);
        return Result.success();
    }

    @ApiImplicitParam(name = "file", value = "相册封面", required = true, dataType = "MultipartFile")
    @SaCheckPermission("web:album:upload")
    @PostMapping("/admin/album/upload")
    public Result<String> uploadAlbumCover(@RequestParam("file") MultipartFile file) {
        return Result.success(albumService.uploadAlbumCover(file));
    }

    @GetMapping("/album/list")
    public Result<List<AlbumResp>> listAlbumVO() {
        return Result.success(albumService.listAlbumVO());
    }

}