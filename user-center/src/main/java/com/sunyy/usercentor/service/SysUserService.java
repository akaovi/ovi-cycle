package com.sunyy.usercentor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.pojo.dto.LoginUserDto;
import com.sunyy.usercentor.pojo.dto.RegisterUserDto;
import com.sunyy.usercentor.pojo.entity.SysUser;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author 60382
 * @description 针对表【sys_user】的数据库操作Service
 * @createDate 2024-07-14 23:54:33
 */
@Validated
public interface SysUserService extends IService<SysUser> {

    /**
     * 注册
     *
     * @param registerUserDto 注册信息
     */
    Message registerEmail(@Valid RegisterUserDto registerUserDto);

    /**
     * 判断邮箱是否注册
     *
     * @return true 已注册 false 未注册
     */
    boolean isEmailRegistered(String email);

    /**
     * 登录
     *
     * @param loginUserDto 登录信息
     * @param request request
     * @param response response
     */
    Message loginEmail(@Valid LoginUserDto loginUserDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 校验是否登录
     *
     * @param email 邮箱
     * @param request request
     */
    Message checkLogin(String email, HttpServletRequest request);
}
