package com.sunyy.usercentor.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.sunyy.usercentor.pojo.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * 登录缓存
 *
 * @author ovi
 * @since 2024/8/12
 */
@Slf4j
@Configuration
public class CaffeineConfig {

    @Bean("sysCaffeineCache")
    public Cache<String, SysUser> sysCaffeineCache() {
        return Caffeine.newBuilder()
                .expireAfter(new Expiry<String, SysUser>() {
                    @Override
                    public long expireAfterCreate(@NonNull String key, @NonNull SysUser value, long currentTime) {
                        log.debug("创建时设置过期时间, key = {}, value = {}", key, value);
                        return TimeUnit.MINUTES.toNanos(60L);
                    }

                    @Override
                    public long expireAfterUpdate(@NonNull String key, @NonNull SysUser value, long currentTime, @NonNegative long currentDuration) {
                        log.debug("被更新时重置过期时间, key = {}, value = {}", key, value);
                        return TimeUnit.MINUTES.toNanos(60L);
                    }

                    @Override
                    public long expireAfterRead(@NonNull String key, @NonNull SysUser value, long currentTime, @NonNegative long currentDuration) {
                        log.debug("读取时重置过期时间, key = {}, value = {}", key, value);
                        return TimeUnit.MINUTES.toNanos(60L);
                    }
                })
                .maximumSize(10_000)
                .build();
    }
}
