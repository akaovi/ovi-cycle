package com.sunyy.usercentor.controller;

import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.common.anno.RequestParamsLog;
import com.sunyy.usercentor.pojo.dto.LoginUserDto;
import com.sunyy.usercentor.pojo.dto.RegisterUserDto;
import com.sunyy.usercentor.pojo.enume.VerifyCodeType;
import com.sunyy.usercentor.service.SysUserService;
import com.sunyy.usercentor.service.VerifyCodeService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ovi
 * @since 2024/8/10
 */
@Slf4j
@RestController
@RequestMapping("/api/system-user")
public class UserCenterController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private VerifyCodeService verifyCodeService;

    /**
     * 发送注册的验证码
     */
    @RequestParamsLog
    @PostMapping("/email/send-register-code")
    @Operation(summary = "注册验证码发送")
    public Message sendRegisterCode(@RequestParam String email) {
        if (StringUtils.isBlank(email)) {
            return Message.error("邮箱不能为空");
        }
        return verifyCodeService.sendCodeToEmail(email, VerifyCodeType.REGISTER);
    }

    /**
     * 注册
     */
    @RequestParamsLog
    @PostMapping("/email/register")
    @Operation(summary = "注册")
    public Message registerEmail(@RequestBody RegisterUserDto registerUserDto, HttpServletRequest request, HttpServletResponse response) {
        if (registerUserDto == null
                || StringUtils.isAnyBlank(registerUserDto.getEmail(), registerUserDto.getVerifyCode(), registerUserDto.getPwd())) {
            return Message.error("参数为空");
        }
        return sysUserService.registerEmail(registerUserDto);
    }

    /**
     * 发送注册的验证码
     */
    @RequestParamsLog
    @PostMapping("/email/send-login-code")
    @Operation(summary = "登录验证码发送")
    public Message sendLoginCode(@RequestParam String email) {
        if (StringUtils.isBlank(email)) {
            return Message.error("邮箱不能为空");
        }
        return verifyCodeService.sendCodeToEmail(email, VerifyCodeType.LOGIN);
    }

    /**
     * 登录
     */
    @RequestParamsLog
    @PostMapping("/email/login")
    @Operation(summary = "登录")
    public Message loginEmail(@RequestBody LoginUserDto loginUserDto, HttpServletRequest request, HttpServletResponse response) {
        if (loginUserDto == null
                || StringUtils.isAnyBlank(loginUserDto.getPwd(), loginUserDto.getVerifyCode(), loginUserDto.getEmail())) {
            return Message.error("参数为空");
        }
        return sysUserService.loginEmail(loginUserDto, request, response);
    }

    /**
     * 检查是否登录
     */
    @RequestParamsLog
    @PostMapping("/email/check/login")
    @Operation(summary = "检查是否登录")
    public Message checkLogin(@RequestParam String email, HttpServletRequest request) {
        if (StringUtils.isBlank(email)) {
            return Message.error("邮箱不能为空");
        }
        return sysUserService.checkLogin(email, request);
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    public Message add() {
        log.info("add");
        return null;
    }

    @PostMapping("/update")
    public Message update() {
        log.info("update");
        return null;
    }

    @PostMapping("/delete")
    public Message delete() {
        log.info("delete");
        return null;
    }

    @PostMapping("/get")
    public Message get() {
        log.info("get");
        return null;
    }

    @PostMapping("/list")
    public Message list() {
        log.info("list");
        return null;
    }

}
