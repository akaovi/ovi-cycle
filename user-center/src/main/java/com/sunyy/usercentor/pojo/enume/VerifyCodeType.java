package com.sunyy.usercentor.pojo.enume;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 验证码类型
 *
 * @author ovi
 * @since 2024/8/11
 */
@Getter
public enum VerifyCodeType {

    REGISTER(0, "注册时使用的验证码", "注册验证码"),
    LOGIN(1, "登录时使用的验证码", "登录验证码"),
    CHANGE_PWD(2, "更换密码时使用的验证码", "更改密码验证码"),
    NoneType(-1, "未知类型", "")
    ;

    /**
     * code
     */
    private final Integer code;
    /**
     * 描述
     */
    private final String desc;

    /**
     * 邮件主题
     */
    private final String subject;

    VerifyCodeType(Integer code, String desc, String subject) {
        this.code = code;
        this.desc = desc;
        this.subject = subject;
    }

    public static VerifyCodeType getVerifyCodeType(Integer code) {
        if (code == null || code < 0) {
            return NoneType;
        }
        return Arrays.stream(VerifyCodeType.values())
                .filter(v -> Objects.equals(v.getCode(), code))
                .findFirst()
                .orElse(NoneType);
    }

}
