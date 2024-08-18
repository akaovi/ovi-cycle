package com.sunyy.usercentor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
import com.sunyy.usercentor.utils.RegUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 60382
 * @description 针对表【verify_code】的数据库操作Service实现
 * @createDate 2024-08-11 18:39:12
 */
@Slf4j
@Service
public class VerifyCodeServiceImpl extends ServiceImpl<VerifyCodeMapper, VerifyCode>
        implements VerifyCodeService {

    @Resource
    private EmailHandler emailHandler;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private UserCenterConfig userCenterConfig;

    @Override
    public Message sendCodeToEmail(String email, VerifyCodeType type) {
        if (!RegUtil.checkEmail(email)) {
            return Message.error("邮箱格式非法");
        }
        if (type == VerifyCodeType.REGISTER) {
            boolean emailRegistered = sysUserService.isEmailRegistered(email);
            if (emailRegistered) {
                return Message.error("邮箱已被注册");
            }
        }
        String code = RandomStringGenerator.generateRandomString(userCenterConfig.getVerifyCodeLength());
        log.debug("email: {}, code: {}", email, code);
        VerifyCode verifyCode = new VerifyCode();
        verifyCode.setCodeType(type.getCode());
        verifyCode.setEmail(email);
        verifyCode.setCode(code);
        verifyCode.setExpirationDate(LocalDateTime.now().plusMinutes(userCenterConfig.getVerifyCodeExpire()));
        verifyCode.setIsDeleted(0);
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
        LocalDateTime now = LocalDateTime.now();
        QueryWrapper<VerifyCode> wrapper = new QueryWrapper<>();
        wrapper.eq("email", email);
        wrapper.eq("code_type", type);
        wrapper.ge("expiration_date", now);
        wrapper.eq("is_deleted", 0);
        List<VerifyCode> collect = list(wrapper).stream().filter(verifyCode -> verifyCode.getCode().equals(code)).collect(Collectors.toList());
        if (collect.isEmpty()) {
            return false;
        }
        UpdateWrapper<VerifyCode> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("email", email);
        updateWrapper.eq("code_type", type);
        updateWrapper.ge("expiration_date", now);
        updateWrapper.eq("is_deleted", 0);
        updateWrapper.set("is_deleted", 1);
        update(updateWrapper);
        return true;
    }

}




