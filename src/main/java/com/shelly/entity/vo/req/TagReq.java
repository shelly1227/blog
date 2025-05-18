package com.shelly.entity.vo.req;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class TagReq {
    Integer id;
    @NotBlank(message = "标签名不能为空")
    String tagName;
}
