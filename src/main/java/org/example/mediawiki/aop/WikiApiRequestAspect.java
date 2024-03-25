package org.example.mediawiki.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class WikiApiRequestAspect {

    private final ArgumentLogger argumentLogger;

    @Autowired
    public WikiApiRequestAspect(final ArgumentLogger argumentLogger) {
        this.argumentLogger = argumentLogger;
    }

    @Around("PointCuts.mapMethodsWikiApi()")
    public Object aroundMapAdvice(final ProceedingJoinPoint joinPoint) {
        return argumentLogger.processMethod(joinPoint, "mapResponseToModel",
                arguments -> log.info("Try map descriptionGetApiSearchResponse to word"),
                arguments -> log.info("Method map to word"));
    }


    @Around("PointCuts.getMethodsWikiApi()")
    public Object aroundGetAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        String method = methodSignature.getName();
        switch (method) {
            case "getDescriptionByTitle" -> {
                return argumentLogger.processMethod(joinPoint, "getDescriptionByTitle",
                        arguments -> argumentLogger.logStringArguments(arguments, "Try get pages by title {}"),
                        arguments -> argumentLogger.logString("Method get all pages"));
            }
            case "getDescriptionByPageId" -> {
                return argumentLogger.processMethod(joinPoint, "getDescriptionByPageId",
                        arguments -> argumentLogger.logLongArguments(arguments, "Try get description by pageId {}"),
                        arguments -> argumentLogger.logString("Method get description by pageId"));
            }
            case "getResponse" -> {
                return argumentLogger.processMethod(joinPoint, "getResponse",
                        arguments -> argumentLogger.logString("Try get response"),
                        arguments -> argumentLogger.logString("Method get response"));
            }
            case "getApiSearchResponsesWords" -> {
                return argumentLogger.processMethod(joinPoint, "getApiSearchResponsesWords",
                        arguments -> argumentLogger.logString("Try get API search responses"),
                        arguments -> argumentLogger.logString("Method get description by pageId"));
            }

            default -> {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

}
