package com.sunyy.usercentor.service;

import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.pojo.dto.RegisterUserDto;
import com.sunyy.usercentor.pojo.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 60382
* @description 针对表【sys_user】的数据库操作Service
* @createDate 2024-07-14 23:54:33
*/
public interface SysUserService extends IService<SysUser> {

    Message register(RegisterUserDto registerUserDto);
}
