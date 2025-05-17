package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.AccessLimit;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.ArticleQuery;
import com.shelly.entity.vo.Query.PageQuery;
import com.shelly.entity.vo.Request.ArticleReq;
import com.shelly.entity.vo.Request.DeleteReq;
import com.shelly.entity.vo.Request.RecommendReq;
import com.shelly.entity.vo.Request.TopReq;
import com.shelly.entity.vo.Response.*;
import com.shelly.enums.LikeTypeEnum;
import com.shelly.service.ArticleService;
import com.shelly.strategy.context.LikeStrategyContext;
import io.swagger.annotations.ApiImplicitParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {


    private final ArticleService articleService;


    private final LikeStrategyContext likeStrategyContext;



    @SaCheckPermission("blog:article:list")
    @GetMapping("/admin/article/list")
    public Result<PageResult<ArticleBackResp>> listArticleBackVO(ArticleQuery articleQuery) {
        return Result.success(articleService.listArticleBackVO(articleQuery));
    }


    @SaCheckPermission("blog:article:add")
    @PostMapping("/admin/article/add")
    public Result<?> addArticle(@Validated @RequestBody ArticleReq article) {
        articleService.addArticle(article);
        return Result.success();
    }


    @SaCheckPermission("blog:article:delete")
    @DeleteMapping("/admin/article/delete")
    public Result<?> deleteArticle(@RequestBody List<Integer> articleIdList) {
        articleService.deleteArticle(articleIdList);
        return Result.success();
    }


    @SaCheckPermission("blog:article:recycle")
    @PutMapping("/admin/article/recycle")
    public Result<?> updateArticleDelete(@Validated @RequestBody DeleteReq delete) {
        articleService.updateArticleDelete(delete);
        return Result.success();
    }


    @SaCheckPermission("blog:article:update")
    @PutMapping("/admin/article/update")
    public Result<?> updateArticle(@Validated @RequestBody ArticleReq article) {
        articleService.updateArticle(article);
        return Result.success();
    }


    @SaCheckPermission("blog:article:edit")
    @GetMapping("/admin/article/edit/{articleId}")
    public Result<ArticleInfoResp> editArticle(@PathVariable("articleId") Integer articleId) {
        return Result.success(articleService.editArticle(articleId));
    }


    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
    @SaCheckPermission("blog:article:upload")
    @PostMapping("/admin/article/upload")
    public Result<String> saveArticleImages(@RequestParam("file") MultipartFile file) {
        return Result.success(articleService.saveArticleImages(file));
    }


    @SaCheckPermission("blog:article:top")
    @PutMapping("/admin/article/top")
    public Result<?> updateArticleTop(@Validated @RequestBody TopReq top) {
        articleService.updateArticleTop(top);
        return Result.success();
    }

    @SaCheckPermission("blog:article:recommend")
    @PutMapping("/admin/article/recommend")
    public Result<?> updateArticleRecommend(@Validated @RequestBody RecommendReq recommend) {
        articleService.updateArticleRecommend(recommend);
        return Result.success();
    }


    @SaCheckLogin
    @AccessLimit(seconds = 60, maxCount = 3)
    @SaCheckPermission("blog:article:like")
    @PostMapping("/article/{articleId}/like")
    public Result<?> likeArticle(@PathVariable("articleId") Integer articleId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.ARTICLE, articleId);
        return Result.success();
    }


    @GetMapping("/article/search")
    public Result<List<ArticleSearchResp>> listArticlesBySearch(String keyword) {
        return Result.success(articleService.listArticlesBySearch(keyword));
    }


    @GetMapping("/article/list")
    public Result<PageResult<ArticleHomeResp>> listArticleHomeVO(PageQuery pageQuery) {
        return Result.success(articleService.listArticleHomeVO(pageQuery));
    }


    @GetMapping("/article/{articleId}")
    public Result<ArticleResp> getArticleHomeById(@PathVariable("articleId") Integer articleId) {
        return Result.success(articleService.getArticleHomeById(articleId));
    }


    @GetMapping("/article/recommend")
    public Result<List<ArticleRecommendResp>> listArticleRecommendVO() {
        return Result.success(articleService.listArticleRecommendVO());
    }


    @GetMapping("/archives/list")
    public Result<PageResult<ArchiveResp>> listArchiveVO(PageQuery pageQuery) {
        return Result.success(articleService.listArchiveVO(pageQuery));
    }

}