package com.shelly.strategy.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.shelly.entity.pojo.Talk;
import com.shelly.enums.RedisConstants;
import com.shelly.mapper.TalkMapper;
import com.shelly.strategy.LikeStrategy;
import com.shelly.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.hutool.core.lang.Assert;

@Service("talkLikeStrategyImpl")
public class TalkLikeStrategyImpl implements LikeStrategy {

    @Autowired
    private RedisUtil redisService;

    @Autowired
    private TalkMapper talkMapper;

    @Override
    public void like(Integer talkId) {
        Talk talk = talkMapper.selectOne(new LambdaQueryWrapper<Talk>()
                .select(Talk::getId)
                .eq(Talk::getId, talkId));
        Assert.notNull(talk, "说说不存在");
        // 用户id作为键，说说id作为值，记录用户点赞记录
        String userLikeTalkKey = RedisConstants.USER_TALK_LIKE.getKey() + StpUtil.getLoginIdAsInt();
        // 判断是否点赞
        if (redisService.hasSetValue(userLikeTalkKey, talkId)) {
            // 取消点赞则删除用户id中的说说id
            redisService.remove(userLikeTalkKey, talkId);
            // 说说点赞量-1
            redisService.increment(RedisConstants.TALK_LIKE_COUNT.getKey(), talkId.toString(), -1L);
        } else {
            // 点赞则在用户id记录说说id
            redisService.setSet(userLikeTalkKey, talkId);
            // 说说点赞量+1
            redisService.increment(RedisConstants.TALK_LIKE_COUNT.getKey(), talkId.toString(), 1L);
        }
    }

}
