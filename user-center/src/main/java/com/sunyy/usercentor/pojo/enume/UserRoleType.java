package com.sunyy.usercentor.pojo.enume;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author ovi
 * @since 2024/8/17
 */
@Getter
public enum UserRoleType {
    SupremeWill(0, "无上意志"),
    ADMIN(1, "管理员"),
    USER(2, "普通用户"),
    None(-1, "无法识别的类型");

    private final Integer code;
    private final String desc;

    UserRoleType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static UserRoleType getUserRoleType(Integer code) {
        if (code == null || code < 0) {
            return None;
        }
        return Arrays.stream(UserRoleType.values())
                .filter(v -> Objects.equals(v.getCode(), code))
                .findFirst()
                .orElse(None);
    }
}
