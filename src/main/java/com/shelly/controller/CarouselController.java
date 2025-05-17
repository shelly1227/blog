package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.CarouselQuery;
import com.shelly.entity.vo.Request.CarouselReqVo;
import com.shelly.entity.vo.Request.CarouselStatusReq;
import com.shelly.entity.vo.Response.CarouselBackResp;
import com.shelly.entity.vo.Response.CarouselResp;
import com.shelly.service.CarouselService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CarouselController {


    private final CarouselService carouselService;


    @GetMapping("/carousel/list")
    public Result<List<CarouselResp>> getCarouselList() {
        return Result.success(carouselService.getCarouselList());
    }

    @SaCheckPermission("web:carousel:list")
    @GetMapping("/admin/carousel/list")
    public Result<PageResult<CarouselBackResp>> getCarouselVOList(CarouselQuery carouselQuery) {
        return Result.success(carouselService.getCarouselVOList(carouselQuery));
    }


    @ApiImplicitParam(name = "file", value = "轮播图片", required = true, dataType = "MultipartFile")
    @SaCheckPermission("web:carousel:upload")
    @PostMapping("/admin/carousel/upload")
    public Result<String> uploadCarousel(@RequestParam("file") MultipartFile file) {
        return Result.success(carouselService.uploadCarousel(file));
    }


    @SaCheckPermission("web:carousel:add")
    @PostMapping("/admin/carousel/add")
    public Result<?> addCarousel(@Validated @RequestBody CarouselReqVo carouselReqVo) {
        carouselService.addCarousel(carouselReqVo);
        return Result.success();
    }



    @SaCheckPermission("web:carousel:update")
    @PostMapping("/admin/carousel/update")
    public Result<?> updateCarousel(@Validated @RequestBody CarouselReqVo carouselReqVo) {
        carouselService.updateCarousel(carouselReqVo);
        return Result.success();
    }


    @SaCheckPermission("web:carousel:delete")
    @DeleteMapping("/admin/carousel/delete")
    public Result<?> deleteCarousel(@RequestBody List<Integer> carouselIdList) {
        carouselService.removeByIds(carouselIdList);
        return Result.success();
    }


    @SaCheckPermission("web:carousel:status")
    @PutMapping("/admin/carousel/status")
    public Result<?> updateCarouselStatus(@Validated @RequestBody CarouselStatusReq carouselStatusReq) {
        carouselService.updateCarouselStatus(carouselStatusReq);
        return Result.success();
    }

}