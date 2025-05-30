package com.shelly.entity.vo.res;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "文章Response")
public class ArticleResp {

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private Integer id;

    /**
     * 文章缩略图
     */
    @ApiModelProperty(value = "文章缩略图")
    private String articleCover;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    /**
     * 文章内容
     */
    @ApiModelProperty(value = "文章内容")
    private String articleContent;

    /**
     * 文章类型 (1原创 2转载 3翻译)
     */
    @ApiModelProperty(value = "文章类型 (1原创 2转载 3翻译)")
    private Integer articleType;

    /**
     * 浏览量
     */
    @ApiModelProperty(value = "浏览量")
    private Integer viewCount;

    /**
     * 点赞量
     */
    @ApiModelProperty(value = "点赞量")
    private Integer likeCount;

    /**
     * 文章分类
     */
    @ApiModelProperty(value = "文章分类")
    private CategoryOptionResp category;

    /**
     * 文章标签
     */
    @ApiModelProperty(value = "文章标签")
    private List<TagOptionResp> tagVOList;

    /**
     * 上一篇文章
     */
    @ApiModelProperty(value = "上一篇文章")
    private ArticlePaginationResp lastArticle;

    /**
     * 下一篇文章
     */
    @ApiModelProperty(value = "下一篇文章")
    private ArticlePaginationResp nextArticle;

    /**
     * 发表时间
     */
    @ApiModelProperty(value = "发表时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}