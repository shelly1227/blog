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
 * @TableName t_task
 */
@TableName(value ="t_task")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String taskName;

    private String taskGroup;

    private String invokeTarget;

    private String cronExpression;

    private Integer misfirePolicy;

    private Integer concurrent;

    private Integer status;

    private String remark;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}