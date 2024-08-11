package com.sunyy.usercentor.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName verify_code
 */
@TableName(value = "verify_code")
@Data
public class VerifyCode implements Serializable {
    /**
     * 邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 验证码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 过期时间
     */
    @TableField(value = "expiration_date")
    private LocalDateTime expirationDate;

    /**
     * 0 注册验证码
     */
    @TableField(value = "code_type")
    private Integer codeType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}