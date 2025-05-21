package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.AccessLimit;
import com.shelly.annotation.OptLogger;
import com.shelly.annotation.VisitLogger;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.MessageQuery;
import com.shelly.entity.vo.req.CheckReq;
import com.shelly.entity.vo.req.MessageReq;
import com.shelly.entity.vo.res.MessageBackResp;
import com.shelly.entity.vo.res.MessageResp;
import com.shelly.service.MessageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shelly.constants.OptTypeConstant.DELETE;
import static com.shelly.constants.OptTypeConstant.UPDATE;

@RestController
@RequiredArgsConstructor
@Tag(name = "留言模块")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "查看留言列表")
    @VisitLogger(value = "留言")
    @GetMapping("/message/list")
    public Result<List<MessageResp>> listMessageVO() {
        return Result.success(messageService.listMessageVO());
    }


    @SaCheckPermission("news:message:list")
    @Operation(summary = "获取留言后台列表")
    @GetMapping("/admin/message/list")
    public Result<PageResult<MessageBackResp>> listMessageBackVO(MessageQuery messageQuery) {
        return Result.success(messageService.listMessageBackVO(messageQuery));
    }

    @AccessLimit(seconds = 60, maxCount = 3)
    @Operation(summary = "添加留言")
    @PostMapping("/message/add")
    public Result<?> addMessage(@Validated @RequestBody MessageReq message) {
        messageService.addMessage(message);
        return Result.success();
    }


    @SaCheckPermission("news:message:delete")
    @Operation(summary = "删除留言")
    @OptLogger(value = DELETE)
    @DeleteMapping("/admin/message/delete")
    public Result<?> deleteMessage(@RequestBody List<Integer> messageIdList) {
        messageService.removeByIds(messageIdList);
        return Result.success();
    }


    @SaCheckPermission("news:message:pass")
    @OptLogger(value = UPDATE)
    @Operation(summary = "审核留言")
    @PutMapping("/admin/message/pass")
    public Result<?> updateMessageCheck(@Validated @RequestBody CheckReq check) {
        messageService.updateMessageCheck(check);
        return Result.success();
    }
}
