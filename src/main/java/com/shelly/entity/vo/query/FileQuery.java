package com.shelly.entity.vo.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FileQuery extends PageQuery {

    /**
     * 文件路径
     */
    private String filePath;

}