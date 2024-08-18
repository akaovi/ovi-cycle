package com.sunyy.usercentor.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.pojo.dto.*;
import com.sunyy.usercentor.pojo.entity.SysUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

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
     * @param request      request
     * @param response     response
     */
    Message loginEmail(@Valid LoginUserDto loginUserDto, HttpServletRequest request, HttpServletResponse response);

    /**
     * 校验是否登录
     *
     * @param email   邮箱
     * @param request request
     */
    Message checkLogin(String email, HttpServletRequest request);

    /**
     * 以邮箱为账号，快速新增一个角色
     *
     * @param email 邮箱
     * @param pwd   密码
     */
    SysUser addUser(String email, String pwd);

    /**
     * 更新用户信息
     *
     * @param updateUserBaseInfoDto 更新信息
     */
    Message updateSysUser(@Valid UpdateUserBaseInfoDto updateUserBaseInfoDto);

    /**
     * 禁用账号
     *
     * @param userId 用户id
     * @return true 禁用成功 反之失败
     */
    boolean disableAccount(Long userId);

    /**
     * 修改密码
     *
     * @param changePwdDto 修改密码信息
     * @return true 修改成功 反之失败
     */
    boolean changePwd(@Valid ChangePwdDto changePwdDto);

    /**
     * 更新用户角色
     *
     * @param userId   用户id
     * @param userRole 用户角色
     * @return true 修改成功 反之失败
     */
    boolean updateUserRole(Long userId, Integer userRole);

    /**
     * 更新用户权限
     *
     * @param userId         用户id
     * @param userPermission 用户权限
     * @return true 修改成功 反之失败
     */
    boolean updateUserPermission(Long userId, List<String> userPermission);

    /**
     * 更新用户头像
     *
     * @param userId 用户id
     * @param avatar 头像
     * @return true 修改成功 反之失败
     */
    boolean updateUserAvatar(Long userId, MultipartFile avatar);

    /**
     * 更改邮箱绑定
     *
     * @param bindEmailChangeDto 邮箱绑定信息
     * @return true 修改成功 反之失败
     */
    Message ChangeEmailBinding(@Valid BindEmailChangeDto bindEmailChangeDto);

    /**
     * 条件分页查询
     *
     * @param querySysUserDto 查询条件
     * @return 分页数据
     */
    Page<SysUser> list(QuerySysUserDto querySysUserDto);
}
