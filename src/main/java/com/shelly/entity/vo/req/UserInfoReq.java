package com.shelly.entity.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
@ApiModel(description = "用户信息Request")
public class UserInfoReq {

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty(value = "用户昵称", required = true)
    private String nickname;

    /**
     * 个人网站
     */
    @ApiModelProperty(value = "个人网站")
    private String webSite;

    /**
     * 个人简介
     */
    @ApiModelProperty(value = "个人简介")
    private String intro;
}
