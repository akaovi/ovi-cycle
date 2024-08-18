package com.sunyy.usercentor.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.Digester;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.common.MyException;
import com.sunyy.usercentor.config.UserCenterConfig;
import com.sunyy.usercentor.mapStruct.SysUserMapStructMapper;
import com.sunyy.usercentor.mapper.SysUserMapper;
import com.sunyy.usercentor.pojo.dto.*;
import com.sunyy.usercentor.pojo.entity.SysUser;
import com.sunyy.usercentor.pojo.entity.UserStatus;
import com.sunyy.usercentor.pojo.enume.UserRoleType;
import com.sunyy.usercentor.pojo.enume.VerifyCodeType;
import com.sunyy.usercentor.service.SysUserService;
import com.sunyy.usercentor.service.UserStatusService;
import com.sunyy.usercentor.service.VerifyCodeService;
import com.sunyy.usercentor.utils.RegUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
            boolean userStatusB = userStatusService.save(userStatus);
            if (userStatusB) {
                return Message.ok("注册成功");
            }
        }
        return Message.error("注册失败");
    }

    @Override
    public boolean isEmailRegistered(String email) {
        SysUser sysUser = getOne(new QueryWrapper<SysUser>().eq("email", email).eq("is_deleted", 0));
        return sysUser != null;
    }

    @Override
    public Message loginEmail(@Valid LoginUserDto loginUserDto, HttpServletRequest request, HttpServletResponse response) {
        boolean b = verifyCodeService.verifyCode(loginUserDto.getEmail(), loginUserDto.getVerifyCode(), VerifyCodeType.LOGIN.getCode());
        if (!b) {
            return Message.error("验证码错误");
        }
        // 是否已登录, 只允许一次在线
        SysUser loginUser = getOne(new QueryWrapper<SysUser>().eq("email", loginUserDto.getEmail()).eq("is_deleted", 0));
        if (loginUser == null) {
            throw new MyException("用户不存在");
        }
        if (loginUser.getAccountStatus() == 1) {
            return Message.error("账号已被禁用");
        }
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
        if (!RegUtil.checkEmail(email)) {
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

    @Override
    public SysUser addUser(String email, String pwd) {
        if (!RegUtil.checkEmail(email)) {
            throw new MyException("邮箱格式不正确");
        }
        if (isEmailRegistered(email)) {
            throw new MyException("添加用户，邮箱已被注册");
        }
        String encodePwd = digesterSha256.digestHex(pwd + userCenterConfig.getPwdSalt());
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userCenterConfig.getSnowflake().nextId());
        sysUser.setUsername(email.substring(0, 6));
        sysUser.setPwd(encodePwd);
        sysUser.setUserPermission("[]");
        sysUser.setEmail(email);
        boolean save = save(sysUser);
        if (save) {
            UserStatus userStatus = new UserStatus();
            userStatus.setUserId(sysUser.getUserId());
            userStatus.setOnline(1);
            boolean userStatusB = userStatusService.save(userStatus);
            if (userStatusB) {
                return sysUser;
            }
        }
        return null;
    }

    @Override
    public Message updateSysUser(@Valid UpdateUserBaseInfoDto updateUserBaseInfoDto) {
        SysUser sysUser = sysUserMapStructMapper.updateUserBaseInfoDtoToEntity(updateUserBaseInfoDto);
        boolean update = update(sysUser, new UpdateWrapper<SysUser>().eq("user_id", updateUserBaseInfoDto.getUserId()));
        if (update) {
            return Message.ok(null);
        }
        return Message.error("更新失败");
    }

    @Override
    public boolean disableAccount(Long userId) {
        if (userId == null) {
            throw new MyException("userId不能为空");
        }
        return update(new UpdateWrapper<SysUser>().eq("user_id", userId).set("account_status", 1));
    }

    @Override
    public boolean changePwd(@Valid ChangePwdDto changePwdDto) {
        if (changePwdDto.getUserId() == null) {
            throw new MyException("userId不能为空");
        }
        SysUser one = getOne(new QueryWrapper<SysUser>().eq("user_id", changePwdDto.getUserId()).eq("is_deleted", 0));
        if (one == null) {
            throw new MyException("用户不存在");
        }
        if (one.getAccountStatus() == 1) {
            throw new MyException("账号已被禁用");
        }
        if (!verifyCodeService.verifyCode(one.getEmail(), changePwdDto.getCode(), VerifyCodeType.CHANGE_PWD.getCode())) {
            throw new MyException("验证码错误");
        }
        String pwd = changePwdDto.getNewPwd();
        if (StringUtils.isBlank(pwd) || pwd.trim().length() < 8) {
            throw new MyException("密码不能为空且长度不能小于8");
        }
        String encodePwd = digesterSha256.digestHex(pwd.trim() + userCenterConfig.getPwdSalt());
        return update(new UpdateWrapper<SysUser>().eq("user_id", changePwdDto.getUserId()).set("pwd", encodePwd));
    }

    @Override
    public boolean updateUserRole(Long userId, Integer userRole) {
        if (userId == null) {
            throw new MyException("userId不能为空");
        }
        UserRoleType userRoleType = UserRoleType.getUserRoleType(userRole);
        if (userRoleType == UserRoleType.None) {
            throw new MyException("userRole非法");
        }
        return update(new UpdateWrapper<SysUser>().eq("user_id", userId).set("user_role", userRoleType.getCode()));
    }

    @Override
    public boolean updateUserPermission(Long userId, List<String> userPermission) {
        if (userId == null) {
            throw new MyException("userId不能为空");
        }
        if (userPermission == null || userPermission.isEmpty()) {
            throw new MyException("userPermission不能为空");
        }
        SysUser user = getOne(new QueryWrapper<SysUser>().eq("user_id", userId).eq("is_deleted", 0));
        if (user == null) {
            throw new MyException("用户不存在");
        }
        try {
            Set<String> permission = objectMapper.readValue(user.getUserPermission(), HashSet.class);
            permission.addAll(userPermission);
            return update(new UpdateWrapper<SysUser>().eq("user_id", userId).set("user_permission", objectMapper.writeValueAsString(permission)));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new MyException("更新权限符时json转换异常");
        }
    }

    @Override
    public boolean updateUserAvatar(Long userId, MultipartFile avatar) {
        if (userId == null) {
            throw new MyException("userId不能为空");
        }
        if (avatar == null || avatar.isEmpty()) {
            throw new MyException("头像为空");
        }
        double size = avatar.getSize() / 8.0 / 1024 / 1024;
        if (size > 5) {
            throw new MyException("头像大小不能超过5M");
        }
        try {
            int idx = avatar.getOriginalFilename().lastIndexOf(".");
            String relativeName = "upload/avatar" + userCenterConfig.getSnowflake().nextId() + avatar.getOriginalFilename().substring(idx);
            File saveFile = new File(System.getProperty("user.dir"), relativeName);
            File parentFile = saveFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdir();
            }
            avatar.transferTo(saveFile);
            return update(new UpdateWrapper<SysUser>().eq("user_id", userId).set("avatar", relativeName));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Message ChangeEmailBinding(@Valid BindEmailChangeDto bindEmailChangeDto) {
        if (!RegUtil.checkEmail(bindEmailChangeDto.getNewEmail())) {
            return Message.error("新邮箱格式不正确");
        }
        // 校验用户是否存在
        SysUser user = getOne(new QueryWrapper<SysUser>().eq("user_id", bindEmailChangeDto.getUserId()).eq("is_deleted", 0));
        if (user == null) {
            return Message.error("用户不存在");
        }
        if (user.getAccountStatus() == 1) {
            return Message.error("用户已被禁用");
        }
        // 校验新邮箱是否被绑定
        if (isEmailRegistered(bindEmailChangeDto.getNewEmail())) {
            return Message.error("新邮箱已被绑定");
        }
        // 校验验证码，原邮箱的验证码
        if (!verifyCodeService.verifyCode(user.getEmail(), bindEmailChangeDto.getCode(), VerifyCodeType.CHANGE_EMAIL.getCode())) {
            return Message.error("验证码错误");
        }
        // 修改
        if (update(new UpdateWrapper<SysUser>().eq("user_id", bindEmailChangeDto.getUserId()).set("email", bindEmailChangeDto.getNewEmail()))) {
            return Message.ok("修改成功");
        } else {
            return Message.error("修改失败");
        }
    }

    @Override
    public Page<SysUser> list(QuerySysUserDto querySysUserDto) {
        if (querySysUserDto == null) {
            throw new MyException("查询参数不能为空");
        }
        Long pageNum = querySysUserDto.getPageNum();
        Long pageSize = querySysUserDto.getPageSize();
        if (pageNum == null || pageNum < 1) {
            pageNum = 1L;
        }
        if (pageSize == null || pageSize <= 0 || pageSize > 50) {
            pageSize = 10L;
        }
        Page<SysUser> sysUserPage = new Page<SysUser>().setCurrent(pageNum).setSize(pageSize);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<>();
        if (querySysUserDto.getUserId() != null) {
            wrapper.eq("user_id", querySysUserDto.getUserId());
        }
        if (StringUtils.isNotBlank(querySysUserDto.getUsername())) {
            wrapper.like("username", querySysUserDto.getUsername());
        }
        if (StringUtils.isNotBlank(querySysUserDto.getNickname())) {
            wrapper.like("nickname", querySysUserDto.getNickname());
        }
        if (querySysUserDto.getGender() != null) {
            wrapper.eq("gender", querySysUserDto.getGender());
        }
        if (querySysUserDto.getAge() != null) {
            wrapper.eq("age", querySysUserDto.getAge());
        }
        if (StringUtils.isNotBlank(querySysUserDto.getPhone())) {
            wrapper.like("phone", querySysUserDto.getPhone());
        }
        if (StringUtils.isNotBlank(querySysUserDto.getEmail())) {
            wrapper.like("email", querySysUserDto.getEmail());
        }
        if (querySysUserDto.getAccountStatus() != null) {
            wrapper.eq("account_status", querySysUserDto.getAccountStatus());
        }
        if (querySysUserDto.getIsDeleted() != null) {
            wrapper.eq("is_deleted", querySysUserDto.getIsDeleted());
        }
        if (querySysUserDto.getStartTime() != null) {
            wrapper.ge("create_time", querySysUserDto.getStartTime());
        }
        if (querySysUserDto.getEndTime() != null) {
            wrapper.lt("create_time", querySysUserDto.getEndTime());
        }
        if (StringUtils.isNotBlank(querySysUserDto.getUserPermission())) {
            wrapper.like("user_permission", querySysUserDto.getUserPermission());
        }
        return page(sysUserPage, wrapper);
    }
}




