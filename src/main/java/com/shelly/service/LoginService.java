package com.shelly.service;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.shelly.constants.CommonConstant;
import com.shelly.constants.MqConstant;
import com.shelly.constants.RedisConstant;

import com.shelly.entity.dto.MailDTO;
import com.shelly.entity.pojo.SiteConfig;
import com.shelly.entity.pojo.User;
import com.shelly.entity.pojo.UserRole;
import com.shelly.entity.vo.req.LoginReq;
import com.shelly.entity.vo.req.RegisterReq;
import com.shelly.enums.LoginTypeEnum;
import com.shelly.enums.RoleEnum;
import com.shelly.mapper.UserMapper;
import com.shelly.mapper.UserRoleMapper;
import com.shelly.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserMapper userMapper;
    private final RedisService redisService;
    private final EmailService emailService;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final UserRoleMapper userRoleMapper;
    private final RabbitTemplate rabbitTemplate;
    public String login(LoginReq login) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId)
                .eq(User::getUsername, login.getUsername())
                .eq(User::getPassword, SecurityUtils.sha256Encrypt(login.getPassword())));
        Assert.notNull(user, "用户不存在或密码错误");
        // 校验指定账号是否已被封禁，如果被封禁则抛出异常 `DisableServiceException`
        StpUtil.checkDisable(user.getId());
        StpUtil.login(user.getId());
        return StpUtil.getTokenValue();
    }

    public void sendCode(String username) {
        Assert.isTrue(Validator.isEmail(username), "请输入正确的邮箱！");
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 6);
        String code = randomGenerator.generate();
        MailDTO mailDTO = MailDTO.builder()
                .toEmail(username)
                .subject(CommonConstant.CAPTCHA)
                .content("您的验证码为 " + code + " 有效期为" + RedisConstant.CODE_EXPIRE_TIME + "分钟")
                .build();
        // 验证码存入消息队列，利用消息队列异步发送
        rabbitTemplate.convertAndSend(MqConstant.EMAIL_EXCHANGE, MqConstant.EMAIL_SIMPLE_KEY, mailDTO);
        // 异步直接发送邮件
        //CompletableFuture.runAsync(() -> emailService.sendSimpleMail(mailDTO), threadPoolTaskExecutor);
        // 验证码存入redis
        redisService.setObject(RedisConstant.CODE_KEY + username, code, RedisConstant.CODE_EXPIRE_TIME, TimeUnit.MINUTES);
    }

    public void register(RegisterReq register) {
        verifyCode(register.getUsername(), register.getCode());
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername)
                .eq(User::getUsername, register.getUsername()));
        Assert.isNull(user, "邮箱已注册！");
        SiteConfig siteConfig = redisService.getObject(RedisConstant.SITE_SETTING);
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
        String sysCode = redisService.getObject(RedisConstant.CODE_KEY + username);
        Assert.notBlank(sysCode, "验证码未发送或已过期！");
        Assert.isTrue(sysCode.equals(code), "验证码错误，请重新输入！");
    }
}
