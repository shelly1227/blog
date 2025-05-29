package com.shelly.controller;

import com.shelly.annotation.VisitLogger;
import com.shelly.common.Result;
import com.shelly.entity.vo.res.BlogBackInfoResp;
import com.shelly.entity.vo.res.BlogInfoResp;
import com.shelly.service.impl.BlogInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "博客信息模块")
@RestController
@RequiredArgsConstructor
public class BlogInfoController {
    private final BlogInfoService blogInfoService;

    /**
     * 上传访客信息
     *
     * @return {@link Result<>}
     */
    @Operation(summary = "上传访客信息")
    @PostMapping("/report")
    public Result<?> report() {
        System.out.println("上传访客信息");
        blogInfoService.report();
        return Result.success();
    }


    @GetMapping("/")
    @Operation(summary = "获取博客信息")
    public Result<BlogInfoResp> getBlogInfo() {
        return Result.success(blogInfoService.getBlogInfo());
    }


    @GetMapping("/admin")
    @Operation(summary = "获取后台信息")
    public Result<BlogBackInfoResp> getBlogBackInfo() {
        return Result.success(blogInfoService.getBlogBackInfo());
    }

    /**
     * 查看关于我信息
     *
     * @return {@link Result<String>} 关于我信息
     */

    @Operation(summary = "查看关于我信息")
    @GetMapping("/about")
    @VisitLogger(value = "关于")
    public Result<String> getAbout() {
        return Result.success(blogInfoService.getAbout());
    }
}