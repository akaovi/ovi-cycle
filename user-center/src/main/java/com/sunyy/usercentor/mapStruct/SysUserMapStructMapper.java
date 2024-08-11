package com.sunyy.usercentor.mapStruct;

import com.sunyy.usercentor.pojo.entity.SysUser;
import com.sunyy.usercentor.pojo.vo.SysUserVo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


/**
 * @author ovi
 * @since 2024/7/17
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SysUserMapStructMapper {

    SysUserVo toSysUserVo(SysUser sysUser);
}
