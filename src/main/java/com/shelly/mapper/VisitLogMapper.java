package com.shelly.mapper;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.VisitLog;
import com.shelly.entity.vo.query.LogQuery;
import com.shelly.entity.vo.res.UserViewResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_visit_log】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.VisitLog
*/
public interface VisitLogMapper extends BaseMapper<VisitLog> {

    List<VisitLog> selectVisitLogRespList(@Param("param") LogQuery logQuery);

    List<UserViewResp> selectUserViewList(@Param("startTime") DateTime startTime, @Param("endTime") DateTime endTime);
}




