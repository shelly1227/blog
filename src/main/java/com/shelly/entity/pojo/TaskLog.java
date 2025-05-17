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
 * @TableName t_task_log
 */
@TableName(value ="t_task_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskLog implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String taskName;

    private String taskGroup;

    private String invokeTarget;

    private String taskMessage;

    private Integer status;

    private String errorInfo;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}