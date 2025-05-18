package com.shelly.entity.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
@ApiModel(description = "说说Request")
public class TalkReq {

    /**
     * 说说id
     */
    @ApiModelProperty(value = "说说id")
    private Integer id;

    /**
     * 说说内容
     */
    @NotBlank(message = "说说内容不能为空")
    @ApiModelProperty(value = "说说内容", required = true)
    private String talkContent;

    /**
     * 说说图片
     */
    @ApiModelProperty(value = "说说图片")
    private String images;

    /**
     * 是否置顶 (0否 1是)
     */
    @NotNull(message = "置顶状态不能为空")
    @ApiModelProperty(value = "是否置顶 (0否 1是)", required = true)
    private Integer isTop;

    /**
     * 说说状态 (1公开 2私密)
     */
    @NotNull(message = "说说状态不能为空")
    @ApiModelProperty(value = "说说状态 (1公开 2私密)", required = true)
    private Integer status;

}