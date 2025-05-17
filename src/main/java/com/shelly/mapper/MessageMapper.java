package com.shelly.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.Message;
import com.shelly.entity.vo.Query.MessageQuery;
import com.shelly.entity.vo.Response.MessageBackResp;
import com.shelly.entity.vo.Response.MessageResp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_message】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.Message
*/
public interface MessageMapper extends BaseMapper<Message> {

    List<MessageResp> selectMessageVOList();

    List<MessageBackResp> selectBackMessageList(@Param("param")MessageQuery messageQuery);
}




