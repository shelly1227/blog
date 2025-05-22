package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import com.shelly.annotation.OptLogger;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.OnlineUserQuery;
import com.shelly.entity.vo.query.UserQuery;
import com.shelly.entity.vo.req.DisableReq;
import com.shelly.entity.vo.req.PasswordReq;
import com.shelly.entity.vo.req.UserRoleReq;
import com.shelly.entity.vo.res.*;
import com.shelly.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shelly.constants.OptTypeConstant.KICK;
import static com.shelly.constants.OptTypeConstant.UPDATE;

@Tag(name = "用户模块")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    @GetMapping("/admin/user/getUserInfo")
    @Operation(summary = "获取用户信息")
    public Result<UserBackInfoResp> getUserBackInfo() {
        return Result.success(userService.getUserBackInfo());
    }


    @GetMapping("/admin/user/getUserMenu")
    @Operation(summary = "获取用户菜单")
    public Result<List<RouterResp>> getUserMenu() {
        return Result.success(userService.getUserMenu(StpUtil.getLoginIdAsInt()));
    }


    @SaCheckPermission("system:user:list")
    @Operation(summary = "获取用户列表")
    @GetMapping("/admin/user/list")
    public Result<PageResult<UserBackResp>> listUserBackVO(UserQuery userQuery) {
        return Result.success(userService.listUserBackVO(userQuery));
    }

    /**
     * 查看用户角色选项
     *
     * @return {@link UserRoleResp} 用户角色选项
     */
    @Operation(summary = "查看用户角色选项")
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
    @Operation(summary = "修改用户")
    @OptLogger(value = UPDATE)
    @SaCheckPermission("system:user:update")
    @PutMapping("/admin/user/update")
    public Result<?> updateUser(@Validated @RequestBody UserRoleReq user) {
        userService.updateUser(user);
        return Result.success();
    }


    @SaCheckPermission("system:user:status")
    @PutMapping("/admin/user/changeStatus")
    @OptLogger(value = UPDATE)
    @Operation(summary = "修改用户状态")
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
    @Operation(summary = "查看在线用户")
    @SaIgnore
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
    @OptLogger(value = KICK)
    @Operation(summary = "下线用户")
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
    @Operation(summary = "修改管理员密码")
    @PutMapping("/admin/password")
    public Result<?> updateAdminPassword(@Validated @RequestBody PasswordReq password) {
        userService.updateAdminPassword(password);
        return Result.success();
    }

}