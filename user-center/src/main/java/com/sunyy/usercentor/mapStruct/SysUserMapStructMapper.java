package com.sunyy.usercentor.mapStruct;

import com.sunyy.usercentor.pojo.dto.AddUserDto;
import com.sunyy.usercentor.pojo.dto.RegisterUserDto;
import com.sunyy.usercentor.pojo.dto.UpdateUserBaseInfoDto;
import com.sunyy.usercentor.pojo.entity.SysUser;
import com.sunyy.usercentor.pojo.vo.SysUserVo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


/**
 * @author ovi
 * @since 2024/7/17
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysUserMapStructMapper {

    SysUserVo toSysUserVo(SysUser sysUser);

    @Mapping(target = "pwd", ignore = true)
    @Mapping(target = "updateTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isDeleted", expression = "java(0)")
    @Mapping(target = "accountStatus", constant = "0")
    @Mapping(target = "userPermission", constant = "[]")
    @Mapping(target = "userRole", constant = "2")
    SysUser registerUserDtoToEntity(RegisterUserDto registerUserDto);

    @Mapping(target = "pwd", ignore = true)
    @Mapping(target = "updateTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "createTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "isDeleted", expression = "java(0)")
    @Mapping(target = "accountStatus", constant = "0")
    SysUser addUserDtoToEntity(AddUserDto addUserDto);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "updateTime", expression = "java(java.time.LocalDateTime.now())")
    SysUser updateUserBaseInfoDtoToEntity(UpdateUserBaseInfoDto updateUserBaseInfoDto);
}
