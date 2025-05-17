package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.common.Result;
import com.shelly.entity.vo.Response.FriendBackResp;
import com.shelly.entity.vo.Request.FriendReq;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.FriendQuery;
import com.shelly.entity.vo.Response.FriendResp;
import com.shelly.service.FriendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @ description: 友链管理
 * @ author: shelly
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class FriendController {
    private final  FriendService friendService;
    @PostMapping("/admin/friend/add")
    @SaCheckPermission("web:friend:add")
    public Result<?> addFriend(@Valid @RequestBody FriendReq friend) {
        friendService.addFriend(friend);
        return Result.success();
    }
    @DeleteMapping("/admin/friend/delete")
    @SaCheckPermission("web:friend:delete")
    public Result<?> deleteFriend(@RequestBody List<Integer> friendIdList) {
        friendService.deleteFriend(friendIdList);
        return Result.success();
    }
    @GetMapping("/admin/friend/list")
    @SaCheckPermission("web:friend:list")
    public Result<PageResult<FriendBackResp>> listFriend(FriendQuery friendQuery) {
        return Result.success(friendService.listFriend(friendQuery));
    }
    @PutMapping("/admin/friend/update")
    @SaCheckPermission("web:friend:update")
    public Result<?> updateFriend(@Valid @RequestBody FriendReq friend) {
        friendService.updateFriend(friend);
        return Result.success();
    }
    @GetMapping("/friend/list")
    public Result<List<FriendResp>> listFriend() {
        return Result.success(friendService.listFriendVO());
    }
}
