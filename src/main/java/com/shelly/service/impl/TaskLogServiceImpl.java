package com.shelly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.shelly.entity.pojo.TaskLog;
import com.shelly.entity.vo.res.TaskLogResp;

import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.TaskQuery;
import com.shelly.service.TaskLogService;
import com.shelly.mapper.TaskLogMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
        Long count = lambdaQuery()
                .like(StringUtils.isNotBlank(taskQuery.getKeyword()), TaskLog::getTaskName, taskQuery.getKeyword())
                .eq(taskQuery.getStatus() != null, TaskLog::getStatus, taskQuery.getStatus())
                .like(StringUtils.isNotBlank(taskQuery.getTaskGroup()), TaskLog::getTaskGroup, taskQuery.getTaskGroup())
                .count();
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询定时任务日志列表
        LambdaQueryWrapper<TaskLog> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .like(StringUtils.isNotBlank(taskQuery.getKeyword()), TaskLog::getTaskName, taskQuery.getKeyword())
                .like(StringUtils.isNotBlank(taskQuery.getTaskGroup()), TaskLog::getTaskGroup, taskQuery.getTaskGroup())
                .eq(taskQuery.getStatus() != null, TaskLog::getStatus, taskQuery.getStatus())
                .orderByDesc(TaskLog::getCreateTime);

        Page<TaskLog> page = new Page<>(taskQuery.getCurrent(), taskQuery.getSize());
        Page<TaskLog> taskLogPage = taskLogMapper.selectPage(page, wrapper);

        List<TaskLogResp> taskLogRespList = taskLogPage.getRecords().stream()
                .map(taskLog -> {
                    TaskLogResp resp = new TaskLogResp();
                    BeanUtils.copyProperties(taskLog, resp);
                    return resp;
                })
                .toList();
        return new PageResult<>(taskLogRespList, count);

    }
}




