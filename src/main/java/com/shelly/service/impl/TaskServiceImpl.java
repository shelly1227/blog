package com.shelly.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shelly.entity.pojo.Task;
import com.shelly.service.TaskService;
import com.shelly.mapper.TaskMapper;
import org.springframework.stereotype.Service;

/**
* @author Shelly6
* @description 针对表【t_task】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task>
    implements TaskService {

}




