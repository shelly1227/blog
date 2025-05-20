package com.shelly.quartz.task;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.shelly.entity.pojo.VisitLog;
import com.shelly.service.VisitLogService;
import com.shelly.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.shelly.enums.RedisConstants.UNIQUE_VISITOR;

@SuppressWarnings(value = "all")
@Component("timedTask")
public class TimedTask {
    @Autowired
    private RedisUtil redisService;

    @Autowired
    private VisitLogService visitLogService;

    /**
     * 清除博客访问记录
     */
    public void clear() {
        redisService.remove(UNIQUE_VISITOR.getKey());
    }

    /**
     * 测试任务
     */
    public void test() {
        System.out.println("测试任务");
    }

    /**
     * 清除一周前的访问日志
     */
    public void clearVistiLog() {
        DateTime endTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        visitLogService.lambdaUpdate()
                .le(VisitLog::getCreateTime, endTime)
                .remove();
    }
}