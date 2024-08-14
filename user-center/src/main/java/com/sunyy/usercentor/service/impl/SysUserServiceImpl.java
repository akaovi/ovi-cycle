package com.sunyy.usercentor.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.config.UserCenterConfig;
import com.sunyy.usercentor.mapStruct.SysUserMapStructMapper;
import com.sunyy.usercentor.mapper.SysUserMapper;
import com.sunyy.usercentor.pojo.dto.LoginUserDto;
import com.sunyy.usercentor.pojo.dto.RegisterUserDto;
import com.sunyy.usercentor.pojo.entity.SysUser;
import com.sunyy.usercentor.pojo.entity.UserStatus;
import com.sunyy.usercentor.pojo.enume.VerifyCodeType;
import com.sunyy.usercentor.service.SysUserService;
import com.sunyy.usercentor.service.UserStatusService;
import com.sunyy.usercentor.service.VerifyCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    /**
     * 邮箱正则校验
     */
    private static final Pattern EMAIL_REGEX = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

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

    @Resource(name = "sysCaffeineCache")
    private Cache<String, SysUser> sysCaffeineCache;

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
            UserStatus userStatus = new UserStatus();
            userStatus.setUserId(sysUser.getUserId());
            userStatus.setOnline(1);
            userStatusService.save(userStatus);
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
        SysUser loginUser = getOne(new QueryWrapper<SysUser>().eq("email", loginUserDto.getEmail()));
        String token = digesterSha256.digestHex(loginUser.getUserId() + "login" + System.currentTimeMillis());
        boolean update = userStatusService.update(new UpdateWrapper<UserStatus>().eq("user_id", loginUser.getUserId()).set("token", token));
        if (!update) {
            return Message.error("登录失败, token写入失败");
        }
        // 写入缓存
        sysCaffeineCache.put(token, loginUser);
        response.setHeader("Authorization", token);
        return Message.ok(null);
    }

    @Override
    public Message checkLogin(String email, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        String token = request.getHeader("Authorization");
        if (StringUtils.isNotBlank(token)) {
            SysUser ifPresent = sysCaffeineCache.getIfPresent(token);
            if (ifPresent != null) {
                map.put("isLogin", true);
                return Message.ok(map);
            }
        }
        Matcher matcher = EMAIL_REGEX.matcher(email);
        if (!matcher.find()) {
            return Message.error("邮箱格式不正确");
        }
        UserStatus userStatus = userStatusService.getUserStatusByEmail(email);
        if (StringUtils.isBlank(userStatus.getToken())) {
            map.put("isLogin", false);
            return Message.ok(map);
        }
        SysUser ifPresent = sysCaffeineCache.getIfPresent(userStatus.getToken());
        if (ifPresent == null) {
            map.put("isLogin", false);
        } else {
            map.put("isLogin", true);
        }
        return Message.ok(map);
    }


}




