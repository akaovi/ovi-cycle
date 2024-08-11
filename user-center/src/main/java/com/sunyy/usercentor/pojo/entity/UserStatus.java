package com.sunyy.usercentor.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName user_status
 */
@TableName(value ="user_status")
@Data
public class UserStatus implements Serializable {
    /**
     * user_id 唯一标识
     */
    @TableId(value = "user_id")
    private Long userId;

    /**
     * 上次登录时间
     */
    @TableField(value = "last_login_time")
    private Date lastLoginTime;

    /**
     * 上次登录ip
     */
    @TableField(value = "last_login_ip")
    private String lastLoginIp;

    /**
     * 当前状态 0 下线 1 在线
     */
    @TableField(value = "online")
    private Integer online;

    /**
     * 在线设备信息
     */
    @TableField(value = "devices")
    private String devices;

    /**
     * 在线标识
     */
    @TableField(value = "token")
    private String token;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}