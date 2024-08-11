package com.sunyy.usercentor.mapStruct;

import com.sunyy.usercentor.pojo.dto.RegisterUserDto;
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
    SysUser registerUserDtoToEntity(RegisterUserDto registerUserDto);
}
