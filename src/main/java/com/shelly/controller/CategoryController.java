package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.OptLogger;
import com.shelly.annotation.VisitLogger;
import com.shelly.common.Result;
import com.shelly.entity.vo.res.CategoryBackResp;
import com.shelly.entity.vo.res.CategoryResp;
import com.shelly.entity.vo.res.CategoryOptionResp;
import com.shelly.entity.vo.query.ArticleConditionQuery;
import com.shelly.entity.vo.query.CategoryQuery;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.req.CategoryReq;
import com.shelly.entity.vo.res.ArticleConditionList;
import com.shelly.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.shelly.constants.OptTypeConstant.*;

/**
 * 分类管理
 * @author shelly
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "分类模块")
public class CategoryController {
    private final CategoryService categoryService;
    /**
     *admin添加分类
     */
    @PostMapping("/admin/category/add")
    @OptLogger(value = ADD)
    @Operation(summary = "添加分类")
    @SaCheckPermission("blog:category:add")
    public Result<String> addCategory(@Valid @RequestBody CategoryReq category) {
        categoryService.addCategory(category);
        return Result.success();
    }
    @DeleteMapping("/admin/category/delete")
    @SaCheckPermission("blog:category:delete")
    @Operation(summary = "删除分类")
    @OptLogger(value = DELETE)
    public Result<?> deleteCategory(@RequestBody List<Integer> categoryIdList) {
        categoryService.deleteCategory(categoryIdList);
        return Result.success();
    }
    @GetMapping("/admin/category/list")
    @SaCheckPermission("blog:category:list")
    @Operation(summary = "获取分类列表")
    public Result<PageResult<CategoryBackResp>> listCategory(CategoryQuery categoryQuery) {
        return Result.success(categoryService.listCategory(categoryQuery));
    }
    @GetMapping("/admin/category/option")
    @Operation(summary = "获取分类下拉列表")
    public Result<List<CategoryOptionResp>> listCategoryOption() {
        return Result.success(categoryService.listCategoryOption());
    }
    @PutMapping("/admin/category/update")
    @OptLogger(value = UPDATE)
    @Operation(summary = "修改分类")
    @SaCheckPermission("blog:category:update")
    public Result<String> updateCategory(@Valid @RequestBody CategoryReq category) {
        categoryService.updateCategory(category);
        return Result.success();
    }
    @GetMapping("/category/article")
    @VisitLogger(value = "分类文章")
    @Operation(summary = "根据分类id获取文章列表")
    public Result<ArticleConditionList> listCategoryByArticle(@Validated ArticleConditionQuery articleQuery) {
        return Result.success(categoryService.listCategoryByArticle(articleQuery));
    }
    @GetMapping("/category/list")
    @VisitLogger(value = "文章分类")
    @Operation(summary = "获取分类列表")
    public Result<List<CategoryResp>> listCategory() {
        return Result.success(categoryService.listCategoryVO());
    }
}
