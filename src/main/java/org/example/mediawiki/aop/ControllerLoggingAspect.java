package org.example.mediawiki.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {

    private  Logger logger = LoggerFactory.getLogger(this.getClass());


    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    @Before("execution(* org.example.mediawiki.controller."
            + "RequestManager.*(..))")
    public void logBefore(final JoinPoint joinPoint) {
        try {
            if (joinPoint != null) {
                logger.info("Method {} called with arguments {}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
            } else {
                throw new IllegalArgumentException("JoinPoint is null");
            }
        } catch (Exception e) {
            // Handle the exception or log it
            logger.error("An error occurred in logBefore advice: {}", e.getMessage());
        }
    }

    @AfterReturning(pointcut = "execution(* org.example.mediawiki."
            + "controller.RequestManager.*(..))", returning = "result")
    public void logAfterReturning(final JoinPoint joinPoint,
                                  final Object result) {
        try {
            if (joinPoint != null && result != null) {
                logger.info("Method {} returned {}",
                        joinPoint.getSignature().toShortString(), result);
            }
            else {
                throw new IllegalArgumentException("JoinPoint is null");
            }
        }catch (Exception e) {
            // Handle the exception or log it
            logger.error("An error occurred in logAfter advice: {}", e.getMessage());
        }

    }
}
