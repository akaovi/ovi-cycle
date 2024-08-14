package com.sunyy.usercentor.mapper;

import com.sunyy.usercentor.pojo.entity.UserStatus;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 60382
* @description 针对表【user_status】的数据库操作Mapper
* @createDate 2024-08-11 23:19:23
* @Entity com.sunyy.usercentor.pojo.entity.UserStatus
*/
public interface UserStatusMapper extends BaseMapper<UserStatus> {

    UserStatus getUserStatusByEmail(String email);

}




