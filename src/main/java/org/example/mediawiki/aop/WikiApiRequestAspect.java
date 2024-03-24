package org.example.mediawiki.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class WikiApiRequestAspect {
    @Around("PointCuts.mapMethodsWikiApi()")
    public Object aroundMapAdvice(final ProceedingJoinPoint joinPoint) {
        Object result = null;
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        if (methodSignature.getName().equals("mapResponseToModel")) {
            log.info("Try map descriptionGetApiSearchResponse to word");
        }
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Method map to word");
        return result;
    }

    public void checkStartMethod(final String method,
                                 final Object[] arguments) {
        switch (method) {
            case "getDescriptionByTitle" -> {
                for (Object arg : arguments) {
                    if (arg instanceof String title) {
                        log.info("Try get pages by title {}", title);
                    }
                }
            }
            case "getResponse" -> log.info("Try get response");
            case "getApiSearchResponsesWords" -> log.
                    info("Try get API search responses");
            case "getDescriptionByPageId" -> {
                for (Object arg : arguments) {
                    if (arg instanceof Long pageId) {
                        log.info("Try get description by pageId {}", pageId);
                    }
                }
            }
            default -> {
                break;
            }
        }
    }

    public void checkEndMethod(final String method) {
        switch (method) {
            case "getDescriptionByTitle" -> log.info("Method get all pages");
            case "getResponse" -> log.info("Method get response");
            case "getApiSearchResponsesWords" ->
                    log.info("Method get all API search responses");
            case "getDescriptionByPageId" ->
                    log.info("Method get description by pageId");
            default -> {
                break;
            }
        }
    }

    @Around("PointCuts.getMethodsWikiApi()")
    public Object aroundGetAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        Object[] arguments = joinPoint.getArgs();
        checkStartMethod(methodSignature.getName(), arguments);
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        checkEndMethod(methodSignature.getName());
        return result;
    }
}
