package com.shelly.strategy.impl;

import com.shelly.constants.ElasticConstant;
import com.shelly.entity.vo.res.ArticleSearchResp;
import com.shelly.mapper.ArticleMapper;
import com.shelly.strategy.SearchStrategy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("mySqlSearchStrategyImpl")
public class MysqlSearchStrategyImpl implements SearchStrategy {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<ArticleSearchResp> searchArticle(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return new ArrayList<>();
        }
        List<ArticleSearchResp> articleSearchRespList = articleMapper.searchArticle(keyword);
        return articleSearchRespList.stream()
                .map(article -> {
                    // 获取关键词第一次出现的位置
                    String articleContent = article.getArticleContent();
                    int index = articleContent.indexOf(keyword);
                    if (index != -1) {
                        // 获取关键词前面的文字
                        int preIndex = index > 25 ? index - 25 : 0;
                        String preText = article.getArticleContent().substring(preIndex, index);
                        // 获取关键词到后面的文字
                        int last = index + keyword.length();
                        int postLength = article.getArticleContent().length() - last;
                        int postIndex = postLength > 175 ? last + 175 : last + postLength;
                        String postText = article.getArticleContent().substring(index, postIndex);
                        // 文章内容高亮
                        articleContent = (preText + postText).replaceAll(keyword, ElasticConstant.PRE_TAG + keyword + ElasticConstant.POST_TAG);
                    }
                    // 文章标题高亮
                    String articleTitle = article.getArticleTitle().replaceAll(keyword, ElasticConstant.PRE_TAG + keyword + ElasticConstant.POST_TAG);
                    return ArticleSearchResp.builder()
                            .id(article.getId())
                            .articleTitle(articleTitle)
                            .articleContent(articleContent)
                            .build();
                })
                .toList();
    }
}
