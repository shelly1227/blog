package com.shelly.entity.vo.Response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleConditionResp {

    /**
     * 文章id
     */

    private Integer id;

    /**
     * 文章缩略图
     */

    private String articleCover;

    /**
     * 文章标题
     */

    private String articleTitle;

    /**
     * 文章分类
     */

    private CategoryOptionResp category;

    /**
     * 文章标签
     */

    private List<TagOptionResp> tagVOList;

    /**
     * 发表时间
     */

    private LocalDateTime createTime;

}
