package com.shelly.entity.vo.Query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TaskQuery extends PageQuery {

    /**
     * 搜索内容
     */

    private String keyword;

    /**
     * 任务状态 (0运行 1暂停)
     */

    private Integer status;

    /**
     * 任务组名
     */

    private String taskGroup;
}