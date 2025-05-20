package com.shelly.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.VisitLog;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.LogQuery;

/**
* @author Shelly6
* @description 针对表【t_visit_log】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface VisitLogService extends IService<VisitLog> {

    PageResult<VisitLog> listVisitLogIds(LogQuery logQuery);

    void saveVisitLog(VisitLog visitLog);
}
