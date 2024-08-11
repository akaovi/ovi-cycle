package com.sunyy.usercentor.pojo.dto;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

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
    @NotBlank(message = "昵称不能为空")
    @Length(max = 20, message = "昵称长度不能超过20")
    private String nickname;

    /**
     * 性别 0 男 1 女
     */
    @NotNull(message = "需要选择性别, 男或女")
    @Min(value = 0, message = "需要选择性别, 男或女")
    @Max(value = 1, message = "需要选择性别, 男或女")
    private Integer gender;

    /**
     * 年龄
     */
    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄不能小于0")
    private Integer age;

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
