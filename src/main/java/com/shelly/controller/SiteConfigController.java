package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.OptLogger;
import com.shelly.common.Result;

import com.shelly.entity.pojo.SiteConfig;
import com.shelly.service.SiteConfigService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.shelly.constants.OptTypeConstant.UPDATE;
import static com.shelly.constants.OptTypeConstant.UPLOAD;

@RestController
@RequiredArgsConstructor
@Tag(name = "站点配置模块")
public class SiteConfigController {

    private final SiteConfigService siteConfigService;

    /**
     * 获取网站配置
     *
     * @return {@link Result<  SiteConfig  >} 网站配置
     */

    @SaCheckPermission("web:site:list")
    @GetMapping("/admin/site/list")
    @Operation(summary = "获取网站配置")
    public Result<SiteConfig> getSiteConfig() {
        return Result.success(siteConfigService.getSiteConfig());
    }

    /**
     * 更新网站配置
     *
     * @param siteConfig 网站配置
     * @return {@link Result<>}
     */

    @SaCheckPermission("web:site:update")
    @PutMapping("/admin/site/update")
    @Operation(summary = "更新网站配置")
    @OptLogger(value = UPDATE)
    public Result<?> updateSiteConfig(@RequestBody SiteConfig siteConfig) {
        siteConfigService.updateSiteConfig(siteConfig);
        return Result.success();
    }

    /**
     * 上传网站配置图片
     *
     * @param file 图片
     * @return {@link Result<String>} 图片路径
     */

    @ApiImplicitParam(name = "file", value = "配置图片", required = true, dataType = "MultipartFile")
    @SaCheckPermission("web:site:upload")
    @Operation(summary = "上传网站配置图片")
    @OptLogger(value = UPLOAD)
    @PostMapping("/admin/site/upload")
    public Result<String> uploadSiteImg(@RequestParam("file") MultipartFile file) {
        return Result.success(siteConfigService.uploadSiteImg(file));
    }

}