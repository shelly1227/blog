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
 * @TableName t_blog_file
 */
@TableName(value ="t_blog_file")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogFile implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String fileUrl;

    private String fileName;

    private Integer fileSize;

    private String extendName;

    private String filePath;

    private Integer isDir;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}