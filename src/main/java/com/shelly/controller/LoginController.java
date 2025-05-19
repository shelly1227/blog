package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.shelly.annotation.AccessLimit;
import com.shelly.common.Result;
import com.shelly.entity.vo.req.LoginReq;
import com.shelly.entity.vo.req.RegisterReq;
import com.shelly.service.impl.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "登录模块")
//checked
public class LoginController {


    private final LoginService loginService;

    /**
     * 用户登录
     *
     * @param login 登录参数
     * @return {@link String} Token
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<String> login(@Validated @RequestBody LoginReq login) {
        return Result.success(loginService.login(login));
    }

    /**
     * 用户退出
     */
    @SaCheckLogin
    @GetMapping("/logout")
    @Operation(summary = "用户退出")
    public Result<Void> logout() {
        StpUtil.logout();
        return Result.success();
    }

    /**
     * 发送邮箱验证码
     *
     * @return {@link Result<>}
     */
    @AccessLimit(seconds = 60, maxCount = 1)
    @GetMapping("/code")
    @Operation(summary = "发送邮箱验证码")
    public Result<?> sendCode(String username) {
        loginService.sendCode(username);
        return Result.success();
    }

    /**
     * 用户邮箱注册
     *
     * @param register 注册信息
     * @return {@link Result<>}
     */
    @PostMapping("/register")
    @Operation(summary = "用户邮箱注册")
    public Result<?> register(@Validated @RequestBody RegisterReq register) {
        loginService.register(register);
        return Result.success();
    }

    /**
     * Gitee登录
     *
     * @param data 第三方code
     * @return {@link Result<String>} Token
     */
    //TODO
//    @PostMapping("/oauth/gitee")
//    public Result<String> giteeLogin(@RequestBody CodeReq data) {
//        return Result.success(loginService.giteeLogin(data));
//    }
//
//    /**
//     * Github登录
//     *
//     * @param data 第三方code
//     * @return {@link Result<String>} Token
//     */
//    @PostMapping("/oauth/github")
//    public Result<String> githubLogin(@RequestBody CodeReq data) {
//        return Result.success(loginService.githubLogin(data));
//    }
//
//    /**
//     * QQ登录
//     *
//     * @param data 第三方code
//     * @return {@link Result<String>} Token
//     */
//
//    @PostMapping("/oauth/qq")
//    public Result<String> qqLogin(@Validated @RequestBody CodeReq data) {
//        return Result.success(loginService.qqLogin(data));
//    }
}

