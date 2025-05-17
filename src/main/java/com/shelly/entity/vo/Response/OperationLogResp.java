package com.shelly.entity.vo.Response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OperationLogResp {

    /**
     * 操作日志id
     */

    private Integer id;

    /**
     * 操作模块
     */

    private String module;

    /**
     * 操作uri
     */

    private String uri;

    /**
     * 操作类型
     */

    private String type;


    private String name;

    /**
     * 操作描述
     */

    private String description;

    /**
     * 请求方式
     */

    private String method;



    private String params;

    private String data;

    /**
     * 用户昵称
     */

    private String nickname;

    /**
     * 操作ip
     */

    private String ipAddress;

    /**
     * 操作地址
     */

    private String ipSource;

    /**
     * 操作耗时 (毫秒)
     */

    private Long times;

    /**
     * 创建时间
     */

    private LocalDateTime createTime;

}