package com.shelly.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Task;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.TaskQuery;
import com.shelly.entity.vo.req.StatusReq;
import com.shelly.entity.vo.req.TaskReq;
import com.shelly.entity.vo.req.TaskRunReq;
import com.shelly.entity.vo.res.TaskBackResp;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_task】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface TaskService extends IService<Task> {

    PageResult<TaskBackResp> listTaskBackVO(TaskQuery taskQuery);

    void addTask(TaskReq task);

    void updateTask(TaskReq task);

    void deleteTask(List<Integer> taskIdList);

    void updateTaskStatus(StatusReq taskStatus);

    void runTask(TaskRunReq taskRun);
}
