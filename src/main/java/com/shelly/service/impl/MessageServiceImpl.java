package com.shelly.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shelly.constants.CommonConstant;

import com.shelly.entity.pojo.Message;
import com.shelly.entity.pojo.SiteConfig;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.MessageQuery;
import com.shelly.entity.vo.req.CheckReq;
import com.shelly.entity.vo.req.MessageReq;
import com.shelly.entity.vo.res.MessageBackResp;
import com.shelly.entity.vo.res.MessageResp;
import com.shelly.service.MessageService;
import com.shelly.mapper.MessageMapper;
import com.shelly.service.SiteConfigService;
import com.shelly.utils.HTMLUtils;
import com.shelly.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

import static cn.hutool.extra.servlet.JakartaServletUtil.getClientIP;

/**
* @author Shelly6
* @description 针对表【t_message】的数据库操作Service实现
* @createDate 2024-07-22 20:24:46
*/
@RequiredArgsConstructor
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
    implements MessageService{
    private final MessageMapper messageMapper;
    private final SiteConfigService siteConfigService;
    private final HttpServletRequest request;



    @Override
    public List<MessageResp> listMessageVO() {
        return messageMapper.selectMessageVOList();
    }

    @Override
    public PageResult<MessageBackResp> listMessageBackVO(MessageQuery messageQuery) {
        // 查询留言数量
        Long count = messageMapper.selectCount(new LambdaQueryWrapper<Message>()
                .like(StringUtils.hasText(messageQuery.getKeyword()), Message::getNickname, messageQuery.getKeyword())
                .eq(Objects.nonNull(messageQuery.getIsCheck()), Message::getIsCheck, messageQuery.getIsCheck()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台友链列表
        List<MessageBackResp> messageBackRespList = messageMapper.selectBackMessageList(messageQuery);
        return new PageResult<>(messageBackRespList, count);
    }

    @Override
    public void addMessage(MessageReq message) {
        //这里的意思是判断是否要进行审核，这是可以设置的
        SiteConfig siteConfig = siteConfigService.getSiteConfig();
        Integer messageCheck = siteConfig.getMessageCheck();
        String ipAddress = getClientIP(request);
        String ipSource = IpUtils.getIpSource(ipAddress);
        Message newMessage = new Message();
        BeanUtils.copyProperties(message, newMessage);
        newMessage.setMessageContent(HTMLUtils.filter(message.getMessageContent()));
        newMessage.setIpAddress(ipAddress);
        newMessage.setIsCheck(messageCheck.equals(CommonConstant.FALSE) ? CommonConstant.TRUE : CommonConstant.FALSE);
        newMessage.setIpSource(ipSource);
        messageMapper.insert(newMessage);
    }

    @Override
    @Transactional
    public void updateMessageCheck(CheckReq check) {
        // 修改留言审核状态
        List<Message> messageList = check.getIdList()
                .stream()
                .map(id -> Message.builder()
                        .id(id)
                        .isCheck(check.getIsCheck())
                        .build())
                .toList();
        this.updateBatchById(messageList);
    }
}




