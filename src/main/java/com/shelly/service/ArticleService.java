package com.shelly.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Article;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.ArticleQuery;
import com.shelly.entity.vo.query.PageQuery;
import com.shelly.entity.vo.req.ArticleReq;
import com.shelly.entity.vo.req.DeleteReq;
import com.shelly.entity.vo.req.RecommendReq;
import com.shelly.entity.vo.req.TopReq;
import com.shelly.entity.vo.res.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_article】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/

public interface ArticleService extends IService<Article> {

    PageResult<ArticleBackResp> listArticleBackVO(ArticleQuery articleQuery);

    void addArticle(ArticleReq article);

    void deleteArticle(List<Integer> articleIdList);

    void updateArticleDelete(DeleteReq delete);

    void updateArticle(ArticleReq article);

    ArticleInfoResp editArticle(Integer articleId);

    String saveArticleImages(MultipartFile file);

    void updateArticleTop(TopReq top);

    void updateArticleRecommend(RecommendReq recommend);

    List<ArticleSearchResp> listArticlesBySearch(String keyword);

    PageResult<ArticleHomeResp> listArticleHomeVO(PageQuery pageQuery);

    ArticleResp getArticleHomeById(Integer articleId);

    List<ArticleRecommendResp> listArticleRecommendVO();

    PageResult<ArchiveResp> listArchiveVO(PageQuery pageQuery);
}
