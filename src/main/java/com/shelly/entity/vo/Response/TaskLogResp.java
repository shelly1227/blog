package com.shelly.entity.vo.Response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskLogResp {

    /**
     * 任务日志id
     */

    private Integer id;

    /**
     * 任务名称
     */

    private String taskName;

    /**
     * 任务组名
     */

    private String taskGroup;

    /**
     * 调用目标
     */

    private String invokeTarget;

    /**
     * 日志信息
     */

    private String taskMessage;

    /**
     * 任务状态 (0失败 1成功)
     */

    private Integer status;

    /**
     * 错误信息
     */

    private String errorInfo;

    /**
     * 创建时间
     */

    private LocalDateTime createTime;

}
