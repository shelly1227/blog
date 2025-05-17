package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.common.Result;
import com.shelly.entity.vo.Response.TagBackResp;
import com.shelly.entity.vo.Response.TagOptionResp;
import com.shelly.entity.vo.Request.TagReq;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.ArticleConditionQuery;
import com.shelly.entity.vo.Query.TagQuery;
import com.shelly.entity.vo.Response.ArticleConditionList;
import com.shelly.entity.vo.Response.TagResp;
import com.shelly.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    @PostMapping("/admin/tag/add")
    @SaCheckPermission("blog:tag:add")
    public Result<?> addTag(@Valid @RequestBody TagReq tag) {
        tagService.addTag(tag);
        return Result.success();
    }
    @DeleteMapping("/admin/tag/delete")
    @SaCheckPermission("blog:tag:delete")
    public Result<?> deleteTag(@RequestBody List<Integer> tagIdList) {
        tagService.deleteTag(tagIdList);
        return Result.success();
    }
    @GetMapping("/admin/tag/list")
    @SaCheckPermission("blog:tag:list")
    public Result<PageResult<TagBackResp>> listTag(TagQuery tagQuery) {
        return Result.success(tagService.listTag(tagQuery));
    }
    @GetMapping("/admin/tag/option")
    public Result<List<TagOptionResp>> listTagOption() {
        return Result.success(tagService.listTagOption());
    }
    @PutMapping("/admin/tag/update")
    @SaCheckPermission("blog:tag:update")
    public Result<?> updateTag(@Valid @RequestBody TagReq tag) {
        tagService.updateTag(tag);
        return Result.success();
    }
    @GetMapping("/tag/article")
    public Result<ArticleConditionList> listTagByArticle(ArticleConditionQuery articleConditionQuery) {
        return Result.success(tagService.listTagByArticle(articleConditionQuery));
    }
    @GetMapping("/tag/list")
    public Result<List<TagResp>> getTag() {
        return Result.success(tagService.getTag());
    }
}
