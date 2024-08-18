package com.sunyy.usercentor.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author ovi
 * @since 2024/8/10
 */
@Slf4j
@Aspect
@Component
public class AspectConfig {

    @Pointcut("@annotation(com.sunyy.usercentor.common.anno.AuthNeed)")
    public void authCheckCut() {
    }

//    @Pointcut("@annotation(com.sunyy.usercentor.common.anno.RequestParamsLog)")
//    public void logPointCut() {}

    @Before("execution(* com.sunyy.usercentor.controller.*.* (..))")
    public void outputLog(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = signature.getParameterNames();
        log.info("================method {} params================", method.getName());
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
                continue;
            }
            log.info("{} ==> {}", parameterNames[i], arg);
        }
        log.info("================//params print end//================");
    }

    @Before("authCheckCut()")
    public void authCheck(JoinPoint joinPoint) {

    }

}
