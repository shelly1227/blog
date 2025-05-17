package com.shelly.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LikeTypeEnum {

    /**
     * 文章
     */
    ARTICLE("文章", "articleLikeStrategyImpl"),

    /**
     * 评论
     */
    COMMENT("评论", "commentLikeStrategyImpl"),

    /**
     * 说说
     */
    TALK("说说", "talkLikeStrategyImpl");

    /**
     * 点赞类型
     */
    private final String likeType;

    /**
     * 策略
     */
    private final String strategy;

}
