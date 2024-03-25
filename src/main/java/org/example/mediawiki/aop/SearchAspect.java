package org.example.mediawiki.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class SearchAspect {

    private final ArgumentLogger argumentLogger;

    @Autowired
    public SearchAspect(final ArgumentLogger argumentLogger) {
        this.argumentLogger = argumentLogger;
    }


    @Around("PointCuts.deleteMethodsSearch()")
    public Object aroundDeleteAdvice(ProceedingJoinPoint joinPoint) {
        return argumentLogger.processMethod(joinPoint,
                arguments -> argumentLogger.logLongArguments(arguments, "Try delete search with id {}"),
                arguments -> argumentLogger.logLongArguments(arguments, "Search with id {} delete"));
    }

    @Around("PointCuts.createMethodsSearch()")
    public Object aroundCreateAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        if (methodSignature.getName().equals("create")) {
                return argumentLogger.processMethod(joinPoint,
                        arguments -> argumentLogger.logArguments(arguments, "Try add search", arg -> arg instanceof Pages, arg -> ((Search) arg).getTitle()),
                        arguments -> argumentLogger.logArguments(arguments, "Search {} add", arg -> arg instanceof Pages, arg -> ((Search) arg).getTitle()));

        }
        if (methodSignature.getName().equals("createSearchAndPages")) {
            return argumentLogger.processMethod(joinPoint,
                    arguments -> argumentLogger.logString( "Try create pages and search "),
                    arguments -> argumentLogger.logString("Method create pages and search"));

        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Around("PointCuts.updateMethodsSearch()")
    public Object aroundUpdateAdvice(ProceedingJoinPoint joinPoint) {
        return argumentLogger.processMethod(joinPoint,
                arguments -> argumentLogger.logArguments(arguments, "Try change search", arg -> true, arg -> ""),
                arguments -> log.info("Method change search"));
    }

    @Around("PointCuts.readMethodsSearch()")
    public Object aroundReadAdvice(ProceedingJoinPoint joinPoint) {
        return argumentLogger.processMethod(joinPoint,
                arguments -> log.info("Try read all searches"),
                arguments -> log.info("Method read all searches"));
    }


    @Around("PointCuts.getMethodsSearch()")
    public Object checkStartMethod(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String method = methodSignature.getName();
        switch (method) {
            case "getSearchById" -> {
                return argumentLogger.processMethod(joinPoint,
                        arguments -> argumentLogger.logLongArguments(arguments, "Try find search by id {}"),
                        arguments -> argumentLogger.logArguments(arguments, "Search with id {} found",
                                arg -> arg instanceof Search, arg -> Long.toString(((Search) arg).getId())));
            }
            case "getSearchByTitle" -> {
                return argumentLogger.processMethod(joinPoint,
                        arguments -> argumentLogger.logStringArguments(arguments, "Try find search by title {}"),
                        arguments -> argumentLogger.logArguments(arguments, "Search with title {} found",
                                arg -> arg instanceof Search, arg -> ((Search) arg).getTitle()));
            }
            case "getSearchExistingById" -> {
                return argumentLogger.processMethod(joinPoint,
                        arguments -> argumentLogger.logLongArguments(arguments, "Try find existing search by id {}"),
                        arguments ->  log.info("Method find existing search"));
            }
            default -> {
                log.warn("Unrecognized method: {}", method);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

}
