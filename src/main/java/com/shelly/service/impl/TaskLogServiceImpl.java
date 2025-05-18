package com.shelly.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.shelly.entity.pojo.TaskLog;
import com.shelly.entity.vo.res.TaskLogResp;

import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.TaskQuery;
import com.shelly.service.TaskLogService;
import com.shelly.mapper.TaskLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_task_log】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@RequiredArgsConstructor
public class TaskLogServiceImpl extends ServiceImpl<TaskLogMapper, TaskLog>
    implements TaskLogService {

    private final TaskLogMapper taskLogMapper;
    @Override
    public void clearTaskLog() {
        taskLogMapper.delete(null);
    }

    @Override
    public PageResult<TaskLogResp> listLogs(TaskQuery taskQuery) {
        // 查询定时任务日志数量
        Long count = taskLogMapper.selectTaskLogCount(taskQuery);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询定时任务日志列表
        List<TaskLogResp> taskLogRespList = taskLogMapper.selectTaskLogRespList(taskQuery);
        return new PageResult<>(taskLogRespList, count);
    }
}




