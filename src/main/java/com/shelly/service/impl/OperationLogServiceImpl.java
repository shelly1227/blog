package com.shelly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shelly.entity.pojo.OperationLog;
import com.shelly.entity.vo.res.OperationLogResp;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.LogQuery;
import com.shelly.service.OperationLogService;
import com.shelly.mapper.OperationLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_operation_log】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog>
    implements OperationLogService{
    private final OperationLogMapper operationLogMapper;

    @Override
    public PageResult<OperationLogResp> listExceptionLogs(LogQuery logQuery) {
        // 查询操作日志数量
        Long count = operationLogMapper.selectCount(new LambdaQueryWrapper<OperationLog>()
                .like(StringUtils.hasText(logQuery.getOptModule()), OperationLog::getModule, logQuery.getOptModule())
                .or()
                .like(StringUtils.hasText(logQuery.getKeyword()), OperationLog::getDescription, logQuery.getKeyword())
        );
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询操作日志列表
        List<OperationLogResp> operationLogRespList = operationLogMapper.selectOperationLogVOList(logQuery);
        return new PageResult<>(operationLogRespList, count);
    }

    @Override
    public void saveOperationLog(OperationLog operationLog) {
        // 保存操作日志
        operationLogMapper.insert(operationLog);
    }
}




