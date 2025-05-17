package com.shelly.entity.vo.Request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TagReq {
    Integer id;
    @NotBlank(message = "标签名不能为空")
    String tagName;
}
