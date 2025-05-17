package com.shelly.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName t_article_tag
 */
@TableName(value ="t_article_tag")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTag implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer articleId;

    private Integer tagId;

    private static final long serialVersionUID = 1L;
}