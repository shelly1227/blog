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
 * @TableName t_friend
 */
@TableName(value ="t_friend")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Friend implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String color;

    private String avatar;

    private String url;

    private String introduction;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}