package org.example.mediawiki.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.mediawiki.modal.Pages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class PagesAspect {

    private final ArgumentLogger argumentLogger;

    @Autowired
    public PagesAspect(final ArgumentLogger argument) {
        this.argumentLogger = argument;
    }

    @Around("PointCuts.deleteMethodsPages()")
    public Object aroundDeleteAdvice(final ProceedingJoinPoint joinPoint) {
        return argumentLogger.processMethod(joinPoint,
                arguments -> argumentLogger.logLongArguments(arguments,
                        "Try delete page with id {}"),
                arguments -> argumentLogger.logLongArguments(arguments,
                        "Page with id {} delete"));
    }

    @Around("PointCuts.createMethodsPages()")
    public Object aroundCreateAdvice(final ProceedingJoinPoint joinPoint) {
        return argumentLogger.processMethod(joinPoint,
                arguments -> argumentLogger.logArguments(arguments,
                        "Try add page", Pages.class::isInstance,
                        arg -> ((Pages) arg).getTitle()),
                arguments -> argumentLogger.logArguments(arguments,
                        "Page {} add", Pages.class::isInstance,
                        arg -> ((Pages) arg).getTitle()));
    }

    @Around("PointCuts.readMethodsPages()")
    public Object aroundReadAdvice(final ProceedingJoinPoint joinPoint) {
        return argumentLogger.processMethod(joinPoint,
                arguments -> argumentLogger.logString("Try read all pages"),
                arguments -> argumentLogger.logString("Method read all pages"));
    }

    @Around("PointCuts.updateMethodsPages()")
    public Object aroundUpdateAdvice(final ProceedingJoinPoint joinPoint) {
        return argumentLogger.processMethod(joinPoint,
                arguments -> argumentLogger.logArguments(arguments,
                        "Try change page", arg -> true, arg -> ""),
                arguments -> argumentLogger.logString("Method change page"));
    }

    @Around("PointCuts.getMethodsPages()")
    public Object checkStartMethod(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        String method = methodSignature.getName();
        if (method.equals("getPageByPageId")) {
            return argumentLogger.processMethod(joinPoint,
                    arguments -> argumentLogger.logLongArguments(arguments,
                            "Try find page with pageId {}"),
                    arguments -> argumentLogger.logArguments(arguments,
                            "Page by id {} delete", Pages.class::isInstance,
                            arg -> Long.toString(((Pages) arg).getPageId())));
        }
        if (method.equals("getPagesBySearch")) {
            return argumentLogger.processMethod(joinPoint,
                    arguments -> argumentLogger.
                            logSearchArguments(arguments,
                                    "Try find page by search {}"),
                    arguments -> log.info("All pages by search are get"));
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
