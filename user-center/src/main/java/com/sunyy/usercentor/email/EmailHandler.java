package com.sunyy.usercentor.email;

import com.sunyy.usercentor.email.entity.ToEmail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ovi
 * @since 2024/8/11
 */
@Slf4j
@Component
public class EmailHandler {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 简单的文本发送
     * @param toEmail 发送实体
     * @return true 发送成功, false 发送失败
     */
    public boolean send(ToEmail toEmail) {
        //创建简单邮件消息
        SimpleMailMessage message = new SimpleMailMessage();
        //谁发的
        message.setFrom(from);
        //谁要接收
        message.setTo(toEmail.getTos());
        //邮件标题
        message.setSubject(toEmail.getSubject());
        //邮件内容
        message.setText(toEmail.getContent());
        try {
            mailSender.send(message);
            return true;
        } catch (MailException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}
