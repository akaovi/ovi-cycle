package com.sunyy.usercentor.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.mapStruct.SysUserMapStructMapper;
import com.sunyy.usercentor.pojo.dto.*;
import com.sunyy.usercentor.pojo.entity.SysUser;
import com.sunyy.usercentor.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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
    private SysUserMapStructMapper sysUserMapStructMapper;

    /**
     * 注册
     */
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
     * 登录
     */
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
    @PostMapping("/email/check/login")
    @Operation(summary = "检查是否登录")
    public Message checkLogin(@RequestParam String email, HttpServletRequest request) {
        if (StringUtils.isBlank(email)) {
            return Message.error("邮箱不能为空");
        }
        return sysUserService.checkLogin(email, request);
    }

    /**
     * 用于管理员新增用户
     */
    @PostMapping("/add")
    @Operation(summary = "用于管理员新增用户")
    public Message addOneUser(@RequestParam String email, @RequestParam String pwd) {
        if (StringUtils.isAnyBlank(email, pwd)) {
            return Message.error("参数为空");
        }
        SysUser sysUser = sysUserService.addUser(email, pwd);
        return Message.ok(sysUser);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    @Operation(summary = "更新用户信息")
    public Message updateUserInfo(@RequestBody UpdateUserBaseInfoDto updateUserBaseInfoDto) {
        if (updateUserBaseInfoDto == null || updateUserBaseInfoDto.getUserId() == null) {
            return Message.error("重要参数为空");
        }
        return sysUserService.updateSysUser(updateUserBaseInfoDto);
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete/{userId}")
    @Operation(summary = "删除用户")
    public Message deleteOne(@PathVariable Long userId) {
        if (userId == null) {
            return Message.error("参数为空");
        }
        boolean update = sysUserService.update(new UpdateWrapper<SysUser>().eq("user_id", userId).set("is_deleted", 1));
        if (update) {
            return Message.ok("删除成功");
        } else {
            return Message.error("删除失败");
        }
    }

    /**
     * 查询单个用户信息
     */
    @PostMapping("/get/one/{userId}")
    @Operation(summary = "查询单个用户信息")
    public Message getOneUser(@PathVariable Long userId) {
        if (userId == null) {
            return Message.error("参数为空");
        }
        SysUser one = sysUserService.getOne(new UpdateWrapper<SysUser>().eq("user_id", userId).eq("is_deleted", 0));
        if (one == null) {
            return Message.error("用户不存在");
        }
        return Message.ok(sysUserMapStructMapper.toSysUserVo(one));
    }

    /**
     * 分页查询用户
     */
    @PostMapping("/page/list")
    @Operation(summary = "分页查询用户")
    public Message listUsers(@RequestBody QuerySysUserDto querySysUserDto) {
        if (querySysUserDto == null) {
            return Message.error("参数为空");
        }
        return Message.ok(sysUserService.list(querySysUserDto));
    }

    /**
     * 禁用账号
     */
    @PostMapping("/disable/account/{userId}")
    @Operation(summary = "禁用账号")
    public Message disableAccount(@PathVariable Long userId) {
        if (userId == null || userId < 0) {
            return Message.error("用户非法");
        }
        boolean b = sysUserService.disableAccount(userId);
        if (b) {
            return Message.ok("禁用成功");
        } else {
            return Message.error("禁用失败");
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/change/pwd")
    @Operation(summary = "修改密码")
    public Message changePwd(@RequestBody ChangePwdDto changePwdDto) {
        if (changePwdDto.getUserId() == null || changePwdDto.getUserId() < 0) {
            return Message.error("用户非法");
        }
        if (StringUtils.isBlank(changePwdDto.getNewPwd()) || changePwdDto.getNewPwd().trim().length() < 8) {
            return Message.error("密码不能为空且长度不能小于8");
        }
        boolean b = sysUserService.changePwd(changePwdDto);
        if (b) {
            return Message.ok("修改成功");
        } else {
            return Message.error("修改失败");
        }
    }

    /**
     * 更新用户角色
     */
    @PostMapping("/update/role/{userId}")
    @Operation(summary = "更新用户角色")
    public Message updateUserRole(@PathVariable Long userId, @RequestParam Integer userRole) {
        if (userId == null || userId < 0 || userRole == null) {
            return Message.error("参数非法");
        }
        boolean b = sysUserService.updateUserRole(userId, userRole);
        if (b) {
            return Message.ok("修改成功");
        } else {
            return Message.error("修改失败");
        }
    }

    /**
     * 更新用户权限
     */
    @PostMapping("/update/permission/{userId}")
    @Operation(summary = "更新用户权限")
    public Message updateUserPermission(@PathVariable Long userId, @RequestBody List<String> userPermission) {
        if (userId == null || userId < 0) {
            return Message.error("用户非法");
        }
        if (userPermission == null || userPermission.isEmpty()) {
            return Message.error("权限为空");
        }
        boolean b = sysUserService.updateUserPermission(userId, userPermission);
        if (b) {
            return Message.ok("修改成功");
        } else {
            return Message.error("修改失败");
        }
    }

    /**
     * 获取所有权限
     */
    @PostMapping("/get/all/permission")
    @Operation(summary = "获取所有权限")
    public Message getAllPermission() {
        return Message.ok(new ArrayList<String>());
    }

    /**
     * 更新用户头像
     */
    @PostMapping("/update/avatar/{userId}")
    @Operation(summary = "更新用户头像")
    public Message updateUserAvatar(@PathVariable Long userId, @RequestParam MultipartFile avatar) {
        if (userId == null || userId < 0) {
            return Message.error("用户非法");
        }
        if (avatar == null) {
            return Message.error("头像为空");
        }
        if (sysUserService.updateUserAvatar(userId, avatar)) {
            return Message.ok("修改成功");
        } else {
            return Message.error("修改失败");
        }
    }

    /**
     * 更改邮箱绑定
     */
    @PostMapping("/change/email")
    @Operation(summary = "更改邮箱绑定")
    public Message ChangeEmailBinding(@RequestBody BindEmailChangeDto bindEmailChangeDto) {
        if (bindEmailChangeDto.getUserId() == null || bindEmailChangeDto.getUserId() < 0) {
            return Message.error("用户非法");
        }
        return sysUserService.ChangeEmailBinding(bindEmailChangeDto);
    }

}
