package com.shelly.entity.vo.res;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class CategoryResp {

    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名
     */
    @NotBlank(message = "分类名不能为空")
    private String categoryName;

    private Integer articleCount;

}