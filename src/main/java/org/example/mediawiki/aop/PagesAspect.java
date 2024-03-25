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
    public PagesAspect(final ArgumentLogger argumentLogger) {
        this.argumentLogger = argumentLogger;
    }

    @Around("PointCuts.deleteMethodsPages()")
    public Object aroundDeleteAdvice(ProceedingJoinPoint joinPoint) {
        return argumentLogger.processMethod(joinPoint, "delete",
                arguments -> argumentLogger.logLongArguments(arguments, "Try delete page with id {}"),
                arguments -> argumentLogger.logLongArguments(arguments, "Page with id {} delete"));
    }

    @Around("PointCuts.createMethodsPages()")
    public Object aroundCreateAdvice(ProceedingJoinPoint joinPoint) {
        return argumentLogger.processMethod(joinPoint, "create",
                arguments -> argumentLogger.logArguments(arguments, "Try add page", arg -> arg instanceof Pages, arg -> ((Pages) arg).getTitle()),
                arguments -> argumentLogger.logArguments(arguments, "Page {} add", arg -> arg instanceof Pages, arg -> ((Pages) arg).getTitle()));
    }

    @Around("PointCuts.updateMethodsPages()")
    public Object aroundUpdateAdvice(ProceedingJoinPoint joinPoint) {
        return argumentLogger.processMethod(joinPoint, "update",
                arguments -> argumentLogger.logArguments(arguments, "Try change page", arg -> true, arg -> ""),
                arguments -> log.info("Method change page"));
    }

    @Around("PointCuts.readMethodsPages()")
    public Object aroundReadAdvice(ProceedingJoinPoint joinPoint) {
        return argumentLogger.processMethod(joinPoint, "read",
                arguments -> log.info("Try read all pages"),
                arguments -> log.info("Method read all pages"));
    }


    @Around("PointCuts.getMethodsPages()")
    public Object checkStartMethod(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        String method = methodSignature.getName();
        switch (method) {
            case "getPageByPageId" -> {
                return argumentLogger.processMethod(joinPoint, "delete",
                        arguments -> argumentLogger.logLongArguments(arguments, "Try find page with pageId {}"),
                        arguments -> argumentLogger.logArguments(arguments, "Page with id {} delete", arg -> arg instanceof Pages, arg -> Long.toString(((Pages) arg).getPageId())));
            }
            case "getPagesBySearch" -> {
                return argumentLogger.processMethod(joinPoint, "delete",
                        arguments -> argumentLogger.logSearchArguments(arguments, "Try find page by search {}"),
                        arguments -> log.info("All pages by search are get"));
            }
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
