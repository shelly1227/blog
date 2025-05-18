package com.shelly.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.OperationLog;
import com.shelly.entity.vo.res.OperationLogResp;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.LogQuery;

/**
* @author Shelly6
* @description 针对表【t_operation_log】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface OperationLogService extends IService<OperationLog> {

    PageResult<OperationLogResp> listExceptionLogs(LogQuery logQuery);
}
