package com.shelly.strategy;

import com.shelly.entity.vo.Response.ArticleSearchResp;

import java.util.List;

public interface SearchStrategy {

    /**
     * 搜索文章
     *
     * @param keyword 关键字
     * @return {@link List<  ArticleSearchResp  >} 文章列表
     */
    List<ArticleSearchResp> searchArticle(String keyword);
}
