package com.shelly.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Category;
import com.shelly.entity.vo.Response.CategoryBackResp;
import com.shelly.entity.vo.Response.CategoryOptionResp;
import com.shelly.entity.vo.Response.CategoryResp;
import com.shelly.entity.vo.Query.ArticleConditionQuery;
import com.shelly.entity.vo.Query.CategoryQuery;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Request.CategoryReq;
import com.shelly.entity.vo.Response.ArticleConditionList;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_category】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface CategoryService extends IService<Category> {

    void addCategory(CategoryReq category);

    void deleteCategory(List<Integer> categoryIdList);

    PageResult<CategoryBackResp> listCategory(CategoryQuery categoryQuery);

    List<CategoryOptionResp> listCategoryOption();

    void updateCategory(CategoryReq category);

    ArticleConditionList listCategoryByArticle(ArticleConditionQuery articleQuery);

    List<CategoryResp> listCategoryVO();


}
