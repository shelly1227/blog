package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.OptLogger;
import com.shelly.annotation.VisitLogger;
import com.shelly.common.Result;
import com.shelly.entity.vo.res.TagBackResp;
import com.shelly.entity.vo.res.TagOptionResp;
import com.shelly.entity.vo.req.TagReq;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.ArticleConditionQuery;
import com.shelly.entity.vo.query.TagQuery;
import com.shelly.entity.vo.res.ArticleConditionList;
import com.shelly.entity.vo.res.TagResp;
import com.shelly.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shelly.constants.OptTypeConstant.*;

@RestController
@Tag(name = "标签模块")
@RequiredArgsConstructor
//CHECKED
public class TagController {
    private final TagService tagService;
    @PostMapping("/admin/tag/add")
    @Operation(summary = "添加标签")
    @SaCheckPermission("blog:tag:add")
    @OptLogger(value = ADD)
    public Result<?> addTag(@Valid @RequestBody TagReq tag) {
        tagService.addTag(tag);
        return Result.success();
    }
    @DeleteMapping("/admin/tag/delete")
    @SaCheckPermission("blog:tag:delete")
    @Operation(summary = "删除标签")
    @OptLogger(value = DELETE)
    public Result<?> deleteTag(@RequestBody List<Integer> tagIdList) {
        tagService.deleteTag(tagIdList);
        return Result.success();
    }
    @GetMapping("/admin/tag/list")
    @SaCheckPermission("blog:tag:list")
    @Operation(summary = "获取标签列表")
    public Result<PageResult<TagBackResp>> listTag(TagQuery tagQuery) {
        return Result.success(tagService.listTag(tagQuery));
    }
    @GetMapping("/admin/tag/option")
    @Operation(summary = "获取标签选项")
    public Result<List<TagOptionResp>> listTagOption() {
        return Result.success(tagService.listTagOption());
    }
    @PutMapping("/admin/tag/update")
    @SaCheckPermission("blog:tag:update")
    @OptLogger(value = UPDATE)
    @Operation(summary = "修改标签")
    public Result<?> updateTag(@Valid @RequestBody TagReq tag) {
        tagService.updateTag(tag);
        return Result.success();
    }
    @GetMapping("/tag/article")
    @Operation(summary = "获取标签下的文章")
    @VisitLogger(value = "标签文章")
    public Result<ArticleConditionList> listTagByArticle(ArticleConditionQuery articleConditionQuery) {
        return Result.success(tagService.listTagByArticle(articleConditionQuery));
    }
    @GetMapping("/tag/list")
    @VisitLogger(value = "文章标签")
    @Operation(summary = "获取标签列表")
    public Result<List<TagResp>> getTag() {
        return Result.success(tagService.getTag());
    }
}
