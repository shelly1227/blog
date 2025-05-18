package com.shelly.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.Article;
import com.shelly.entity.vo.query.ArticleConditionQuery;
import com.shelly.entity.vo.query.ArticleQuery;
import com.shelly.entity.vo.query.PageQuery;
import com.shelly.entity.vo.res.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_article】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.Article
*/
public interface ArticleMapper extends BaseMapper<Article> {

    List<ArticleConditionResp> selectArticleConditionList(@Param("articleQuery") ArticleConditionQuery articleQuery);

    List<ArticleConditionResp> selectArticleListByCondition(@Param("param") ArticleConditionQuery tagQuery);

    Long selectBackArticleCount(@Param("param") ArticleQuery articleQuery);

    List<ArticleBackResp> selectBackArticleList(@Param("param") ArticleQuery articleQuery);

    ArticleInfoResp selectArticleInfoById(@Param("articleId") Integer articleId);

    List<ArticleHomeResp> selectArticleHomeList(@Param("param") PageQuery pageQuery);

    ArticleResp selectArticleHomeById(Integer articleId);

    ArticlePaginationResp selectLastArticle(Integer articleId);

    ArticlePaginationResp selectNextArticle(Integer articleId);

    List<ArticleRecommendResp> selectArticleRecommend();

    List<ArchiveResp> selectArchiveList(@Param("param") PageQuery pageQuery);

    List<ArticleSearchResp> searchArticle(@Param("keyword")String keyword);

    List<ArticleStatisticsResp> selectArticleStatistics();
}




