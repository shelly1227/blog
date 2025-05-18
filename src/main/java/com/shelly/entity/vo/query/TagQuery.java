package com.shelly.entity.vo.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TagQuery extends PageQuery {

    /**
     * 搜索内容
     */
    private String keyword;

}