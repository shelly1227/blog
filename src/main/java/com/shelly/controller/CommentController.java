package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.AccessLimit;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.CommentQuery;
import com.shelly.entity.vo.Request.CheckReq;
import com.shelly.entity.vo.Request.CommentReq;
import com.shelly.entity.vo.Response.CommentBackResp;
import com.shelly.entity.vo.Response.CommentResp;
import com.shelly.entity.vo.Response.RecentCommentResp;
import com.shelly.entity.vo.Response.ReplyResp;
import com.shelly.enums.LikeTypeEnum;
import com.shelly.service.CommentService;
import com.shelly.strategy.context.LikeStrategyContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    private final LikeStrategyContext likeStrategyContext;


    @SaCheckPermission("news:comment:list")
    @GetMapping("/admin/comment/list")
    public Result<PageResult<CommentBackResp>> listCommentBackVO(CommentQuery commentQuery) {
        return Result.success(commentService.listCommentBackVO(commentQuery));
    }


    @SaCheckLogin
    @SaCheckPermission("news:comment:add")
    @PostMapping("/comment/add")
    public Result<?> addComment(@Validated @RequestBody CommentReq comment) {
        commentService.addComment(comment);
        return Result.success();
    }


    @SaCheckPermission("news:comment:delete")
    @DeleteMapping("/admin/comment/delete")
    public Result<?> deleteComment(@RequestBody List<Integer> commentIdList) {
        commentService.removeByIds(commentIdList);
        return Result.success();
    }


    @SaCheckPermission("news:comment:pass")
    @PutMapping("/admin/comment/pass")
    public Result<?> updateCommentCheck(@Validated @RequestBody CheckReq check) {
        commentService.updateCommentCheck(check);
        return Result.success();
    }


    @SaCheckLogin
    @AccessLimit(seconds = 60, maxCount = 3)
    @SaCheckPermission("news:comment:like")
    @PostMapping("/comment/{commentId}/like")
    public Result<?> likeComment(@PathVariable("commentId") Integer commentId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.COMMENT, commentId);
        return Result.success();
    }


    @GetMapping("/recent/comment")
    public Result<List<RecentCommentResp>> listRecentCommentVO() {
        return Result.success(commentService.listRecentCommentVO());
    }


    @GetMapping("/comment/list")
    public Result<PageResult<CommentResp>> listCommentVO(CommentQuery commentQuery) {
        return Result.success(commentService.listCommentVO(commentQuery));
    }


    @GetMapping("/comment/{commentId}/reply")
    public Result<List<ReplyResp>> listReply(@PathVariable("commentId") Integer commentId) {
        return Result.success(commentService.listReply(commentId));
    }

}