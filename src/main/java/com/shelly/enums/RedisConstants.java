package com.shelly.enums;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public enum RedisConstants {

    /**
     * 默认
     */
    DEFAULT("",10L, TimeUnit.SECONDS),
    /**
     * 用户常量
     */
    USER("User:", 1800L, TimeUnit.SECONDS),
    /**
     * 用户角色常量
     */
    USER_ROLE("User:role:", 1800L, TimeUnit.SECONDS),
    /**
     * 用户权限常量
     */
    USER_PERMISSION("User:permission:", 1800L, TimeUnit.SECONDS),

    RESUME_EXPERIENCE("Resume:experience:", 1800L, TimeUnit.SECONDS),

    RESUME_SKILL("Resume:skill:", 1800L, TimeUnit.SECONDS),

    SITE_SETTING("site_setting", 1L, TimeUnit.DAYS),

    /**
     * 用户点赞常量
     */

    USER_COURSE_LIKE("User:courseLike:", 3600L,TimeUnit.SECONDS),
    /**
     * 邮箱验证码常量
     */
    EMAIL_CODE("Email:code:", 300L, TimeUnit.SECONDS),
    /**
     * 手机验证码常量
     */
    PHONE_CODE("Phone:code:", 300L, TimeUnit.SECONDS),
    /**
     * 验证码常量
     */

    VERIFICATION_CODE("Verification:code:", 300L, TimeUnit.SECONDS),

    USER_TOKEN("User:token:", 3L,TimeUnit.DAYS ),
    USER_INFO("User:info:",3L ,TimeUnit.DAYS ),
    STOP_WORD_CONTENT("Word:stopWord:list:", -1L, TimeUnit.SECONDS),
    JOB_APPLICATION_COUNT("Job:application:count",600L , TimeUnit.SECONDS),
    USER_MENU("User:menu:", 1800L, TimeUnit.SECONDS),
    ARTICLE_LIKE_COUNT("article_like_count",-1L, TimeUnit.SECONDS),
    BLOG_VIEW_COUNT("blog_view_count",-1L, TimeUnit.SECONDS),
    UNIQUE_VISITOR("unique_visitor",-1L, TimeUnit.SECONDS),
    TALK_LIKE_COUNT("talk_like_count",-1L, TimeUnit.SECONDS ),

    /**
     * 用户点赞文章
     */
    USER_ARTICLE_LIKE ("user_article_like:",-1L, TimeUnit.SECONDS),

    /**
     * 用户点赞评论
     */
    USER_COMMENT_LIKE("user_comment_like:",-1L, TimeUnit.SECONDS),

    /**
     * 用户点赞说说
     */
    USER_TALK_LIKE("user_talk_like:",-1L, TimeUnit.SECONDS),
    COMMENT_LIKE_COUNT ("comment_like_count", -1L, TimeUnit.SECONDS),
    ARTICLE_VIEW_COUNT("article_view_count", -1L, TimeUnit.SECONDS );

    /**
     * 键
     */
    private final String key;
    /**
     * 过期时间
     */
    private final Long ttl;

    /**
     * 时间单位
     */
    private final TimeUnit timeUnit;

    RedisConstants(String key, Long ttl, TimeUnit timeUnit) {
        this.key = key;
        this.ttl = ttl;
        this.timeUnit = timeUnit;
    }

}
