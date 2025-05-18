package com.shelly.entity.vo.req;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class FriendReq {
    @NotBlank(message = "头像不能为空")
    String avatar;
    @NotBlank(message = "地址不能为空")
    String url;
    @NotBlank(message = "名不能为空")
    String name;
    @NotBlank(message = "颜色不能为空")
    String color;
    @NotBlank(message = "介绍不能为空")
    String introduction;
    Integer id;
}
