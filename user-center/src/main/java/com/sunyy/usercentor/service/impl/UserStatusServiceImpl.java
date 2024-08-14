package com.sunyy.usercentor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunyy.usercentor.common.MyException;
import com.sunyy.usercentor.pojo.entity.UserStatus;
import com.sunyy.usercentor.service.UserStatusService;
import com.sunyy.usercentor.mapper.UserStatusMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 60382
* @description 针对表【user_status】的数据库操作Service实现
* @createDate 2024-08-11 23:19:23
*/
@Service
public class UserStatusServiceImpl extends ServiceImpl<UserStatusMapper, UserStatus>
    implements UserStatusService{

    @Resource
    private UserStatusMapper userStatusMapper;

    @Override
    public boolean checkUserIsLogin(Long userId) {
        if (userId == null) {
            throw new MyException("校验用户是否登录时，userId为空");
        }
        List<UserStatus> users = list(new QueryWrapper<UserStatus>().eq("user_id", userId));
        if (users == null || users.isEmpty()) {
            return false;
        }
        UserStatus userStatus = users.get(0);
        return userStatus.getOnline() == 1;
    }

    @Override
    public UserStatus getUserStatusByEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return null;
        }
        return userStatusMapper.getUserStatusByEmail(email);
    }


}




