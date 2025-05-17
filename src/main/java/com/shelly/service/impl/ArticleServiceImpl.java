package com.shelly.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shelly.constants.CommonConstant;
import com.shelly.constants.RedisConstant;

import com.shelly.entity.dto.CanalDTO;
import com.shelly.entity.pojo.*;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.ArticleQuery;
import com.shelly.entity.vo.Query.PageQuery;
import com.shelly.entity.vo.Request.ArticleReq;
import com.shelly.entity.vo.Request.DeleteReq;
import com.shelly.entity.vo.Request.RecommendReq;
import com.shelly.entity.vo.Request.TopReq;
import com.shelly.entity.vo.Response.*;
import com.shelly.enums.ArticleStatusEnum;
import com.shelly.enums.FilePathEnum;
import com.shelly.mapper.ArticleTagMapper;
import com.shelly.mapper.CategoryMapper;
import com.shelly.mapper.TagMapper;
import com.shelly.service.ArticleService;
import com.shelly.mapper.ArticleMapper;
import com.shelly.service.RedisService;
import com.shelly.strategy.context.SearchStrategyContext;
import com.shelly.strategy.context.UploadStrategyContext;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.shelly.constants.ElasticConstant.DELETE;
import static com.shelly.constants.ElasticConstant.INSERT;
import static com.shelly.constants.MqConstant.ARTICLE_EXCHANGE;
import static com.shelly.constants.MqConstant.ARTICLE_KEY;

