package com.shelly.strategy.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shelly.constants.CommonConstant;

import com.shelly.entity.pojo.Comment;
import com.shelly.enums.RedisConstants;
import com.shelly.mapper.CommentMapper;
import com.shelly.strategy.LikeStrategy;
import com.shelly.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("commentLikeStrategyImpl")
public class CommentLikeStrategyImpl implements LikeStrategy {

    @Autowired
    private RedisUtil redisService;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public void like(Integer commentId) {
        // 判断评论是否存在或是否通过或是否进入回收站
        Comment comment = commentMapper.selectOne(new LambdaQueryWrapper<Comment>()
                .select(Comment::getId, Comment::getIsCheck)
                .eq(Comment::getId, commentId).last("limit 1"));
        Assert.isFalse(Objects.isNull(comment) || comment.getIsCheck().equals(CommonConstant.FALSE), "文章不存在");
        // 用户id作为键，评论id作为值，记录用户点赞记录
        String userLikeCommentKey = RedisConstants.USER_COMMENT_LIKE.getKey() + StpUtil.getLoginIdAsInt();
        if (redisService.hasSetValue(userLikeCommentKey, commentId)) {
            // 取消点赞则删除用户id中的评论id
            redisService.remove(userLikeCommentKey, commentId);
            // 评论点赞量-1
            redisService.increment(RedisConstants.COMMENT_LIKE_COUNT.getKey(), commentId.toString(), -1L);
        } else {
            // 点赞则在用户id记录评论id
            redisService.setSet(userLikeCommentKey, commentId);
            // 评论点赞量+1
            redisService.increment(RedisConstants.COMMENT_LIKE_COUNT.getKey(), commentId.toString(), 1L);
        }
    }
}