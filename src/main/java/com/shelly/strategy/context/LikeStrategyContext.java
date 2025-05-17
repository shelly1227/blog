package com.shelly.strategy.context;

import com.shelly.enums.LikeTypeEnum;
import com.shelly.strategy.LikeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LikeStrategyContext {

    @Autowired
    private Map<String, LikeStrategy> likeStrategyMap;

    /**
     * 点赞
     *
     * @param likeType 点赞类型
     * @param typeId   类型id
     */
    public void executeLikeStrategy(LikeTypeEnum likeType, Integer typeId) {
        likeStrategyMap.get(likeType.getStrategy()).like(typeId);
    }
}