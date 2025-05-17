package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.AccessLimit;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.PageQuery;
import com.shelly.entity.vo.Query.TalkQuery;
import com.shelly.entity.vo.Request.TalkReq;
import com.shelly.entity.vo.Response.TalkBackInfoResp;
import com.shelly.entity.vo.Response.TalkBackResp;
import com.shelly.entity.vo.Response.TalkResp;
import com.shelly.enums.LikeTypeEnum;
import com.shelly.service.TalkService;
import com.shelly.strategy.context.LikeStrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


/**
 * 说说控制器
 *
 * @author shelly
 **/
@RestController
@RequiredArgsConstructor
public class TalkController {

    private final TalkService talkService;

    private final LikeStrategyContext likeStrategyContext;



    @SaCheckPermission("web:talk:list")
    @GetMapping("/admin/talk/list")
    public Result<PageResult<TalkBackResp>> listTalkBackVO(TalkQuery talkQuery) {
        return Result.success(talkService.listTalkBackVO(talkQuery));
    }


    @ApiImplicitParam(name = "file", value = "相册封面", required = true, dataType = "MultipartFile")
    @SaCheckPermission("web:talk:upload")
    @PostMapping("/admin/talk/upload")
    public Result<String> uploadTalkCover(@RequestParam("file") MultipartFile file) {
        return Result.success(talkService.uploadTalkCover(file));
    }

    @SaCheckPermission("web:talk:add")
    @PostMapping("/admin/talk/add")
    public Result<?> addTalk(@Validated @RequestBody TalkReq talk) {
        talkService.addTalk(talk);
        return Result.success();
    }



    @SaCheckPermission("web:talk:delete")
    @DeleteMapping("/admin/talk/delete/{talkId}")
    public Result<?> deleteTalk(@PathVariable("talkId") Integer talkId) {
        talkService.delete(talkId);
        return Result.success();
    }



    @SaCheckPermission("web:talk:update")
    @PutMapping("/admin/talk/update")
    public Result<?> updateTalk(@Validated @RequestBody TalkReq talk) {
        talkService.updateTalk(talk);
        return Result.success();
    }


    @SaCheckPermission("web:talk:edit")
    @GetMapping("/admin/talk/edit/{talkId}")
    public Result<TalkBackInfoResp> editTalk(@PathVariable("talkId") Integer talkId) {
        return Result.success(talkService.editTalk(talkId));
    }

    @SaCheckLogin
    @AccessLimit(seconds = 60, maxCount = 3)
    @SaCheckPermission("web:talk:like")
    @PostMapping("/talk/{talkId}/like")
    public Result<?> saveTalkLike(@PathVariable("talkId") Integer talkId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.TALK, talkId);
        return Result.success();
    }


    @GetMapping("/home/talk")
    public Result<List<String>> listTalkHome() {
        return Result.success(talkService.listTalkHome());
    }



    @GetMapping("/talk/list")
    public Result<PageResult<TalkResp>> listTalkVO(@Validated PageQuery pageQuery) {
        return Result.success(talkService.listTalkVO(pageQuery));
    }



    @GetMapping("/talk/{talkId}")
    public Result<TalkResp> getTalkById(@PathVariable("talkId") Integer talkId) {
        return Result.success(talkService.getTalkById(talkId));
    }
}