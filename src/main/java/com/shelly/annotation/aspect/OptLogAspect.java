package com.shelly.annotation.aspect;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.shelly.annotation.OptLogger;
import com.shelly.async.AsyncFactory;
import com.shelly.async.manager.AsyncManager;
import com.shelly.entity.pojo.OperationLog;
import com.shelly.utils.IpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.Objects;

import static cn.hutool.extra.servlet.JakartaServletUtil.getClientIP;

@Aspect
@Component
public class OptLogAspect {

    /**
     * 请求开始时间
     */
    ThreadLocal<Long> startTime = new ThreadLocal<>();

    /**
     * 设置操作日志切入点，记录操作日志，在注解的位置切入代码
     */
    @Pointcut("@annotation(com.shelly.annotation.OptLogger)")
    public void optLogPointCut() {
    }

    /**
     * 前置通知
     */
    @Before("optLogPointCut()")
    public void doBefore() {
        // 记录请求开始时间
        startTime.set(System.currentTimeMillis());
    }

    /**
     * 连接点正常返回通知，拦截用户操作日志，正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切面方法的信息
     * @param result    返回结果
     */
    @AfterReturning(value = "optLogPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        Tag tag = (Tag) signature.getDeclaringType().getAnnotation(Tag.class);
        Operation operation = method.getAnnotation(Operation.class);
        OptLogger optLogger = method.getAnnotation(OptLogger.class);
        // 获取request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = Objects.requireNonNull(attributes).getRequest();
        // 日志保存到数据库
        OperationLog operationLog = new OperationLog();
        // 操作模块
        operationLog.setModule(tag.name());
        // 操作描述
        operationLog.setDescription(operation.summary());
        // 操作类型
        operationLog.setType(optLogger.value());
        // 请求URI
        operationLog.setUri(request.getRequestURI());
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的方法名
        String methodName = method.getName();
        methodName = className + "." + methodName;
        // 请求方法
        operationLog.setName(methodName);

        // 请求参数
        if (joinPoint.getArgs()[0] instanceof MultipartFile) {
            operationLog.setParams(((MultipartFile) joinPoint.getArgs()[0]).getOriginalFilename());
        } else {
            operationLog.setParams(JSON.toJSONString(joinPoint.getArgs()));
        }
        // 请求方式
        operationLog.setMethod(Objects.requireNonNull(request).getMethod());
        // 返回数据
        operationLog.setData(JSON.toJSONString(result));
        // 请求用户ID
        operationLog.setUserId(StpUtil.getLoginIdAsInt());
        // 请求用户昵称
        operationLog.setNickname("shelly");
        // 操作ip和操作地址
        String ip = getClientIP(request);
        operationLog.setIpAddress(ip);
        operationLog.setIpSource(IpUtils.getIpSource(ip));
        // 执行耗时
        operationLog.setTimes((int) (System.currentTimeMillis() - startTime.get()));
        startTime.remove();
        // 保存到数据库
        AsyncManager.getInstance().execute(AsyncFactory.recordOperation(operationLog));
    }
}