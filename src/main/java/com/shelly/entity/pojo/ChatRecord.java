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
 * @TableName t_chat_record
 */
@TableName(value ="t_chat_record")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRecord implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String nickname;

    private String avatar;

    private String content;

    private String ipAddress;

    private String ipSource;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;
}