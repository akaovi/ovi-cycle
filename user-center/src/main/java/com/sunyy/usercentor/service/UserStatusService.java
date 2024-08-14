package com.sunyy.usercentor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunyy.usercentor.pojo.entity.UserStatus;

/**
 * @author 60382
 * @description 针对表【user_status】的数据库操作Service
 * @createDate 2024-08-11 23:19:23
 */
public interface UserStatusService extends IService<UserStatus> {

    /**
     * 检查用户是否登录
     *
     * @param userId 唯一标识
     * @return true 在线 false 离线
     */
    boolean checkUserIsLogin(Long userId);

    /**
     * 根据邮箱获取用户状态
     *
     * @param email 邮箱
     * @return 用户状态
     */
    UserStatus getUserStatusByEmail(String email);

}
