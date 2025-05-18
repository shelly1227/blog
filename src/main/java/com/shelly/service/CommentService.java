package com.shelly.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Comment;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.CommentQuery;
import com.shelly.entity.vo.req.CheckReq;
import com.shelly.entity.vo.req.CommentReq;
import com.shelly.entity.vo.res.CommentBackResp;
import com.shelly.entity.vo.res.CommentResp;
import com.shelly.entity.vo.res.RecentCommentResp;
import com.shelly.entity.vo.res.ReplyResp;

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
