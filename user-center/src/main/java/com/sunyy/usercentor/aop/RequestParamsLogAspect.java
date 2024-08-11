package com.sunyy.usercentor.aop;

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
import java.util.Arrays;

/**
 * @author ovi
 * @since 2024/8/10
 */
@Slf4j
@Aspect
@Component
public class RequestParamsLogAspect {

    @Pointcut("@annotation(com.sunyy.usercentor.common.anno.RequestParamsLog)")
    public void logPointCut() {
    }

    @Before("logPointCut()")
    public void OutputLog(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        log.info("================method {} params================", method.getName());
        Arrays.stream(args)
                .filter(arg -> !(arg instanceof HttpServletRequest || arg instanceof HttpServletResponse))
                .forEach(arg -> log.info("arg: {}", arg));
        log.info("================method {} params================", method.getName());
    }

}
