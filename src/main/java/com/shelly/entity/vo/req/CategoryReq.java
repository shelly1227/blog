package com.shelly.entity.vo.req;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data

public class CategoryReq {

    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名
     */
    @NotBlank(message = "分类名不能为空")
    private String categoryName;

}