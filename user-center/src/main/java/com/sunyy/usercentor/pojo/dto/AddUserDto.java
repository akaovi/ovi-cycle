package com.sunyy.usercentor.pojo.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author ovi
 * @since 2024/8/18
 */
@Data
@ToString
public class AddUserDto {
    /**
     * 姓名
     */
    private String username;

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
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 给个默认头像
     */
    private String avatar;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 签名
     */
    private String signature;

    /**
     * GitHub
     */
    private String github;

    /**
     * Gitee
     */
    private String gitee;

    /**
     * 博客
     */
    private String blog;

    /**
     * 一个json数组，角色权限符号
     */
    private String userPermission;

    /**
     * 用户角色 0-无上意志 1-管理员 2-普通用户
     */
    private Integer userRole;
}
