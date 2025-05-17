package com.shelly.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Message;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.MessageQuery;
import com.shelly.entity.vo.Request.CheckReq;
import com.shelly.entity.vo.Request.MessageReq;
import com.shelly.entity.vo.Response.MessageBackResp;
import com.shelly.entity.vo.Response.MessageResp;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_message】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface MessageService extends IService<Message> {

    List<MessageResp> listMessageVO();

    PageResult<MessageBackResp> listMessageBackVO(MessageQuery messageQuery);

    void addMessage(MessageReq message);

    void updateMessageCheck(CheckReq check);
}
