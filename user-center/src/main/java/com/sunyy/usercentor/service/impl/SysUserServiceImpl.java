package com.sunyy.usercentor.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.config.UserCenterConfig;
import com.sunyy.usercentor.mapStruct.SysUserMapStructMapper;
import com.sunyy.usercentor.mapper.SysUserMapper;
import com.sunyy.usercentor.pojo.dto.LoginUserDto;
import com.sunyy.usercentor.pojo.dto.RegisterUserDto;
import com.sunyy.usercentor.pojo.entity.SysUser;
import com.sunyy.usercentor.pojo.enume.VerifyCodeType;
import com.sunyy.usercentor.service.SysUserService;
import com.sunyy.usercentor.service.UserStatusService;
import com.sunyy.usercentor.service.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 60382
 * @description 针对表【sys_user】的数据库操作Service实现
 * @createDate 2024-07-14 23:54:33
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {
    private static final DateTimeFormatter ymdhmsNoSymbol = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Digester digesterSha256 = SecureUtil.sha256();

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private UserCenterConfig userCenterConfig;

    @Lazy
    @Resource
    private VerifyCodeService verifyCodeService;

    @Resource
    private SysUserMapStructMapper sysUserMapStructMapper;

    @Resource
    private UserStatusService userStatusService;

    @Override
    public Message registerEmail(@Valid RegisterUserDto registerUserDto) {
        boolean b = verifyCodeService.verifyCode(registerUserDto.getEmail(), registerUserDto.getVerifyCode(), VerifyCodeType.REGISTER.getCode());
        if (!b) {
            return Message.error("验证码错误");
        }
        String encodePwd = digesterSha256.digestHex(registerUserDto.getPwd() + userCenterConfig.getPwdSalt());
        SysUser sysUser = sysUserMapStructMapper.registerUserDtoToEntity(registerUserDto);
        sysUser.setUserId(userCenterConfig.getSnowflake().nextId());
        sysUser.setUsername(registerUserDto.getNickname() + ymdhmsNoSymbol.format(LocalDateTime.now()));
        sysUser.setPwd(encodePwd);
        boolean save = save(sysUser);
        if (save) {
            return Message.ok("注册成功");
        }
        return Message.error("注册失败");
    }

    @Override
    public boolean isEmailRegistered(String email) {
        SysUser sysUser = getOne(new QueryWrapper<SysUser>().eq("email", email));
        return sysUser != null;
    }

    @Override
    public Message loginEmail(LoginUserDto loginUserDto, HttpServletRequest request, HttpServletResponse response) {
        boolean b = verifyCodeService.verifyCode(loginUserDto.getEmail(), loginUserDto.getVerifyCode(), VerifyCodeType.LOGIN.getCode());
        if (!b) {
            return Message.error("验证码错误");
        }
        // 是否已登录, 只允许一次在线
        return null;
    }
}




