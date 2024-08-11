package com.sunyy.usercentor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.config.UserCenterConfig;
import com.sunyy.usercentor.email.EmailHandler;
import com.sunyy.usercentor.email.entity.ToEmail;
import com.sunyy.usercentor.mapper.VerifyCodeMapper;
import com.sunyy.usercentor.pojo.entity.VerifyCode;
import com.sunyy.usercentor.pojo.enume.VerifyCodeType;
import com.sunyy.usercentor.service.SysUserService;
import com.sunyy.usercentor.service.VerifyCodeService;
import com.sunyy.usercentor.utils.RandomStringGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 60382
 * @description 针对表【verify_code】的数据库操作Service实现
 * @createDate 2024-08-11 18:39:12
 */
@Slf4j
@Service
public class VerifyCodeServiceImpl extends ServiceImpl<VerifyCodeMapper, VerifyCode>
        implements VerifyCodeService {

    /**
     * 邮箱正则校验
     */
    private static final Pattern EMAIL_REGEX = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    @Resource
    private EmailHandler emailHandler;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private UserCenterConfig userCenterConfig;

    @Override
    public Message sendCodeToEmail(String email, VerifyCodeType type) {
        Matcher matcher = EMAIL_REGEX.matcher(email);
        if (!matcher.find()) {
            return Message.error("邮箱格式不正确");
        }
        boolean emailRegistered = sysUserService.isEmailRegistered(email);
        if (emailRegistered) {
            return Message.error("邮箱已被注册");
        }
        String code = RandomStringGenerator.generateRandomString(userCenterConfig.getVerifyCodeLength());
        log.debug("email: {}, code: {}", email, code);
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setCodeType(type.getCode());
        verifyCode.setEmail(email);
        verifyCode.setCode(code);
        LocalDateTime now = LocalDateTime.now();
        verifyCode.setExpirationDate(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
                now.getHour(), now.getMinute() + userCenterConfig.getVerifyCodeExpire(), now.getSecond()));
        save(verifyCode);
        ToEmail toEmail = new ToEmail();
        toEmail.setTos(new String[]{email});
        toEmail.setSubject(type.getSubject());
        toEmail.setContent("你的验证码是：" + code + "，该验证码有效期为5分钟，请在有效期内完成注册！");
        boolean send = emailHandler.send(toEmail);
        if (send) {
            return Message.ok(null);
        }
        return Message.error("验证码发送失败");
    }

    @Override
    public boolean verifyCode(String email, String code, Integer type) {
        QueryWrapper<VerifyCode> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        wrapper.eq("code_type", type);
        wrapper.ge("expiration_date", LocalDateTime.now());
        long count = list(wrapper).stream().filter(verifyCode -> verifyCode.getCode().equals(code)).count();
        return count != 0;
    }

}




