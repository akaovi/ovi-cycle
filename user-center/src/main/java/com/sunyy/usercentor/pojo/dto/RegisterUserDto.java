package com.sunyy.usercentor.pojo.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author ovi
 * @since 2024/8/10
 */
@Data
@ToString
public class RegisterUserDto {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别 0 男 1 女
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String pwd;

}
