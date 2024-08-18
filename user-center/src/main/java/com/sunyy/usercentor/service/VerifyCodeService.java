package com.sunyy.usercentor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sunyy.usercentor.common.Message;
import com.sunyy.usercentor.pojo.entity.VerifyCode;
import com.sunyy.usercentor.pojo.enume.VerifyCodeType;

/**
 * @author 60382
 * @description 针对表【verify_code】的数据库操作Service
 * @createDate 2024-08-11 18:39:12
 */
public interface VerifyCodeService extends IService<VerifyCode> {

    /**
     * 发送验证码
     *
     * @param email 邮箱
     * @param type  验证码类型
     */
    Message sendCodeToEmail(String email, VerifyCodeType type);

    /**
     * 校验验证码
     *
     * @return true:验证成功 false:验证失败
     */
    boolean verifyCode(String email, String code, Integer type);
}
