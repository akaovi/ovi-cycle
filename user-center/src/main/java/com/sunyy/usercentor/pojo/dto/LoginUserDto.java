package com.sunyy.usercentor.pojo.dto;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author ovi
 * @since 2024/8/11
 */
@Data
@ToString
public class LoginUserDto {

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱地址非法")
    private String email;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Length(min = 8, message = "密码长度不能小于8")
    private String pwd;

    /**
     * 验证码
     */
    @NotBlank(message = "验证码不能为空")
    @Length(min = 6, max = 6, message = "验证码非法")
    private String verifyCode;

}
