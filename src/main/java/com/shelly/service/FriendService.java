package com.shelly.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.shelly.entity.pojo.Friend;
import com.shelly.entity.vo.Response.FriendBackResp;
import com.shelly.entity.vo.Request.FriendReq;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.FriendQuery;
import com.shelly.entity.vo.Response.FriendResp;

import java.util.List;

/**
* @author Shelly6
* @description 针对表【t_friend】的数据库操作Service
* @createDate 2024-07-22 20:24:46
*/
public interface FriendService extends IService<Friend> {

    void addFriend(FriendReq friend);

    void deleteFriend(List<Integer> integerList);

    PageResult<FriendBackResp> listFriend(FriendQuery friendQuery);

    void updateFriend(FriendReq friend);

    List<FriendResp> listFriendVO();
}
