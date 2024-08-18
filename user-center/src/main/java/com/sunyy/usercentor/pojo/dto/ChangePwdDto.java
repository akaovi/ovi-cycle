package com.sunyy.usercentor.pojo.dto;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ovi
 * @since 2024/8/18
 */
@Data
@ToString
public class ChangePwdDto {

    @NotNull(message = "用户id不能为空")
    private Long userId;

    @NotBlank(message = "新密码不能为空")
    @Length(min = 8, message = "密码长度至少大于8位")
    private String newPwd;

    @NotBlank(message = "验证码不能为空")
    @Length(min = 6, max = 6, message = "验证码异常")
    private String code;
}
