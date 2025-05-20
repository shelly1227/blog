package com.shelly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shelly.entity.pojo.VisitLog;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.LogQuery;
import com.shelly.service.VisitLogService;
import com.shelly.mapper.VisitLogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_visit_log】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
@RequiredArgsConstructor
public class VisitLogServiceImpl extends ServiceImpl<VisitLogMapper, VisitLog>
    implements VisitLogService {
private final VisitLogMapper visitLogMapper;
    @Override
    public PageResult<VisitLog> listVisitLogIds(LogQuery logQuery) {
        Long count = visitLogMapper.selectCount(new LambdaQueryWrapper<VisitLog>()
                .like(StringUtils.hasText(logQuery.getKeyword()), VisitLog::getPage, logQuery.getKeyword())
        );
        if (count == 0){
            return new PageResult<>();
        }
        List<VisitLog>list = visitLogMapper.selectVisitLogRespList(logQuery);
        return new PageResult<>(list,count);
    }

    @Override
    public void saveVisitLog(VisitLog visitLog) {
        this.save(visitLog);
    }
}




