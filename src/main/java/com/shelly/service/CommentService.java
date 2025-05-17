package com.shelly.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Comment;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.CommentQuery;
import com.shelly.entity.vo.Request.CheckReq;
import com.shelly.entity.vo.Request.CommentReq;
import com.shelly.entity.vo.Response.CommentBackResp;
import com.shelly.entity.vo.Response.CommentResp;
import com.shelly.entity.vo.Response.RecentCommentResp;
import com.shelly.entity.vo.Response.ReplyResp;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_comment】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface CommentService extends IService<Comment> {

    PageResult<CommentBackResp> listCommentBackVO(CommentQuery commentQuery);

    PageResult<CommentResp> listCommentVO(CommentQuery commentQuery);

    List<RecentCommentResp> listRecentCommentVO();

    List<ReplyResp> listReply(Integer commentId);

    void updateCommentCheck(CheckReq check);

    void addComment(CommentReq comment);
}
