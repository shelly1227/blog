package com.shelly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shelly.common.ServiceException;
import com.shelly.entity.pojo.Article;
import com.shelly.entity.pojo.Category;
import com.shelly.entity.vo.res.CategoryBackResp;
import com.shelly.entity.vo.res.CategoryResp;
import com.shelly.entity.vo.res.CategoryOptionResp;
import com.shelly.entity.vo.query.ArticleConditionQuery;
import com.shelly.entity.vo.query.CategoryQuery;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.req.CategoryReq;
import com.shelly.entity.vo.res.ArticleConditionList;
import com.shelly.entity.vo.res.ArticleConditionResp;
import com.shelly.mapper.ArticleMapper;
import com.shelly.service.CategoryService;
import com.shelly.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
* @author Shelly6
* @description 针对表【t_category】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    private final CategoryMapper categoryMapper;

    private final ArticleMapper articleMapper;
    private static final int maxDeep = 3;
    @Override
    public void addCategory(CategoryReq category) {
        // 分类是否存在
        Category existCategory = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, category.getCategoryName()));
        if (existCategory != null) {
            throw new ServiceException("分类已存在");
        }
        Category newCategory = Category.builder()
                .categoryName(category.getCategoryName())
                .build();
        categoryMapper.insert(newCategory);
    }

    @Override
    public void deleteCategory(List<Integer> categoryIdList) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Article::getCategoryId, categoryIdList);
        if (articleMapper.selectCount(wrapper) > 0) {
            throw new IllegalArgumentException("该分类下存在文章，无法删除");
        }
        categoryMapper.deleteByIds(categoryIdList);
    }

    @Override
    public PageResult<CategoryBackResp> listCategory(CategoryQuery categoryQuery) {
        LambdaQueryWrapper <Category> wrapper = new LambdaQueryWrapper<>();
wrapper.like(StringUtils.hasText(categoryQuery.getKeyword()),Category::getCategoryName, categoryQuery.getKeyword());
               if(categoryMapper.selectCount(wrapper)  ==  0){
                   return new PageResult<>();
               }
        //查出所有的分类id，无论父级子级
        List<CategoryBackResp> categoryBackResp = categoryMapper.selectBackCategoryList(categoryQuery);
        // 搜集当前分类id列表
        Set<Integer> categoryIdList = categoryBackResp.stream()
                .map(CategoryBackResp::getId)
                .collect(Collectors.toSet());

        List<CategoryBackResp> res = categoryBackResp.stream()
                .map(category -> {
                    Integer parentId = category.getParentId();
                    // parentId不在当前分类id列表，说明为父级分类id，根据此id作为递归的开始条件节点
                    if (!categoryIdList.contains(parentId)) {
                        categoryIdList.add(parentId);
                        return recurCategoryList(categoryBackResp, parentId, 0, maxDeep);
                    }
                    //注意，这里的两次返回，只会进入一个，是父级ID返回所有子节点，否则返回null
                    return new ArrayList<CategoryBackResp>();
                    //类似与收集森林的每棵树
                }).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
        // 执行分页
        //第一个元素在整个列表中的索引
        int fromIndex = categoryQuery.getCurrent();
        int size = categoryQuery.getSize();
        int toIndex = categoryBackResp.size() - fromIndex > size ? fromIndex + size : res.size();
        return new PageResult<>(res.subList(fromIndex, toIndex), (long) res.size());
    }

    @Override
    public List<CategoryOptionResp> listCategoryOption() {
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .orderByDesc(Category::getId));
        // 创建 CategoryOptionResp 列表
        List<CategoryOptionResp> categoryOptionRespList = new ArrayList<>();
        // 遍历 categoryList 并复制属性
        for (Category category : categoryList) {
            CategoryOptionResp dto = new CategoryOptionResp();
            BeanUtils.copyProperties(category, dto);
            categoryOptionRespList.add(dto);
        }
        return categoryOptionRespList;
    }

    @Override
    public void updateCategory(CategoryReq category) {
        Category existCategory  =  categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, category.getCategoryName()));
        //修改分类名称，不与已存在的相同
        if (Objects.nonNull(existCategory) && !existCategory.getId().equals(category.getId())) {
            throw new IllegalArgumentException(category.getCategoryName() + "分类已存在");
        }
            Category newCategory = Category.builder()
                    .id(category.getId())
                    .categoryName(category.getCategoryName())
                    .build();
        baseMapper.updateById(newCategory);
    }

    @Override
    public ArticleConditionList listCategoryByArticle(ArticleConditionQuery articleQuery) {
        log.info(articleQuery.toString());
        List<ArticleConditionResp> articleConditionList = articleMapper.selectArticleListByCondition(articleQuery);
        log.info("articleConditionList:" + articleConditionList.toString());
        String name = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                        .select(Category::getCategoryName)
                        .eq(Category::getId, articleQuery.getCategoryId()))
                .getCategoryName();
        return ArticleConditionList.builder()
                .articleConditionVOList(articleConditionList)
                .name(name)
                .build();
    }

    @Override
    public List<CategoryResp> listCategoryVO() {
        return categoryMapper.listCategoryVO();
    }



    // 递归获取子分类
    private List<CategoryBackResp> recurCategoryList(List<CategoryBackResp> categoryList, Integer parentId, int currentDeep, int maxDeep) {
        List<CategoryBackResp> tree = new ArrayList<>();
        if (maxDeep < 0) {
            return tree;
        }
        if (currentDeep == maxDeep) {
            return tree;
        } else {
            for (CategoryBackResp category : categoryList) {
                if (category.getParentId().equals(parentId)) {
                    category.setChildren(recurCategoryList(categoryList, category.getId(), currentDeep + 1, maxDeep));
                    tree.add(category);
                }
            }
        }
        return tree;
    }
}




