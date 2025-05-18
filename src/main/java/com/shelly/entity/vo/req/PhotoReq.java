package com.shelly.entity.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Data
@ApiModel(description = "照片Request")
public class PhotoReq {

    /**
     * 相册id
     */
    @NotNull(message = "相册id不能为空")
    @ApiModelProperty(value = "相册id", required = true)
    private Integer albumId;

    /**
     * 照片链接
     */
    @ApiModelProperty(value = "照片链接")
    private List<String> photoUrlList;

    /**
     * 照片id
     */
    @ApiModelProperty(value = "照片id")
    private List<Integer> photoIdList;
}