package com.shelly.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.ExceptionLog;
import com.shelly.entity.vo.query.LogQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_exception_log】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.ExceptionLog
*/
public interface ExceptionLogMapper extends BaseMapper<ExceptionLog> {

    List<ExceptionLog> selectExceptionLogList(@Param("param") LogQuery logQuery);
}




