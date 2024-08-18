package com.sunyy.usercentor.common.anno;

import java.lang.annotation.*;

/**
 * 权限控制
 *
 * @author ovi
 * @since 2024/8/17
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthNeed {

    /**
     * 权限符号
     * 控制每个方法的请求权限
     * 在应用：aa下，有controller：bb，方法：cc，那么value的值就是aa:bb:cc
     * 这个值控制了角色的一个权限
     */
    String[] value() default {};

}
