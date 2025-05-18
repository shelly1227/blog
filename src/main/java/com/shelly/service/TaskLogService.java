package com.shelly.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.shelly.entity.pojo.TaskLog;
import com.shelly.entity.vo.res.TaskLogResp;

import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.TaskQuery;

/**
* @author Shelly6
* @description 针对表【t_task_log】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface TaskLogService extends IService<TaskLog> {

    void clearTaskLog();

    PageResult<TaskLogResp> listLogs(TaskQuery taskQuery);
}
