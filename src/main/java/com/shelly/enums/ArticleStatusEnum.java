package com.shelly.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArticleStatusEnum {

    /**
     * 公开
     */
    PUBLIC(1, "公开"),

    /**
     * 私密
     */
    SECRET(2, "私密"),

    /**
     * 草稿
     */
    DRAFT(3, "草稿");

    /**
     * 状态
     */
    private final Integer status;

    /**
     * 描述
     */
    private final String description;
}
