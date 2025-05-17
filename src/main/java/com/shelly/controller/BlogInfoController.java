package com.shelly.controller;

import com.shelly.common.Result;
import com.shelly.entity.vo.Response.BlogBackInfoResp;
import com.shelly.entity.vo.Response.BlogInfoResp;
import com.shelly.service.BlogInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "博客模块")
@RestController
@RequiredArgsConstructor
public class BlogInfoController {
    private final BlogInfoService blogInfoService;

    /**
     * 上传访客信息
     *
     * @return {@link Result<>}
     */
    @ApiOperation(value = "上传访客信息")
    @PostMapping("/report")
    public Result<?> report() {
        blogInfoService.report();
        return Result.success();
    }


    @GetMapping("/")
    public Result<BlogInfoResp> getBlogInfo() {
        return Result.success(blogInfoService.getBlogInfo());
    }


    @GetMapping("/admin")
    public Result<BlogBackInfoResp> getBlogBackInfo() {
        return Result.success(blogInfoService.getBlogBackInfo());
    }

    /**
     * 查看关于我信息
     *
     * @return {@link Result<String>} 关于我信息
     */

    @ApiOperation(value = "查看关于我信息")
    @GetMapping("/about")
    public Result<String> getAbout() {
        return Result.success(blogInfoService.getAbout());
    }
}