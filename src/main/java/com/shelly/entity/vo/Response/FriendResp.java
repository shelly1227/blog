package com.shelly.entity.vo.Response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FriendResp {

    /**
     * 友链id
     */

    private Integer id;

    /**
     * 友链颜色
     */

    private String color;

    /**
     * 友链名称
     */

    private String name;

    /**
     * 友链头像
     */

    private String avatar;

    /**
     * 友链地址
     */

    private String url;

    /**
     * 友链介绍
     */

    private String introduction;

}