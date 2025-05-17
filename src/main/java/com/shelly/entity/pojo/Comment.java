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
 * @TableName t_comment
 */
@TableName(value ="t_comment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer commentType;

    private Integer typeId;

    private Integer parentId;

    private Integer replyId;

    private String commentContent;

    private Integer fromUid;

    private Integer toUid;

    private Integer isCheck;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}