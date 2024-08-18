package com.sunyy.usercentor.pojo.dto;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author ovi
 * @since 2024/8/17
 */
@Data
@ToString
public class UpdateUserBaseInfoDto {

    /**
     * 用户id 雪花算法
     */
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 姓名
     */
    private String username;

    /**
     * 昵称
     */
    @Length(min = 1, max = 7, message = "昵称长度不能大于7")
    private String nickname;

    /**
     * 性别 0 男 1 女
     */
    @Min(value = 0, message = "性别非法")
    @Max(value = 1, message = "性别非法")
    private Integer gender;

    /**
     * 年龄
     */
    @Min(value = 0, message = "年龄非法")
    private Integer age;

    /**
     * 签名
     */
    @Length(min = 1, max = 50, message = "签名长度不能大于50")
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
     * 手机号  warn: 后续如果支持手机号登录，需要将这个修改项去掉
     */
    private String phone;


}
