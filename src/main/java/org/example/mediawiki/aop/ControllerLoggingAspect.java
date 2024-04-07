package org.example.mediawiki.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {
    @Before("execution(* org.example.mediawiki.controller."
            + "RequestManager.*(..))")
    public void logBefore(final JoinPoint joinPoint) {
        log.info("Method {} called with arguments {}", joinPoint.
                getSignature().toShortString(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* org.example.mediawiki."
            + "controller.RequestManager.*(..))", returning = "result")
    public void logAfterReturning(final JoinPoint joinPoint,
                                  final Object result) {
        log.info("Method {} returned {}",
                joinPoint.getSignature().toShortString(), result);
    }
}