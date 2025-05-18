package com.shelly.entity.vo.req;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class AlbumReq {

    /**
     * 相册id
     */

    private Integer id;

    /**
     * 相册名
     */
    @NotBlank(message = "相册名不能为空")
    private String albumName;

    /**
     * 相册描述
     */
    @ApiModelProperty(value = "相册描述")
    private String albumDesc;

    /**
     * 相册封面
     */
    @NotBlank(message = "相册封面不能为空")
    private String albumCover;


    private Integer status;
}