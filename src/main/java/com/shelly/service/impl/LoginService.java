package com.shelly.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.shelly.common.ServiceException;
import com.shelly.constants.CommonConstant;

import com.shelly.entity.dto.MailDTO;
import com.shelly.entity.pojo.SiteConfig;
import com.shelly.entity.pojo.User;
import com.shelly.entity.pojo.UserRole;
import com.shelly.entity.vo.req.LoginReq;
import com.shelly.entity.vo.req.RegisterReq;
import com.shelly.entity.vo.res.OnlineUserResp;
import com.shelly.enums.LoginTypeEnum;
import com.shelly.enums.RedisConstants;
import com.shelly.enums.RoleEnum;
import com.shelly.mapper.UserMapper;
import com.shelly.mapper.UserRoleMapper;
import com.shelly.service.SiteConfigService;
import com.shelly.utils.IpUtils;
import com.shelly.utils.RedisUtil;
import com.shelly.utils.SecurityUtils;
import com.shelly.utils.UserAgentUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static cn.hutool.extra.servlet.JakartaServletUtil.getClientIP;
import static com.shelly.enums.ZoneEnum.SHANGHAI;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final UserMapper userMapper;
    private final RedisUtil redisUtil;
    private final EmailService emailService;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final UserRoleMapper userRoleMapper;
    private final SiteConfigService siteConfigService;
    private final HttpServletRequest request;
    public String login(LoginReq login) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId)
                .eq(User::getUsername, login.getUsername())
                .eq(User::getPassword, SecurityUtils.sha256Encrypt(login.getPassword())));
        Assert.notNull(user, "用户不存在或密码错误");
        // 校验指定账号是否已被封禁，如果被封禁则抛出异常 `DisableServiceException`
        log.info("用户登录开始");
        StpUtil.checkDisable(user.getId());
        StpUtil.login(user.getId());
        redisUtil.set(RedisConstants.USER_TOKEN, String.valueOf(user.getId()), StpUtil.getTokenValue());
        redisUtil.set(RedisConstants.USER, String.valueOf(user.getId()), JSON.toJSONString(user));
        log.info("用户登录成功");
        String ipAddress = getClientIP(request);
        CompletableFuture.runAsync(() -> {
            try {
                recordLoginInfo(user.getId(), StpUtil.getTokenValue(), ipAddress);
            } catch (Exception e) {
                // 记录日志时出现异常，不影响主流程
                log.error("登录日志记录失败: " + e.getMessage());
            }
        }, threadPoolTaskExecutor);
        return StpUtil.getTokenValue();
    }
    private void recordLoginInfo(int loginId, String tokenValue, String ipAddress) {
        log.info("监听触发......");
        // 查询用户昵称、头像
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getAvatar, User::getNickname)
                .eq(User::getId, loginId));
        // 解析浏览器和系统
        Map<String, String> userAgentMap = UserAgentUtils.parseOsAndBrowser(request.getHeader("User-Agent"));

        // 获取登录 IP 和地址
        String ipSource = IpUtils.getIpSource(ipAddress);
        // 登录时间
        LocalDateTime loginTime = LocalDateTime.now(ZoneId.of(SHANGHAI.getZone()));
        // 构建在线用户信息
        OnlineUserResp onlineUserResp = OnlineUserResp.builder()
                .id(loginId)
                .token(tokenValue)
                .avatar(user.getAvatar())
                .nickname(user.getNickname())
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .os(userAgentMap.get("os"))
                .browser(userAgentMap.get("browser"))
                .loginTime(loginTime)
                .build();
        // 更新用户登录信息
        User newUser = User.builder()
                .id(loginId)
                .ipAddress(ipAddress)
                .ipSource(ipSource)
                .loginTime(loginTime)
                .build();
        userMapper.updateById(newUser);
        redisUtil.set(RedisConstants.USER_INFO, String.valueOf(loginId), onlineUserResp);
    }


    public void sendCode(String username) {
        Assert.isTrue(Validator.isEmail(username), "请输入正确的邮箱！");
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 6);
        String code = randomGenerator.generate();
        MailDTO mailDTO = MailDTO.builder()
                .toEmail(username)
                .subject(CommonConstant.CAPTCHA)
                .content("您的验证码为 " + code + " 有效期为5分钟")
                .build();
//        // 验证码存入消息队列，利用消息队列异步发送
//        rabbitTemplate.convertAndSend(MqConstant.EMAIL_EXCHANGE, MqConstant.EMAIL_SIMPLE_KEY, mailDTO);
        emailService.sendSimpleMail(mailDTO);
        // 异步直接发送邮件
        //CompletableFuture.runAsync(() -> emailService.sendSimpleMail(mailDTO), threadPoolTaskExecutor);
        // 将生成的验证码存入Redis
        String redisKey = RedisConstants.VERIFICATION_CODE.getKey() + username;
        redisUtil.set(redisKey, code);
        redisUtil.expire(redisKey, 300);
    }

    public void register(RegisterReq register) {
        verifyCode(register.getUsername(), register.getCode());
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername)
                .eq(User::getUsername, register.getUsername()));
        Assert.isNull(user, "邮箱已注册！");
        SiteConfig siteConfig = redisUtil.get(RedisConstants.SITE_SETTING.getKey(), SiteConfig.class);
        if(siteConfig == null){
            siteConfig = siteConfigService.lambdaQuery()
                    .eq(SiteConfig::getId, 1)
                    .one();
            redisUtil.set(RedisConstants.SITE_SETTING.getKey(), siteConfig, RedisConstants.SITE_SETTING.getTtl(), RedisConstants.SITE_SETTING.getTimeUnit());
        }
        // 添加用户
        User newUser = User.builder()
                .username(register.getUsername())
                .email(register.getUsername())
                .nickname(CommonConstant.USER_NICKNAME + IdWorker.getId())
                .avatar(siteConfig.getUserAvatar())
                .password(SecurityUtils.sha256Encrypt(register.getPassword()))
                .loginType(LoginTypeEnum.EMAIL.getLoginType())
                .isDisable(CommonConstant.FALSE)
                .build();
        userMapper.insert(newUser);
        // 绑定用户角色
        UserRole userRole = UserRole.builder()
                .userId(newUser.getId())
                .roleId(RoleEnum.USER.getRoleId())
                .build();
        userRoleMapper.insert(userRole);
    }
    public void verifyCode(String username, String code) {
        String redisKeyPrefix = RedisConstants.VERIFICATION_CODE.getKey();
        String redisKey = redisKeyPrefix + username;
        if (redisUtil.getObject(redisKey) == null || redisUtil.getTime(redisKey) == 0) {
            throw new ServiceException("验证码过期");
        }
        // 验证验证码是否匹配
        if (!code.equals(redisUtil.getObject(redisKey).toString())) {
            throw new ServiceException("验证码错误");
        }
    }
}
