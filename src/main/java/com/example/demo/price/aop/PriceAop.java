package com.example.demo.price.aop;

import com.example.demo.account.service.AuthenticationService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.shell.command.invocation.InvocableShellMethod.log;

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = false)
public class PriceAop {

    private static final Logger log = LoggerFactory.getLogger(PriceAop.class);
    private final AuthenticationService authenticationService;


    @Pointcut("within(com.example.demo.shell.MyCommands)")
    public void command() {
    }
    @Pointcut("execution(* *city(..))|| execution(* *sector(..)) || execution(* *price(..))|| execution(* *billTotal(..))")
    public void priceMethod() {
    }

    public PriceAop(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }


    @Around("(command() && priceMethod())")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        if (this.authenticationService.getCurrentAccount() == null) {
            throw new RuntimeException("need to login first");
        } else {
            log.info("----- {} {}.{}({}) ----->", new Object[]{this.authenticationService.getCurrentAccount().getName(), joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), joinPoint.getArgs()});
            Object object = joinPoint.proceed();
            log.info("<----- {} {}.{}({}) -----", new Object[]{this.authenticationService.getCurrentAccount().getName(), joinPoint.getTarget().getClass(), joinPoint.getSignature().getName(), object.toString()});
            return object;
        }
    }


}

