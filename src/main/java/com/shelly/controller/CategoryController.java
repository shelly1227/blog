package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.common.Result;
import com.shelly.entity.vo.Response.CategoryBackResp;
import com.shelly.entity.vo.Response.CategoryResp;
import com.shelly.entity.vo.Response.CategoryOptionResp;
import com.shelly.entity.vo.Query.ArticleConditionQuery;
import com.shelly.entity.vo.Query.CategoryQuery;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Request.CategoryReq;
import com.shelly.entity.vo.Response.ArticleConditionList;
import com.shelly.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ description: 分类管理
 * @ author: shelly
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    /**
     *admin添加分类
     */
    @PostMapping("/admin/category/add")
    @SaCheckPermission("blog:category:add")
    public Result<String> addCategory(@Valid @RequestBody CategoryReq category) {
        categoryService.addCategory(category);
        return Result.success();
    }
    @DeleteMapping("/admin/category/delete")
    @SaCheckPermission("blog:category:delete")
    public Result<?> deleteCategory(@RequestBody List<Integer> categoryIdList) {
        categoryService.deleteCategory(categoryIdList);
        return Result.success();
    }
    @GetMapping("/admin/category/list")
    @SaCheckPermission("blog:category:list")
    public Result<PageResult<CategoryBackResp>> listCategory(CategoryQuery categoryQuery) {
        return Result.success(categoryService.listCategory(categoryQuery));
    }
    @GetMapping("/admin/category/option")
    public Result<List<CategoryOptionResp>> listCategoryOption() {
        return Result.success(categoryService.listCategoryOption());
    }
    @PutMapping("/admin/category/update")
    @SaCheckPermission("blog:category:update")
    public Result<String> updateCategory(@Valid @RequestBody CategoryReq category) {
        categoryService.updateCategory(category);
        return Result.success();
    }
    @GetMapping("/category/article")
    public Result<ArticleConditionList> listCategoryByArticle(@Validated ArticleConditionQuery articleQuery) {
        return Result.success(categoryService.listCategoryByArticle(articleQuery));
    }
    @GetMapping("/category/list")
    public Result<List<CategoryResp>> listCategory() {
        return Result.success(categoryService.listCategoryVO());
    }
}
