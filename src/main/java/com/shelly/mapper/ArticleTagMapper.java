package com.shelly.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.ArticleTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_article_tag】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.ArticleTag
*/
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

    void saveBatchArticleTag(@Param("articleId")Integer articleId,  List<Integer> tagIdList);
}




