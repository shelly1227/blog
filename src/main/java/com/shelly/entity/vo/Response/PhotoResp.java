package com.shelly.entity.vo.Response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "照片Response")
public class PhotoResp {

    /**
     * 照片id
     */
    @ApiModelProperty(value = "照片id")
    private Integer id;

    /**
     * 照片链接
     */
    @ApiModelProperty(value = "照片链接")
    private String photoUrl;

}