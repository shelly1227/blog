package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.OptLogger;
import com.shelly.annotation.VisitLogger;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.PhotoQuery;
import com.shelly.entity.vo.req.PhotoInfoReq;
import com.shelly.entity.vo.req.PhotoReq;
import com.shelly.entity.vo.res.AlbumBackResp;
import com.shelly.entity.vo.res.PhotoBackResp;
import com.shelly.service.PhotoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

import static com.shelly.constants.OptTypeConstant.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "图片模块")
public class PhotoController {
    private final PhotoService photoService;
    @PostMapping("/admin/photo/add")
    @SaCheckPermission("web:photo:add")
    @Operation(summary = "添加图片")
    @OptLogger(value = ADD)
    public Result<?> addPhoto(@Valid @RequestBody PhotoReq photo) {
        photoService.addPhoto(photo);
        return Result.success();
    }
    @GetMapping("/admin/photo/album/{albumId}/info")
    @SaCheckPermission("web:photo:list")
    @Operation(summary = "获取相册信息")
    public Result<AlbumBackResp> getAlbumInfo(@PathVariable("albumId") Integer albumId) {
        return Result.success(photoService.getAlbumInfo(albumId));
    }
    @DeleteMapping("/admin/photo/delete")
    @SaCheckPermission("web:photo:delete")
    @OptLogger(value = DELETE)
    @Operation(summary = "删除图片")
    public Result<?> deletePhoto(@RequestBody List<Integer> photoIdList) {
        photoService.deletePhoto(photoIdList);
        return Result.success();
    }
    @GetMapping("/admin/photo/list")
    @SaCheckPermission("web:photo:list")
    @Operation(summary = "获取图片列表")
    public Result<PageResult<PhotoBackResp>> listPhoto(PhotoQuery photoQuery) {
        return Result.success(photoService.listPhoto(photoQuery));
    }
    @PutMapping("/admin/photo/move")
    @OptLogger(value = UPDATE)
    @Operation(summary = "移动图片相册")
    @SaCheckPermission("web:photo:move")
    public Result<?> updatePhoto(@Valid @RequestBody PhotoReq photo) {
        photoService.updatePhoto(photo);
        return Result.success();
    }
    @PutMapping("/admin/photo/update")
    @Operation(summary = "修改图片信息")
    @SaCheckPermission("web:photo:update")
    @OptLogger(value = UPDATE)
    public Result<?> updatePhotoAlbum(@Valid @RequestBody PhotoInfoReq photoInfo) {
        photoService.updatePhotoAlbum(photoInfo);
        return Result.success();
    }
    @PostMapping("/admin/photo/upload")
    @OptLogger(value = UPLOAD)
    @Operation(summary = "上传图片")
    @SaCheckPermission("web:photo:upload")
    @ApiImplicitParam(name = "file", value = "照片", required = true, dataType = "MultipartFile")
    public Result<String> uploadPhoto(@RequestParam("file") MultipartFile file) {
        return Result.success(photoService.uploadPhoto(file));
    }
    @GetMapping("/photo/list")
    @VisitLogger(value = "照片")
    @Operation(summary = "获取公开图片列表")
    public Result<Map<String, Object>> listPublicPhoto(@RequestParam Integer albumId) {
        return Result.success(photoService.listPublicPhoto(albumId));
    }
}
