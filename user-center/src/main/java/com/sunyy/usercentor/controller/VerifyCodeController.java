package com.sunyy.usercentor.controller;

import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.pojo.enume.VerifyCodeType;
import com.sunyy.usercentor.service.VerifyCodeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ovi
 * @since 2024/8/18
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/verify/code")
public class VerifyCodeController {

    private VerifyCodeService verifyCodeService;

    /**
     * 发送注册的验证码
     */
    @PostMapping("/email/send-register-code")
    @Operation(summary = "注册验证码发送")
    public Message sendRegisterCode(@RequestParam String email) {
        if (StringUtils.isBlank(email)) {
            return Message.error("邮箱不能为空");
        }
        return verifyCodeService.sendCodeToEmail(email, VerifyCodeType.REGISTER);
    }

    /**
     * 登录验证码发送
     */
    @PostMapping("/email/send-login-code")
    @Operation(summary = "登录验证码发送")
    public Message sendLoginCode(@RequestParam String email) {
        if (StringUtils.isBlank(email)) {
            return Message.error("邮箱不能为空");
        }
        return verifyCodeService.sendCodeToEmail(email, VerifyCodeType.LOGIN);
    }

    /**
     * 登录验证码发送
     */
    @PostMapping("/email/send-change-email-code")
    @Operation(summary = "更换邮箱验证码发送")
    public Message sendChangeEmailCode(@RequestParam String email) {
        if (StringUtils.isBlank(email)) {
            return Message.error("邮箱不能为空");
        }
        return verifyCodeService.sendCodeToEmail(email, VerifyCodeType.CHANGE_EMAIL);
    }

    /**
     * 登录验证码发送
     */
    @PostMapping("/email/send-change-pwd-code")
    @Operation(summary = "更换密码验证码发送")
    public Message sendChangePwdCode(@RequestParam String email) {
        if (StringUtils.isBlank(email)) {
            return Message.error("邮箱不能为空");
        }
        return verifyCodeService.sendCodeToEmail(email, VerifyCodeType.CHANGE_PWD);
    }
}
