package com.shelly.entity.vo.Response;

import lombok.Data;

import javax.validation.constraints.NotBlank;

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