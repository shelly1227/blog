package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.PhotoQuery;
import com.shelly.entity.vo.Request.PhotoInfoReq;
import com.shelly.entity.vo.Request.PhotoReq;
import com.shelly.entity.vo.Response.AlbumBackResp;
import com.shelly.entity.vo.Response.PhotoBackResp;
import com.shelly.service.PhotoService;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PhotoController {
    private final PhotoService photoService;
    @PostMapping("/admin/photo/add")
    @SaCheckPermission("web:photo:add")
    public Result<?> addPhoto(@Valid @RequestBody PhotoReq photo) {
        photoService.addPhoto(photo);
        return Result.success();
    }
    @GetMapping("/admin/photo/album/{albumId}/info")
    @SaCheckPermission("web:photo:list")
    public Result<AlbumBackResp> getAlbumInfo(@PathVariable("albumId") Integer albumId) {
        return Result.success(photoService.getAlbumInfo(albumId));
    }
    @DeleteMapping("/admin/photo/delete")
    @SaCheckPermission("web:photo:delete")
    public Result<?> deletePhoto(@RequestBody List<Integer> photoIdList) {
        photoService.deletePhoto(photoIdList);
        return Result.success();
    }
    @GetMapping("/admin/photo/list")
    @SaCheckPermission("web:photo:list")
    public Result<PageResult<PhotoBackResp>> listPhoto(PhotoQuery photoQuery) {
        return Result.success(photoService.listPhoto(photoQuery));
    }
    @PutMapping("/admin/photo/move")
    @SaCheckPermission("web:photo:move")
    public Result<?> updatePhoto(@Valid @RequestBody PhotoReq photo) {
        photoService.updatePhoto(photo);
        return Result.success();
    }
    @PutMapping("/admin/photo/update")
    @SaCheckPermission("web:photo:update")
    public Result<?> updatePhotoAlbum(@Valid @RequestBody PhotoInfoReq photoInfo) {
        photoService.updatePhotoAlbum(photoInfo);
        return Result.success();
    }
    @PostMapping("/admin/photo/upload")
    @SaCheckPermission("web:photo:upload")
    @ApiImplicitParam(name = "file", value = "照片", required = true, dataType = "MultipartFile")
    public Result<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        return Result.success(photoService.uploadPhoto(file));
    }
    @GetMapping("/photo/list")
    public Result<Map<String, Object>> listPublicPhoto(@RequestParam Integer albumId) {
        return Result.success(photoService.listPublicPhoto(albumId));
    }
}
