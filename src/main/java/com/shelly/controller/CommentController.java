package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.AccessLimit;
import com.shelly.annotation.OptLogger;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.CommentQuery;
import com.shelly.entity.vo.req.CheckReq;
import com.shelly.entity.vo.req.CommentReq;
import com.shelly.entity.vo.res.CommentBackResp;
import com.shelly.entity.vo.res.CommentResp;
import com.shelly.entity.vo.res.RecentCommentResp;
import com.shelly.entity.vo.res.ReplyResp;
import com.shelly.enums.LikeTypeEnum;
import com.shelly.service.CommentService;
import com.shelly.strategy.context.LikeStrategyContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shelly.constants.OptTypeConstant.DELETE;
import static com.shelly.constants.OptTypeConstant.UPDATE;

@RequiredArgsConstructor
@RestController
@Tag(name = "评论模块")
public class CommentController {

    private final CommentService commentService;

    private final LikeStrategyContext likeStrategyContext;


    @SaCheckPermission("news:comment:list")
    @GetMapping("/admin/comment/list")
    @Operation(summary = "获取后台评论列表")
    public Result<PageResult<CommentBackResp>> listCommentBackVO(CommentQuery commentQuery) {
        return Result.success(commentService.listCommentBackVO(commentQuery));
    }


    @SaCheckLogin
    @SaCheckPermission("news:comment:add")
    @Operation(summary = "添加评论")
    @PostMapping("/comment/add")
    public Result<?> addComment(@Validated @RequestBody CommentReq comment) {
        commentService.addComment(comment);
        return Result.success();
    }


    @SaCheckPermission("news:comment:delete")
    @DeleteMapping("/admin/comment/delete")
    @Operation(summary = "删除评论")
    @OptLogger(value = DELETE)
    public Result<?> deleteComment(@RequestBody List<Integer> commentIdList) {
        commentService.removeByIds(commentIdList);
        return Result.success();
    }


    @SaCheckPermission("news:comment:pass")
    @Operation(summary = "审核评论")
    @OptLogger(value = UPDATE)
    @PutMapping("/admin/comment/pass")
    public Result<?> updateCommentCheck(@Validated @RequestBody CheckReq check) {
        commentService.updateCommentCheck(check);
        return Result.success();
    }


    @SaCheckLogin
    @AccessLimit(seconds = 60, maxCount = 3)
    @Operation(summary = "点赞评论")
    @SaCheckPermission("news:comment:like")
    @PostMapping("/comment/{commentId}/like")
    public Result<?> likeComment(@PathVariable("commentId") Integer commentId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.COMMENT, commentId);
        return Result.success();
    }


    @GetMapping("/recent/comment")
    @Operation(summary = "获取最新评论")
    public Result<List<RecentCommentResp>> listRecentCommentVO() {
        return Result.success(commentService.listRecentCommentVO());
    }


    @GetMapping("/comment/list")
    @Operation(summary = "获取评论列表")
    public Result<PageResult<CommentResp>> listCommentVO(CommentQuery commentQuery) {
        return Result.success(commentService.listCommentVO(commentQuery));
    }


    @GetMapping("/comment/{commentId}/reply")
    @Operation(summary = "获取评论回复")
    public Result<List<ReplyResp>> listReply(@PathVariable("commentId") Integer commentId) {
        return Result.success(commentService.listReply(commentId));
    }

}