package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.common.Result;
import com.shelly.entity.vo.req.EmailReq;
import com.shelly.entity.vo.req.UserInfoReq;
import com.shelly.entity.vo.req.UserReq;
import com.shelly.entity.vo.res.UserInfoResp;
import com.shelly.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "用户信息模块")
public class UserInfoController {
    private final UserService userService;
    @PostMapping("/user/avatar")
    @SaCheckPermission(value = "user:avatar:update")
    @Operation(summary = "上传用户头像")
    public Result<String> updateUserAvatar(@RequestParam(value = "file") MultipartFile file) {
        return Result.success(userService.updateUserAvatar(file));
    }

    @PutMapping("/user/email")
    @SaCheckPermission(value = "user:email:update")
    @Operation(summary = "修改用户邮箱")
    public Result<?> updateUserEmail(@Validated @RequestBody EmailReq email) {
        userService.updateUserEmail(email);
        return Result.success();
    }

    @GetMapping("/user/getUserInfo")
    @SaCheckLogin
    @Operation(summary = "获取用户信息")
    public Result<UserInfoResp> getUserInfo() {
        return Result.success(userService.getUserInfo());
    }

    @PutMapping("/user/info")
    @Operation(summary = "修改用户信息")
    @SaCheckPermission(value = "user:info:update")
    public Result<?> updateUserInfo(@Validated @RequestBody UserInfoReq userInfo) {
        userService.updateUserInfo(userInfo);
        return Result.success();
    }

    @PutMapping("/user/password")
    @Operation(summary = "修改用户密码")
    public Result<?> updatePassword(@Validated @RequestBody UserReq user) {
        userService.updatePassword(user);
        return Result.success();
    }


}
