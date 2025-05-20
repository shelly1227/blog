package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.OptLogger;
import com.shelly.common.Result;
import com.shelly.constants.OptTypeConstant;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.CarouselQuery;
import com.shelly.entity.vo.req.CarouselReqVo;
import com.shelly.entity.vo.req.CarouselStatusReq;
import com.shelly.entity.vo.res.CarouselBackResp;
import com.shelly.entity.vo.res.CarouselResp;
import com.shelly.service.CarouselService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "轮播图模块")
//checked but file upload
public class CarouselController {

    private final CarouselService carouselService;


    @GetMapping("/carousel/list")
    @Operation(summary = "获取轮播图列表")
    public Result<List<CarouselResp>> getCarouselList() {
        return Result.success(carouselService.getCarouselList());
    }

    @SaCheckPermission("web:carousel:list")
    @GetMapping("/admin/carousel/list")
    @Operation(summary = "获取后台轮播图列表")
    public Result<PageResult<CarouselBackResp>> getCarouselVOList(CarouselQuery carouselQuery) {
        return Result.success(carouselService.getCarouselVOList(carouselQuery));
    }


    @ApiImplicitParam(name = "file", value = "轮播图片", required = true, dataType = "MultipartFile")
    @SaCheckPermission("web:carousel:upload")
    @PostMapping("/admin/carousel/upload")
    @OptLogger(value = OptTypeConstant.UPLOAD)
    @Operation(summary = "上传轮播图")
    public Result<String> uploadCarousel(@RequestParam("file") MultipartFile file) {
        return Result.success(carouselService.uploadCarousel(file));
    }


    @SaCheckPermission("web:carousel:add")
    @Operation(summary = "添加轮播图")
    @OptLogger(value = OptTypeConstant.ADD)
    @PostMapping("/admin/carousel/add")
    public Result<?> addCarousel(@Validated @RequestBody CarouselReqVo carouselReqVo) {
        carouselService.addCarousel(carouselReqVo);
        return Result.success();
    }



    @SaCheckPermission("web:carousel:update")
    @Operation(summary = "修改轮播图")
    @OptLogger(value = OptTypeConstant.UPDATE)
    @PostMapping("/admin/carousel/update")
    public Result<?> updateCarousel(@Validated @RequestBody CarouselReqVo carouselReqVo) {
        carouselService.updateCarousel(carouselReqVo);
        return Result.success();
    }


    @SaCheckPermission("web:carousel:delete")
    @OptLogger(value = OptTypeConstant.DELETE)
    @Operation(summary = "删除轮播图")
    @DeleteMapping("/admin/carousel/delete")
    public Result<?> deleteCarousel(@RequestBody List<Integer> carouselIdList) {
        carouselService.removeByIds(carouselIdList);
        return Result.success();
    }


    @SaCheckPermission("web:carousel:status")
    @OptLogger(value = OptTypeConstant.UPDATE)
    @Operation(summary = "修改轮播图状态")
    @PutMapping("/admin/carousel/status")
    public Result<?> updateCarouselStatus(@Validated @RequestBody CarouselStatusReq carouselStatusReq) {
        carouselService.updateCarouselStatus(carouselStatusReq);
        return Result.success();
    }

}