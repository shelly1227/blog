package com.shelly.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shelly.entity.pojo.Friend;
import com.shelly.entity.vo.res.FriendResp;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_friend】的数据库操作Mapper
* @createDate 2024-07-22 20:24:46
* @Entity generator.entity.Friend
*/
public interface FriendMapper extends BaseMapper<Friend> {

    List<FriendResp> selectFriendVOList();
}




