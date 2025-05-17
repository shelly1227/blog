package com.shelly.entity.vo.Query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "文章条件")
public class ArticleConditionQuery extends PageQuery {

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id", required = true)
    private Integer categoryId;

    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id", required = true)
    private Integer tagId;

}