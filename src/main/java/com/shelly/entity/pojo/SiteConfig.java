package com.shelly.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName t_site_config
 */
@TableName(value ="t_site_config")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteConfig implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String userAvatar;

    private String touristAvatar;

    private String siteName;

    private String siteAddress;

    private String siteIntro;

    private String siteNotice;

    private String createSiteTime;

    private String recordNumber;

    private String authorAvatar;

    private String siteAuthor;

    private String articleCover;

    private String aboutMe;

    private String github;

    private String gitee;

    private String bilibili;

    private String qq;

    private Integer commentCheck;

    private Integer messageCheck;

    private Integer isReward;

    private String weiXinCode;

    private String aliCode;

    private Integer emailNotice;

    private String socialList;

    private String loginList;

    private Integer isMusic;

    private String musicId;

    private Integer isChat;

    private String websocketUrl;
    @TableField(fill = FieldFill.INSERT)
    @JsonIgnore
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    @JsonIgnore
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}