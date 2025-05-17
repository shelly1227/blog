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
 * @TableName t_photo
 */
@TableName(value ="t_photo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer albumId;

    private String photoName;

    private String photoDesc;

    private String photoUrl;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}