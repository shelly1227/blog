package com.shelly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shelly.entity.pojo.ExceptionLog;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.LogQuery;
import com.shelly.service.ExceptionLogService;
import com.shelly.mapper.ExceptionLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_exception_log】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@RequiredArgsConstructor
public class ExceptionLogServiceImpl extends ServiceImpl<ExceptionLogMapper, ExceptionLog>
    implements ExceptionLogService{
    private final ExceptionLogMapper exceptionLogMapper;

    @Override
    public PageResult<ExceptionLog> listExceptionLogs(LogQuery logQuery) {
        // 查询异常日志数量
        Long count = exceptionLogMapper.selectCount(new LambdaQueryWrapper<ExceptionLog>()
                .like(StringUtils.hasText(logQuery.getOptModule()), ExceptionLog::getModule, logQuery.getOptModule())
                .or()
                .like(StringUtils.hasText(logQuery.getKeyword()), ExceptionLog::getDescription, logQuery.getKeyword())
        );
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询异常日志列表
        List<ExceptionLog> operationLogVOList = exceptionLogMapper.selectExceptionLogList(logQuery);
        return new PageResult<>(operationLogVOList, count);
    }
}




