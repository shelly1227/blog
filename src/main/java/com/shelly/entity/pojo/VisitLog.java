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
 * @TableName t_visit_log
 */
@TableName(value ="t_visit_log")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitLog implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String page;

    private String ipAddress;

    private String ipSource;

    private String os;

    private String browser;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}