package com.sunyy.usercentor.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.pojo.dto.RegisterUserDto;
import com.sunyy.usercentor.pojo.entity.SysUser;
import com.sunyy.usercentor.service.SysUserService;
import com.sunyy.usercentor.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
* @author 60382
* @description 针对表【sys_user】的数据库操作Service实现
* @createDate 2024-07-14 23:54:33
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    @Override
    public Message register(RegisterUserDto registerUserDto) {
        return null;
    }
}




