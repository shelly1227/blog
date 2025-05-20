package com.shelly.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    /**
     * 邮件服务器地址，例如 smtp.qq.com
     */
    private String host;

    /**
     * 邮箱用户名（发件人地址）
     */
    private String username;

    /**
     * 邮箱授权码（不是登录密码）
     */
    private String password;
}
