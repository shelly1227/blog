package com.shelly.entity.vo.Response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TagResp {

    /**
     * 标签id
     */

    private Integer id;

    /**
     * 标签名
     */

    private String tagName;

    /**
     * 文章数量
     */

    private Integer articleCount;
}