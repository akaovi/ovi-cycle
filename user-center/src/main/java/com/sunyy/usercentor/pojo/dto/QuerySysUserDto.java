package com.sunyy.usercentor.pojo.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author ovi
 * @since 2024/8/17
 */
@Data
@ToString(callSuper = true)
public class QuerySysUserDto extends PageDto {

    /**
     * 用户id 雪花算法
     */
    private Long userId;

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
     * 账号状态（0为正常，1禁用）
     */
    private Integer accountStatus;

    /**
     * 删除标志（0为未删除，1删除）
     */
    private Integer isDeleted;

    /**
     * 时间查询（创建时间）
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 一个json数组，角色权限符号
     */
    private String userPermission;

}
