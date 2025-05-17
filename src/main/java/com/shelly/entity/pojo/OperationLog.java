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
 * @TableName t_operation_log
 */
@TableName(value ="t_operation_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationLog implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String module;

    private String type;

    private String uri;

    private String name;

    private String description;

    private String params;

    private String method;

    private String data;

    private Integer userId;

    private String nickname;

    private String ipAddress;

    private String ipSource;

    private Integer times;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}