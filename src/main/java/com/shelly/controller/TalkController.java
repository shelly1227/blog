package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.AccessLimit;
import com.shelly.annotation.OptLogger;
import com.shelly.annotation.VisitLogger;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.PageQuery;
import com.shelly.entity.vo.query.TalkQuery;
import com.shelly.entity.vo.req.TalkReq;
import com.shelly.entity.vo.res.TalkBackInfoResp;
import com.shelly.entity.vo.res.TalkBackResp;
import com.shelly.entity.vo.res.TalkResp;
import com.shelly.enums.LikeTypeEnum;
import com.shelly.service.TalkService;
import com.shelly.strategy.context.LikeStrategyContext;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.shelly.constants.OptTypeConstant.*;


/**
 * 说说控制器
 * @author shelly
 **/
@RestController
@RequiredArgsConstructor
@Tag(name = "说说模块")
//checked but upload error
public class TalkController {

    private final TalkService talkService;

    private final LikeStrategyContext likeStrategyContext;



    @SaCheckPermission("web:talk:list")
    @GetMapping("/admin/talk/list")
    @Operation(summary = "获取说说列表")
    public Result<PageResult<TalkBackResp>> listTalkBackVO(TalkQuery talkQuery) {
        return Result.success(talkService.listTalkBackVO(talkQuery));
    }


    @ApiImplicitParam(name = "file", value = "相册封面", required = true, dataType = "MultipartFile")
    @SaCheckPermission("web:talk:upload")
    @PostMapping("/admin/talk/upload")
    @Operation(summary = "上传说说封面")
    @OptLogger(value = UPLOAD)
    public Result<String> uploadTalkCover(@RequestParam("file") MultipartFile file) {
        return Result.success(talkService.uploadTalkCover(file));
    }

    @SaCheckPermission("web:talk:add")
    @OptLogger(value = ADD)
    @Operation(summary = "添加说说")
    @PostMapping("/admin/talk/add")
    public Result<?> addTalk(@Validated @RequestBody TalkReq talk) {
        talkService.addTalk(talk);
        return Result.success();
    }


    @SaCheckPermission("web:talk:delete")
    @DeleteMapping("/admin/talk/delete/{talkId}")
    @Operation(summary = "删除说说")
    @OptLogger(value = DELETE)
    public Result<?> deleteTalk(@PathVariable("talkId") Integer talkId) {
        talkService.delete(talkId);
        return Result.success();
    }



    @SaCheckPermission("web:talk:update")
    @PutMapping("/admin/talk/update")
    @Operation(summary = "修改说说")
    @OptLogger(value = UPDATE)
    public Result<?> updateTalk(@Validated @RequestBody TalkReq talk) {
        talkService.updateTalk(talk);
        return Result.success();
    }


    @SaCheckPermission("web:talk:edit")
    @Operation(summary = "获取说说信息")
    @GetMapping("/admin/talk/edit/{talkId}")
    public Result<TalkBackInfoResp> editTalk(@PathVariable("talkId") Integer talkId) {
        return Result.success(talkService.editTalk(talkId));
    }

    @SaCheckLogin
    @AccessLimit(seconds = 60, maxCount = 3)
    @SaCheckPermission("web:talk:like")
    @Operation(summary = "点赞")
    @PostMapping("/talk/{talkId}/like")
    public Result<?> saveTalkLike(@PathVariable("talkId") Integer talkId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.TALK, talkId);
        return Result.success();
    }


    @GetMapping("/home/talk")
    @Operation(summary = "获取首页说说")
    public Result<List<String>> listTalkHome() {
        return Result.success(talkService.listTalkHome());
    }

    @GetMapping("/talk/list")
    @VisitLogger(value = "说说列表")
    @Operation(summary = "获取说说列表")
    public Result<PageResult<TalkResp>> listTalkVO(@Validated PageQuery pageQuery) {
        return Result.success(talkService.listTalkVO(pageQuery));
    }

    @GetMapping("/talk/{talkId}")
    @VisitLogger(value = "说说")
    @Operation(summary = "获取说说详情")
    public Result<TalkResp> getTalkById(@PathVariable("talkId") Integer talkId) {
        return Result.success(talkService.getTalkById(talkId));
    }
}