/**
* @author Shelly6
* @description 针对表【t_article】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
    implements ArticleService{
    private final ArticleMapper articleMapper;
    private final RedisService redisService;
    private final CategoryMapper categoryMapper;
    private final ArticleTagMapper articleTagMapper;
    private final TagMapper tagMapper;
    private final com.shelly.service.TagService tagService;
    private final UploadStrategyContext uploadStrategyContext;
    private final BlogFileServiceImpl blogFileService;
    private final SearchStrategyContext searchStrategyContext;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public PageResult<ArticleBackResp> listArticleBackVO(ArticleQuery articleQuery) {
        // 查询文章数量
        Long count = articleMapper.selectBackArticleCount(articleQuery);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询文章后台信息
        List<ArticleBackResp> articleBackRespList = articleMapper.selectBackArticleList(articleQuery);
        // 浏览量
        Map<Object, Double> viewCountMap = redisService.getZsetAllScore(RedisConstant.ARTICLE_VIEW_COUNT);
        // 点赞量
        Map<String, Integer> likeCountMap = redisService.getHashAll(RedisConstant.ARTICLE_LIKE_COUNT);
        // 封装文章后台信息
        articleBackRespList.forEach(item -> {
            Double viewCount = Optional.ofNullable(viewCountMap.get(item.getId())).orElse((double) 0);
            item.setViewCount(viewCount.intValue());
            Integer likeCount = likeCountMap.get(item.getId().toString());
            item.setLikeCount(Optional.ofNullable(likeCount).orElse(0));
        });
        return new PageResult<>(articleBackRespList, count);
    }

    @Override
    public void addArticle(ArticleReq article) {
        // 保存文章分类
        Integer categoryId = saveArticleCategory(article);
        // 添加文章
        Article newArticle = new Article();
        BeanUtils.copyProperties(article, newArticle);
        if (StringUtils.isBlank(newArticle.getArticleCover())) {
            SiteConfig siteConfig = redisService.getObject(RedisConstant.SITE_SETTING);
            newArticle.setArticleCover(siteConfig.getArticleCover());
        }
        newArticle.setCategoryId(categoryId);
        newArticle.setUserId(StpUtil.getLoginIdAsInt());
        baseMapper.insert(newArticle);
        // 保存文章标签
        saveArticleTag(article, newArticle.getId());
        // 构建 CanalDTO 对象
        CanalDTO canalDTO = new CanalDTO();
        canalDTO.setIsDdl(false);
        canalDTO.setType(INSERT);
        // 将 article 对象转换为 Map 并添加到 data 列表中
        Map<String, Object> articleMap = BeanUtil.beanToMap(article);
        canalDTO.setData(Collections.singletonList(articleMap));

        // 将 CanalDTO 转换为 JSON 字符串并发送消息
        String message = JSONUtil.toJsonStr(canalDTO);
        rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE, ARTICLE_KEY, message);
        //
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticle(List<Integer> articleIdList) {
        // 删除文章标签
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, articleIdList));
        // 删除文章
        articleMapper.deleteBatchIds(articleIdList);
        // 构建 CanalDTO 对象
        CanalDTO canalDTO = new CanalDTO();
        canalDTO.setIsDdl(false);
        canalDTO.setType(DELETE);
        // 将 article 对象转换为 Map 并添加到 data 列表中
        Map<String, Object> articleMap = BeanUtil.beanToMap(articleIdList);
        canalDTO.setData(Collections.singletonList(articleMap));

        // 将 CanalDTO 转换为 JSON 字符串并发送消息
        String message = JSONUtil.toJsonStr(canalDTO);
        rabbitTemplate.convertAndSend(ARTICLE_EXCHANGE, ARTICLE_KEY, message);
    }

    @Override
    @Transactional
    public void updateArticleDelete(DeleteReq delete) {
        // 批量更新文章删除状态
        List<Article> articleList = delete.getIdList()
                .stream()
                .map(id -> Article.builder()
                        .id(id)
                        .isDelete(delete.getIsDelete())
                        .isTop(CommonConstant.FALSE)
                        .isRecommend(CommonConstant.FALSE)
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(articleList);
    }

    @Override
    public void updateArticle(ArticleReq article) {
        // 保存文章分类
        Integer categoryId = saveArticleCategory(article);
        // 修改文章
        Article newArticle = new Article();
        BeanUtils.copyProperties(article, newArticle);
        newArticle.setCategoryId(categoryId);
        newArticle.setUserId(StpUtil.getLoginIdAsInt());
        baseMapper.updateById(newArticle);
        // 保存文章标签
        saveArticleTag(article, newArticle.getId());
    }

    @Override
    public ArticleInfoResp editArticle(Integer articleId) {
        // 查询文章信息
        ArticleInfoResp articleInfoVO = articleMapper.selectArticleInfoById(articleId);
        Assert.notNull(articleInfoVO, "没有该文章");
        // 查询文章分类名称
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getCategoryName)
                .eq(Category::getId, articleInfoVO.getCategoryId()));
        // 查询文章标签名称
        List<String> tagNameList = tagMapper.selectTagNameByArticleId(articleId);
        articleInfoVO.setCategoryName(category.getCategoryName());
        articleInfoVO.setTagNameList(tagNameList);
        return articleInfoVO;
    }

    @Override
    public String saveArticleImages(MultipartFile file) {
        // 上传文件
        String url = uploadStrategyContext.executeUploadStrategy(file, FilePathEnum.ARTICLE.getPath());
        blogFileService.saveBlogFile(file, url, FilePathEnum.ARTICLE.getFilePath());
        return url;
    }

    @Override
    public void updateArticleTop(TopReq top) {
        // 修改文章置顶状态
        Article newArticle = Article.builder()
                .id(top.getId())
                .isTop(top.getIsTop())
                .build();
        articleMapper.updateById(newArticle);
    }

    @Override
    public void updateArticleRecommend(RecommendReq recommend) {
        // 修改文章推荐状态
        Article newArticle = Article.builder()
                .id(recommend.getId())
                .isRecommend(recommend.getIsRecommend())
                .build();
        articleMapper.updateById(newArticle);
    }

    @Override
    public List<ArticleSearchResp> listArticlesBySearch(String keyword) {
        return searchStrategyContext.executeSearchStrategy(keyword);
    }

    @Override
    public PageResult<ArticleHomeResp> listArticleHomeVO(PageQuery pageQuery) {
        // 查询文章数量
        Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, CommonConstant.FALSE)
                .eq(Article::getStatus, ArticleStatusEnum.PUBLIC.getStatus()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询首页文章
        List<ArticleHomeResp> articleHomeVOList = articleMapper.selectArticleHomeList(pageQuery);
        return new PageResult<>(articleHomeVOList, count);
    }

    @Override
    public ArticleResp getArticleHomeById(Integer articleId) {
        // 查询文章信息
        ArticleResp article = articleMapper.selectArticleHomeById(articleId);
        System.out.println("article = " + article);
        if (Objects.isNull(article)) {
            return null;
        }
        // 浏览量+1
        redisService.incrZet(RedisConstant.ARTICLE_VIEW_COUNT, articleId, 1D);
        // 查询上一篇文章
        ArticlePaginationResp lastArticle = articleMapper.selectLastArticle(articleId);
        // 查询下一篇文章
        ArticlePaginationResp nextArticle = articleMapper.selectNextArticle(articleId);
        article.setLastArticle(lastArticle);
        article.setNextArticle(nextArticle);
        // 查询浏览量
        Double viewCount = Optional.ofNullable(redisService.getZsetScore(RedisConstant.ARTICLE_VIEW_COUNT, articleId))
                .orElse((double) 0);
        article.setViewCount(viewCount.intValue());
        // 查询点赞量
        Integer likeCount = redisService.getHash(RedisConstant.ARTICLE_LIKE_COUNT, articleId.toString());
        article.setLikeCount(Optional.ofNullable(likeCount).orElse(0));
        return article;
    }

    @Override
    public List<ArticleRecommendResp> listArticleRecommendVO() {
        return articleMapper.selectArticleRecommend();
    }

    @Override
    public PageResult<ArchiveResp> listArchiveVO(PageQuery pageQuery) {
        // 查询文章数量
        Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, CommonConstant.FALSE)
                .eq(Article::getStatus, ArticleStatusEnum.PUBLIC.getStatus()));
        if (count == 0) {
            return new PageResult<>();
        }
        List<ArchiveResp> archiveList = articleMapper.selectArchiveList(pageQuery);
        return new PageResult<>(archiveList, count);
    }

    private void saveArticleTag(ArticleReq article, Integer articleId) {
        // 删除文章标签
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, articleId));
        // 标签名列表
        List<String> tagNameList = article.getTagNameList();
        if (CollectionUtils.isEmpty(tagNameList)) {
            return;
        }
        // 查询出已存在的标签
        List<Tag> existTagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId, Tag::getTagName)
                .in(Tag::getTagName, tagNameList));
        List<String> existTagNameList = existTagList.stream()
                .map(Tag::getTagName)
                .toList();
        List<Integer> existTagIdList = existTagList.stream()
                .map(Tag::getId)
                .collect(Collectors.toList());
        // 移除已存在的标签列表
        tagNameList.removeAll(existTagNameList);
        // 含有新标签
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 新标签列表
            List<Tag> newTagList = tagNameList.stream()
                    .map(item -> Tag.builder()
                            .tagName(item)
                            .build())
                    .collect(Collectors.toList());
            // 批量保存新标签
            tagService.saveBatch(newTagList);
            // 获取新标签id列表
            List<Integer> newTagIdList = newTagList.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
            // 新标签id添加到id列表
            existTagIdList.addAll(newTagIdList);
        }
        // 将所有的标签绑定到文章标签关联表
        articleTagMapper.saveBatchArticleTag(articleId, existTagIdList);
    }
    private Integer saveArticleCategory(ArticleReq article) {
        // 查询分类
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, article.getCategoryName()));
        // 分类不存在
        if (Objects.isNull(category)) {
            category = Category.builder()
                    .categoryName(article.getCategoryName())
                    .build();
            // 保存分类
            categoryMapper.insert(category);
        }
        return category.getId();
    }

}




