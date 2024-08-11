package com.sunyy.usercentor;

import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.email.EmailHandler;
import com.sunyy.usercentor.email.entity.ToEmail;
import com.sunyy.usercentor.mapStruct.SysUserMapStructMapper;
import com.sunyy.usercentor.pojo.entity.SysUser;
import com.sunyy.usercentor.pojo.enume.VerifyCodeType;
import com.sunyy.usercentor.pojo.vo.SysUserVo;
import com.sunyy.usercentor.service.VerifyCodeService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class UserCenterApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(UserCenterApplicationTests.class);
    @Resource
    private SysUserMapStructMapper sysUserMapStructMapper;

    @Resource
    private EmailHandler emailHandler;

    @Resource
    private VerifyCodeService verifyCodeService;

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

    @Test
    public void testEmail() {
        ToEmail toEmail = new ToEmail();
        toEmail.setTos(new String[]{""});
        toEmail.setSubject("测试");
        toEmail.setContent("这是一个基于spring的邮件发送测试, 当前时间: "
                + DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
        boolean send = emailHandler.send(toEmail);
        log.info("邮件发送是否成功: {}", send);
    }

    @Test
    public void testVerifyCode() {
        String email = "";
        Message message = verifyCodeService.sendCodeToEmail(email, VerifyCodeType.REGISTER);
        log.info("发送邮件结果: {}", message);
    }

}
