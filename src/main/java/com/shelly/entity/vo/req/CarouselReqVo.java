package com.shelly.entity.vo.req;

import io.swagger.annotations.ApiModel;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@ApiModel(value = "轮播图Request")
public class CarouselReqVo {

    /**
     * 轮播图id
     */
    private Integer id;

    /**
     * 轮播图地址
     */
    @NotBlank(message = "imgUrl is null")
    private String imgUrl;

    /**
     * 是否显示 (0否 1是)
     */
    @NotNull(message = "status is null")
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}