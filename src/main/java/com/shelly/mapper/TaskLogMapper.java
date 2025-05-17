package com.shelly.mapper;

import com.github.houbb.heaven.annotation.reflect.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.TaskLog;
import com.shelly.entity.vo.Response.TaskLogResp;
import com.shelly.entity.vo.Query.TaskQuery;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_task_log】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.TaskLog
*/
public interface TaskLogMapper extends BaseMapper<TaskLog> {

    Long selectTaskLogCount(@Param("param") TaskQuery taskQuery);

    List<TaskLogResp> selectTaskLogRespList(@Param("param")TaskQuery taskQuery);
}




