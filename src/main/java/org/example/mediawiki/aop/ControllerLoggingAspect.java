package org.example.mediawiki.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerLoggingAspect {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    public void setLogger(final Logger logger) {
        this.log = logger;
    }

    @Before("execution(* org.example.mediawiki.controller.RequestManager.*(..))")
    public void logBefore(final JoinPoint joinPoint) {
        if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0].equals("specificValue")) {
            log.info("Method {} called with arguments {}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
        }
    }

    @AfterReturning(pointcut = "execution(* org.example.mediawiki.controller.RequestManager.*(..))", returning = "result")
    public void logAfterReturning(final JoinPoint joinPoint, final Object result) {
        if (joinPoint.getArgs().length > 0 && joinPoint.getArgs()[0].equals("specificValue")) {
            log.info("Method {} returned {}", joinPoint.getSignature().toShortString(), result);
        }
    }

}