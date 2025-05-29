package com.shelly.config;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.interceptor.SaInterceptor;
import com.shelly.interceptor.AccessLimitInterceptor;
import com.shelly.interceptor.PageableInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Autowired
    private AccessLimitInterceptor accessLimitInterceptor;

    private final String[] EXCLUDE_PATH_PATTERNS = {
            "/swagger-resources",
            "/webjars/**",
            "/v2/api-docs",
            "/doc.html",
            "/favicon.ico",
            "/login",
            "/oauth/*",
    };
    /**
     * 注册 Sa-Token 拦截器，打开注解式鉴权功能
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册分页拦截器
        registry.addInterceptor(new PageableInterceptor());
        // 注册Redis限流器
        registry.addInterceptor(accessLimitInterceptor);

        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        registry.addInterceptor(new SaInterceptor().setAuth(r -> {
                    // ---------- 设置一些安全响应头 ----------
                    SaHolder.getResponse()
                            .setServer("server")
                            .setHeader("X-Frame-Options", "SAMEORIGIN")
                            .setHeader("X-XSS-Protection", "1; mode=block")
                            .setHeader("X-Content-Type-Options", "nosniff")
                            .setHeader("X-Permitted-Cross-Domain-Policies", "none")
                            .setHeader("X-Requested-With", "?")
                            .setHeader("X-Powered-By", "JDK 17")
                            .setHeader("Referrer-Policy", "strict-origin-when-cross-origin")
                            .setHeader("Sec-Fetch-Dest", "empty")
                            .setHeader("Sec-Fetch-Mode", "cors")
                            .setHeader("Sec-Fetch-Site", "same-origin")
                            .setHeader("Sec-GPC", "1");
                }))
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATH_PATTERNS);
    }
}
