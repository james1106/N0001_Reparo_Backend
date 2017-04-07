package com.hyperchain.common.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by ldy on 2017/4/5.
 */
@Aspect
@Order(1)
@Component
public class TokenAuthAspect {

    @Pointcut("@annotation(com.hyperchain.common.annotation.TokenAuth)")
    public void tokenAspect() {
    }

    /**
     * 拦截token
     *
     * @param joinPoint 切入点
     * @return
     */
    @Around("tokenAspect()")
    public Object doBefore(ProceedingJoinPoint joinPoint) {
        return null;
    }
}
