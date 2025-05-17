package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.OnlineUserQuery;
import com.shelly.entity.vo.Query.UserQuery;
import com.shelly.entity.vo.Request.DisableReq;
import com.shelly.entity.vo.Request.PasswordReq;
import com.shelly.entity.vo.Request.UserRoleReq;
import com.shelly.entity.vo.Response.*;
import com.shelly.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户模块")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/admin/user/getUserInfo")
    public Result<UserBackInfoResp> getUserBackInfo() {
        return Result.success(userService.getUserBackInfo());
    }


    @GetMapping("/admin/user/getUserMenu")
    public Result<List<RouterResp>> getUserMenu() {
        return Result.success(userService.getUserMenu());
    }


    @SaCheckPermission("system:user:list")
    @GetMapping("/admin/user/list")
    public Result<PageResult<UserBackResp>> listUserBackVO(UserQuery userQuery) {
        return Result.success(userService.listUserBackVO(userQuery));
    }

    /**
     * 查看用户角色选项
     *
     * @return {@link UserRoleResp} 用户角色选项
     */
    @ApiOperation(value = "查看用户角色选项")
    @SaCheckPermission("system:user:list")
    @GetMapping("/admin/user/role")
    public Result<List<UserRoleResp>> listUserRoleDTO() {
        return Result.success(userService.listUserRoleDTO());
    }

    /**
     * 修改用户
     *
     * @param user 用户信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改用户")
    @SaCheckPermission("system:user:update")
    @PutMapping("/admin/user/update")
    public Result<?> updateUser(@Validated @RequestBody UserRoleReq user) {
        userService.updateUser(user);
        return Result.success();
    }


    @SaCheckPermission("system:user:status")
    @PutMapping("/admin/user/changeStatus")
    public Result<?> updateUserStatus(@Validated @RequestBody DisableReq disable) {
        userService.updateUserStatus(disable);
        return Result.success();
    }

    /**
     * 查看在线用户
     *
     * @param onlineUserQuery 查询条件
     * @return {@link OnlineUserResp} 在线用户列表
     */
    @ApiOperation(value = "查看在线用户")
    @SaCheckPermission("monitor:online:list")
    @GetMapping("/admin/online/list")
    public Result<PageResult<OnlineUserResp>> listOnlineUser(OnlineUserQuery onlineUserQuery) {
        return Result.success(userService.listOnlineUser(onlineUserQuery));
    }

    /**
     * 下线用户
     *
     * @param token 在线token
     * @return {@link Result<>}
     */
    @SaCheckPermission("monitor:online:kick")
    @GetMapping("/admin/online/kick/{token}")
    public Result<?> kickOutUser(@PathVariable("token") String token) {
        userService.kickOutUser(token);
        return Result.success();
    }

    /**
     * 修改管理员密码
     *
     * @param password 密码
     * @return {@link Result<>}
     */
    @SaCheckRole("1")
    @ApiOperation(value = "修改管理员密码")
    @PutMapping("/admin/password")
    public Result<?> updateAdminPassword(@Validated @RequestBody PasswordReq password) {
        userService.updateAdminPassword(password);
        return Result.success();
    }

}