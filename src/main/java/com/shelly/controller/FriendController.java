package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.annotation.OptLogger;
import com.shelly.annotation.VisitLogger;
import com.shelly.common.Result;
import com.shelly.entity.vo.res.FriendBackResp;
import com.shelly.entity.vo.req.FriendReq;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.FriendQuery;
import com.shelly.entity.vo.res.FriendResp;
import com.shelly.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shelly.constants.OptTypeConstant.*;

/**
 * @ description: 友链管理
 * @ author: shelly
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "友链模块")
//checked
public class FriendController {
    private final  FriendService friendService;
    @PostMapping("/admin/friend/add")
    @SaCheckPermission("web:friend:add")
    @OptLogger(value = ADD)
    @Operation(summary = "添加友链")
    public Result<?> addFriend(@Valid @RequestBody FriendReq friend) {
        friendService.addFriend(friend);
        return Result.success();
    }
    @DeleteMapping("/admin/friend/delete")
    @Operation(summary = "删除友链")
    @OptLogger(value = DELETE)
    @SaCheckPermission("web:friend:delete")
    public Result<?> deleteFriend(@RequestBody List<Integer> friendIdList) {
        friendService.deleteFriend(friendIdList);
        return Result.success();
    }
    @GetMapping("/admin/friend/list")
    @SaCheckPermission("web:friend:list")
    @Operation(summary = "获取友链列表")
    public Result<PageResult<FriendBackResp>> listFriend(FriendQuery friendQuery) {
        return Result.success(friendService.listFriend(friendQuery));
    }
    @PutMapping("/admin/friend/update")
    @SaCheckPermission("web:friend:update")
    @OptLogger(value = UPDATE)
    @Operation(summary = "修改友链")
    public Result<?> updateFriend(@Valid @RequestBody FriendReq friend) {
        friendService.updateFriend(friend);
        return Result.success();
    }
    @GetMapping("/friend/list")
    @VisitLogger(value = "友链")
    @Operation(summary = "获取友链列表")
    public Result<List<FriendResp>> listFriend() {
        return Result.success(friendService.listFriendVO());
    }
}
