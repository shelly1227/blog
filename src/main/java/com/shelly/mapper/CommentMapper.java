package com.shelly.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.Comment;
import com.shelly.entity.vo.query.CommentQuery;
import com.shelly.entity.vo.res.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_comment】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.Comment
*/
public interface CommentMapper extends BaseMapper<Comment> {

    List<CommentCountResp> selectCommentCountByTypeId(@Param("typeIdList") List<Integer> typeIdList, @Param("commentType") Integer commentType);

    Long countComment(@Param("param") CommentQuery commentQuery);

    List<CommentBackResp> selectBackCommentList(@Param("param")CommentQuery commentQuery);

    List<CommentResp> selectParentComment(@Param("param")CommentQuery commentQuery);

    List<ReplyResp> selectReplyByParentIdList(@Param("parentCommentIdList")List<Integer> parentCommentIdList);

    List<ReplyCountResp> selectReplyCountByParentId(@Param("parentCommentIdList") List<Integer> parentCommentIdList);

    List<RecentCommentResp> selectRecentComment();

    List<ReplyResp> selectReplyByParentId(@Param("limit") Long limit, @Param("size") Long size, @Param("commentId") Integer commentId);

    List<Integer> selectCommentIdByParentId(@Param("parentId")Integer parentId);
}




