package com.shelly.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName t_menu
 */
@TableName(value ="t_menu")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer parentId;

    private String menuType;

    private String menuName;

    private String path;

    private String icon;

    private String component;

    private String perms;

    private Integer isHidden;

    private Integer isDisable;

    private Integer orderNum;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}