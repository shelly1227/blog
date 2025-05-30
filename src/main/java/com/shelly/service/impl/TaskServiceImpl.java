package com.shelly.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shelly.common.ServiceException;
import com.shelly.constants.ScheduleConstant;
import com.shelly.entity.pojo.Task;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.TaskQuery;
import com.shelly.entity.vo.req.StatusReq;
import com.shelly.entity.vo.req.TaskReq;
import com.shelly.entity.vo.req.TaskRunReq;
import com.shelly.entity.vo.res.TaskBackResp;
import com.shelly.enums.TaskStatusEnum;
import com.shelly.service.TaskService;
import com.shelly.mapper.TaskMapper;
import com.shelly.utils.CronUtils;
import com.shelly.utils.ScheduleUtils;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_task】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
    implements TaskService {
    @Autowired
    private Scheduler scheduler;

    @Autowired
    private TaskMapper taskMapper;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理
     * 注：不能手动修改数据库ID和任务组名，否则会导致脏数据
     */
    @PostConstruct
    public void init() throws SchedulerException {
        scheduler.clear();
        List<Task> taskList = taskMapper.selectList(null);
        for (Task task : taskList) {
            // 创建定时任务
            ScheduleUtils.createScheduleJob(scheduler, task);
        }
    }

    public PageResult<TaskBackResp> listTaskBackVO(TaskQuery taskQuery) {
        // 查询定时任务数量
        Long count = lambdaQuery()
                .select(Task::getId)
                .like(StringUtils.isBlank(taskQuery.getKeyword()), Task::getTaskName, taskQuery.getKeyword())
                .eq(taskQuery.getStatus() != null, Task::getStatus,taskQuery.getStatus())
                .like(StringUtils.isNotBlank(taskQuery.getTaskGroup()), Task::getTaskGroup, taskQuery.getTaskGroup())
                .count();
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询定时任务列表
        List<TaskBackResp> taskBackRespList = lambdaQuery()
                .like(StringUtils.isBlank(taskQuery.getKeyword()), Task::getTaskName, taskQuery.getKeyword())
                .eq(taskQuery.getStatus() != null, Task::getStatus,taskQuery.getStatus())
                .like(StringUtils.isNotBlank(taskQuery.getTaskGroup()), Task::getTaskGroup, taskQuery.getTaskGroup())
                .orderByDesc(Task::getId)
                .page(new Page<>(taskQuery.getCurrent(), taskQuery.getSize())) // 核心：分页
                .getRecords() // 获取分页后的数据
                .stream()
                .map(task -> {
                    TaskBackResp resp = new TaskBackResp();
                    BeanUtils.copyProperties(task, resp);
                    return resp;
                })
                .toList();

        taskBackRespList.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getCronExpression())) {
                Date nextExecution = CronUtils.getNextExecution(item.getCronExpression());
                item.setNextValidTime(nextExecution);
            } else {
                item.setNextValidTime(null);
            }
        });
        return new PageResult<>(taskBackRespList, count);
    }

    public void addTask(TaskReq task) {
        Assert.isTrue(CronUtils.isValid(task.getCronExpression()), "Cron表达式无效");
        Task newTask = new Task();
        BeanUtils.copyProperties(task, newTask);
        // 新增定时任务
        int row = taskMapper.insert(newTask);
        // 创建定时任务
        if (row > 0) {
            ScheduleUtils.createScheduleJob(scheduler, newTask);
        }
    }

    public void updateTask(TaskReq task) {
        Assert.isTrue(CronUtils.isValid(task.getCronExpression()), "Cron表达式无效");
        Task existTask = taskMapper.selectById(task.getId());
        Task newTask = new Task();
        BeanUtils.copyProperties(task, newTask);
        // 更新定时任务
        int row = taskMapper.updateById(newTask);
        if (row > 0) {
            try {
                updateSchedulerJob(newTask, existTask.getTaskGroup());
            } catch (SchedulerException e) {
                throw new ServiceException("更新定时任务异常");
            }
        }
    }

    public void deleteTask(List<Integer> taskIdList) {
        List<Task> taskList = taskMapper.selectList(new LambdaQueryWrapper<Task>()
                .select(Task::getId, Task::getTaskGroup)
                .in(Task::getId, taskIdList));
        // 删除定时任务
        int row = taskMapper.delete(new LambdaQueryWrapper<Task>().in(Task::getId, taskIdList));
        if (row > 0) {
            taskList.forEach(task -> {
                try {
                    scheduler.deleteJob(ScheduleUtils.getJobKey(task.getId(), task.getTaskGroup()));
                } catch (SchedulerException e) {
                    throw new ServiceException("删除定时任务异常");
                }
            });
        }
    }

    public void updateTaskStatus(StatusReq taskStatus) {
        Task task = taskMapper.selectById(taskStatus.getId());
        // 相同不用更新
        if (task.getStatus().equals(taskStatus.getStatus())) {
            return;
        }
        // 更新数据库中的定时任务状态
        Task newTask = Task.builder()
                .id(taskStatus.getId())
                .status(taskStatus.getStatus())
                .build();
        int row = taskMapper.updateById(newTask);
        // 获取定时任务状态、id、任务组
        Integer status = taskStatus.getStatus();
        Integer taskId = task.getId();
        String taskGroup = task.getTaskGroup();
        if (row > 0) {
            // 更新定时任务
            try {
                if (TaskStatusEnum.RUNNING.getStatus().equals(status)) {
                    scheduler.resumeJob(ScheduleUtils.getJobKey(taskId, taskGroup));
                }
                if (TaskStatusEnum.PAUSE.getStatus().equals(status)) {
                    scheduler.pauseJob(ScheduleUtils.getJobKey(taskId, taskGroup));
                }
            } catch (SchedulerException e) {
                throw new ServiceException("更新定时任务状态异常");
            }
        }
    }

    public void runTask(TaskRunReq taskRun) {
        Integer taskId = taskRun.getId();
        String taskGroup = taskRun.getTaskGroup();
        // 查询定时任务
        Task task = taskMapper.selectById(taskRun.getId());
        // 设置参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstant.TASK_PROPERTIES, task);
        try {
            scheduler.triggerJob(ScheduleUtils.getJobKey(taskId, taskGroup), dataMap);
        } catch (SchedulerException e) {
            throw new ServiceException("执行定时任务异常");
        }
    }

    /**
     * 更新任务
     *
     * @param task      任务对象
     * @param taskGroup 任务组名
     */
    public void updateSchedulerJob(Task task, String taskGroup) throws SchedulerException {
        Integer taskId = task.getId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(taskId, taskGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, task);
    }
}




