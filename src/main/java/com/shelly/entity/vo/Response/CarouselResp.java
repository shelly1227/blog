package com.shelly.entity.vo.Response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "轮播图Response")
public class CarouselResp {

    /**
     * 轮播图id
     */
    @ApiModelProperty(value = "轮播图id")
    private Integer id;

    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String imgUrl;

}