package com.example.demo.account.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class AccountAop {

    private static final Logger accountLogger = LoggerFactory.getLogger(AccountAop.class);

    // 로그인 요청 로깅
    @Before("execution(* com.example.demo.account.service.AuthenticationService.login(..))")
    public void logLoginRequest(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];  // id 파라미터
        String pw = (String) args[1];  // pw 파라미터
        accountLogger.info("login attempt - ID: {}, PW: {}", id, pw);  // 평문으로 로그를 남깁니다.
    }

    // 로그아웃 요청 로깅
    @Before("execution(* com.example.demo.account.service.AuthenticationService.logout())")
    public void logLogoutRequest(JoinPoint joinPoint) {
        accountLogger.info("logout");
    }

}