package com.sunyy.usercentor;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.sunyy.usercentor.mapStruct.SysUserMapStructMapper;
import com.sunyy.usercentor.pojo.entity.SysUser;
import com.sunyy.usercentor.pojo.vo.SysUserVo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class UserCenterApplicationTests {

    @Resource
    private SysUserMapStructMapper sysUserMapStructMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void testMapperStruct() {

        SysUser sysUser = new SysUser();
        sysUser.setUserId(1111L);
        SysUserVo sysUserVo = sysUserMapStructMapper.toSysUserVo(sysUser);
        System.out.println(sysUserVo);

    }

}
