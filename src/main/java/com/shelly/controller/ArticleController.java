package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.AccessLimit;
import com.shelly.annotation.OptLogger;
import com.shelly.annotation.VisitLogger;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.ArticleQuery;
import com.shelly.entity.vo.query.PageQuery;
import com.shelly.entity.vo.req.ArticleReq;
import com.shelly.entity.vo.req.DeleteReq;
import com.shelly.entity.vo.req.RecommendReq;
import com.shelly.entity.vo.req.TopReq;
import com.shelly.entity.vo.res.*;
import com.shelly.enums.LikeTypeEnum;
import com.shelly.service.ArticleService;
import com.shelly.strategy.context.LikeStrategyContext;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

import static com.shelly.constants.OptTypeConstant.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "文章模块")
public class ArticleController {

    private final ArticleService articleService;


    private final LikeStrategyContext likeStrategyContext;



    @SaCheckPermission("blog:article:list")
    @GetMapping("/admin/article/list")
    @Operation(summary = "获取后台文章列表")
    public Result<PageResult<ArticleBackResp>> listArticleBackVO(ArticleQuery articleQuery) {
        return Result.success(articleService.listArticleBackVO(articleQuery));
    }


    @SaCheckPermission("blog:article:add")
    @PostMapping("/admin/article/add")
    @Operation(summary = "添加文章")
    @OptLogger(value = ADD)
    public Result<?> addArticle(@Validated @RequestBody ArticleReq article) {
        articleService.addArticle(article);
        return Result.success();
    }


    @SaCheckPermission("blog:article:delete")
    @DeleteMapping("/admin/article/delete")
    @OptLogger(value = DELETE)
    @Operation(summary = "删除文章")
    public Result<?> deleteArticle(@RequestBody List<Integer> articleIdList) {
        articleService.deleteArticle(articleIdList);
        return Result.success();
    }


    @SaCheckPermission("blog:article:recycle")
    @PutMapping("/admin/article/recycle")
    @Operation(summary = "回收文章")
    @OptLogger(value = UPDATE)
    public Result<?> updateArticleDelete(@Validated @RequestBody DeleteReq delete) {
        articleService.updateArticleDelete(delete);
        return Result.success();
    }


    @SaCheckPermission("blog:article:update")
    @PutMapping("/admin/article/update")
    @OptLogger(value = UPDATE)
    @Operation(summary = "修改文章")
    public Result<?> updateArticle(@Validated @RequestBody ArticleReq article) {
        articleService.updateArticle(article);
        return Result.success();
    }


    @SaCheckPermission("blog:article:edit")
    @GetMapping("/admin/article/edit/{articleId}")
    @Operation(summary = "编辑文章")
    public Result<ArticleInfoResp> editArticle(@PathVariable("articleId") Integer articleId) {
        return Result.success(articleService.editArticle(articleId));
    }


    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
    @SaCheckPermission("blog:article:upload")
    @PostMapping("/admin/article/upload")
    @OptLogger(value = UPLOAD)
    @Operation(summary = "上传文章图片")
    public Result<String> saveArticleImages(@RequestParam("file") MultipartFile file) {
        return Result.success(articleService.saveArticleImages(file));
    }


    @SaCheckPermission("blog:article:top")
    @PutMapping("/admin/article/top")
    @OptLogger(value = UPDATE)
    @Operation(summary = "修改文章置顶状态")
    public Result<?> updateArticleTop(@Validated @RequestBody TopReq top) {
        articleService.updateArticleTop(top);
        return Result.success();
    }

    @SaCheckPermission("blog:article:recommend")
    @OptLogger(value = UPDATE)
    @PutMapping("/admin/article/recommend")
    @Operation(summary = "修改文章推荐状态")
    public Result<?> updateArticleRecommend(@Validated @RequestBody RecommendReq recommend) {
        articleService.updateArticleRecommend(recommend);
        return Result.success();
    }


    @SaCheckLogin
    @AccessLimit(seconds = 60, maxCount = 3)
    @SaCheckPermission("blog:article:like")
    @PostMapping("/article/{articleId}/like")
    @Operation(summary = "点赞文章")
    public Result<?> likeArticle(@PathVariable("articleId") Integer articleId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.ARTICLE, articleId);
        return Result.success();
    }


    @GetMapping("/article/search")
    @Operation(summary = "搜索文章")
    public Result<List<ArticleSearchResp>> listArticlesBySearch(String keyword) {
        return Result.success(articleService.listArticlesBySearch(keyword));
    }


    @GetMapping("/article/list")
    @Operation(summary = "获取文章列表")
    @VisitLogger(value = "首页")
    public Result<PageResult<ArticleHomeResp>> listArticleHomeVO(PageQuery pageQuery) {
        return Result.success(articleService.listArticleHomeVO(pageQuery));
    }


    @GetMapping("/article/{articleId}")
    @VisitLogger(value = "文章")
    @Operation(summary = "获取文章详情")
    public Result<ArticleResp> getArticleHomeById(@PathVariable("articleId") Integer articleId) {
        return Result.success(articleService.getArticleHomeById(articleId));
    }


    @GetMapping("/article/recommend")
    @Operation(summary = "获取推荐文章")
    public Result<List<ArticleRecommendResp>> listArticleRecommendVO() {
        return Result.success(articleService.listArticleRecommendVO());
    }


    @GetMapping("/archives/list")
    @VisitLogger(value = "归档")
    @Operation(summary = "获取文章归档")
    public Result<PageResult<ArchiveResp>> listArchiveVO(PageQuery pageQuery) {
        return Result.success(articleService.listArchiveVO(pageQuery));
    }

}