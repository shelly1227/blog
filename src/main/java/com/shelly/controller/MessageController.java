package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.AccessLimit;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.MessageQuery;
import com.shelly.entity.vo.Request.CheckReq;
import com.shelly.entity.vo.Request.MessageReq;
import com.shelly.entity.vo.Response.MessageBackResp;
import com.shelly.entity.vo.Response.MessageResp;
import com.shelly.service.MessageService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    @ApiOperation(value = "查看留言列表")
    @GetMapping("/message/list")
    public Result<List<MessageResp>> listMessageVO() {
        return Result.success(messageService.listMessageVO());
    }


    @SaCheckPermission("news:message:list")
    @GetMapping("/admin/message/list")
    public Result<PageResult<MessageBackResp>> listMessageBackVO(MessageQuery messageQuery) {
        return Result.success(messageService.listMessageBackVO(messageQuery));
    }

    @AccessLimit(seconds = 60, maxCount = 3)
    @PostMapping("/message/add")
    public Result<?> addMessage(@Validated @RequestBody MessageReq message) {
        messageService.addMessage(message);
        return Result.success();
    }


    @SaCheckPermission("news:message:delete")
    @DeleteMapping("/admin/message/delete")
    public Result<?> deleteMessage(@RequestBody List<Integer> messageIdList) {
        messageService.removeByIds(messageIdList);
        return Result.success();
    }


    @SaCheckPermission("news:message:pass")
    @PutMapping("/admin/message/pass")
    public Result<?> updateMessageCheck(@Validated @RequestBody CheckReq check) {
        messageService.updateMessageCheck(check);
        return Result.success();
    }
}
