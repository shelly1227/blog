package com.shelly.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName t_album
 */
@TableName(value ="t_album")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String albumName;

    private String albumCover;

    private String albumDesc;

    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}