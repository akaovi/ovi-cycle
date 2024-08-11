package com.sunyy.usercentor.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author ovi
 * @since 2024/8/11
 */
@Data
@ToString
@Configuration
@ConfigurationProperties("user-center")
public class UserCenterConfig {

    /**
     * 雪花算法机器id
     */
    private int workerId;

    /**
     * 雪花算法数据中心id
     */
    private int datacenterId;

    /**
     * 密码盐
     */
    private String pwdSalt;

    /**
     * 验证码过期时间 单位：分钟
     */
    private Integer verifyCodeExpire;

    /**
     * 验证码长度
     */
    private Integer verifyCodeLength;

    /**
     * id生成器
     */
    @Setter(AccessLevel.NONE)
    private Snowflake snowflake;

    @PostConstruct
    public void init() {
        snowflake = IdUtil.getSnowflake(workerId, datacenterId);
    }
}
