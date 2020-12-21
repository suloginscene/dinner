package me.scene.dinner.integration.utils.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;


@Aspect
@Slf4j
@Component
public class LogAspect {

    @Around("@annotation(LogAround)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String name = joinPoint.getSignature().getName();
        log.debug("\n------------------------------ {} ------------------------------", name);

        Object proceed = joinPoint.proceed();

        log.debug("\n----------------------------------------------------------------------");
        return proceed;
    }

}
