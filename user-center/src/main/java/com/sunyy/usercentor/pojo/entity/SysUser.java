package com.sunyy.usercentor.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName sys_user
 */
@Data
@TableName(value = "sys_user")
public class SysUser implements Serializable {
    /**
     * 用户id 雪花算法
     */
    @TableId(value = "user_id")
    private Long userId;

    /**
     * 姓名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 性别 0 男 1 女
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 年龄
     */
    @TableField(value = "age")
    private Integer age;

    /**
     * 手机号
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 给个默认头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 密码
     */
    @TableField(value = "pwd")
    private String pwd;

    /**
     * 签名
     */
    @TableField(value = "signature")
    private String signature;

    /**
     * 账号状态（0为正常，1禁用）
     */
    @TableField(value = "account_status")
    private Integer accountStatus;

    /**
     * GitHub
     */
    @TableField(value = "github")
    private String github;

    /**
     * Gitee
     */
    @TableField(value = "gitee")
    private String gitee;

    /**
     * 博客
     */
    @TableField(value = "blog")
    private String blog;

    /**
     * 删除标志（0为未删除，1删除）
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}