package com.shelly.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Message;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.MessageQuery;
import com.shelly.entity.vo.req.CheckReq;
import com.shelly.entity.vo.req.MessageReq;
import com.shelly.entity.vo.res.MessageBackResp;
import com.shelly.entity.vo.res.MessageResp;

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
