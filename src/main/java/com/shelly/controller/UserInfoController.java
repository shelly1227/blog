package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.common.Result;
import com.shelly.entity.vo.Request.EmailReq;
import com.shelly.entity.vo.Request.UserInfoReq;
import com.shelly.entity.vo.Request.UserReq;
import com.shelly.entity.vo.Response.UserInfoResp;
import com.shelly.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserInfoController {
    private final UserService userService;
    @PostMapping("/user/avatar")
    @SaCheckPermission(value = "user:avatar:update")
    public Result<String> updateUserAvatar(@RequestParam(value = "file") MultipartFile file) {
        return Result.success(userService.updateUserAvatar(file));
    }

    @PutMapping("/user/email")
    @SaCheckPermission(value = "user:email:update")
    public Result<?> updateUserEmail(@Validated @RequestBody EmailReq email) {
        userService.updateUserEmail(email);
        return Result.success();
    }

    @GetMapping("/user/getUserInfo")
    @SaCheckLogin
    //TODO
    public Result<UserInfoResp> getUserInfo() {
        return Result.success(userService.getUserInfo());
    }

    @PutMapping("/user/info")
    @SaCheckPermission(value = "user:info:update")
    public Result<?> updateUserInfo(@Validated @RequestBody UserInfoReq userInfo) {
        userService.updateUserInfo(userInfo);
        return Result.success();
    }

    @PutMapping("/user/password")
    public Result<?> updatePassword(@Validated @RequestBody UserReq user) {
        userService.updatePassword(user);
        return Result.success();
    }


}
