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
 * @TableName t_carousel
 */
@TableName(value ="t_carousel")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Carousel implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String imgUrl;

    private Integer status;

    private String remark;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}