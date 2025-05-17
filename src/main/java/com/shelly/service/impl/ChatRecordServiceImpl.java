package com.shelly.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.shelly.entity.pojo.ChatRecord;
import com.shelly.service.ChatRecordService;
import com.shelly.mapper.ChatRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author Shelly6
* @description 针对表【t_chat_record】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@Service
public class ChatRecordServiceImpl extends ServiceImpl<ChatRecordMapper, ChatRecord>
    implements ChatRecordService{

}




