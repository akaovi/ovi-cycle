package com.sunyy.usercentor.controller;

import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.common.anno.RequestParamsLog;
import com.sunyy.usercentor.pojo.dto.RegisterUserDto;
import com.sunyy.usercentor.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
     * 注册
     */
    @RequestParamsLog
    @PostMapping("/register")
    @Operation(summary = "注册")
    public Message register(@RequestBody RegisterUserDto registerUserDto, HttpServletRequest request, HttpServletResponse response) {
        if (registerUserDto == null) {
            return Message.error("参数为空");
        }
        if (StringUtils.isBlank(registerUserDto.getEmail())) {
            return Message.error("注册邮箱不能为空");
        }
        return sysUserService.register(registerUserDto);
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    public Message login() {
        log.info("login");
        return null;
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
