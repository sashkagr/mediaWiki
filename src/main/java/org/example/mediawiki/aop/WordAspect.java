package org.example.mediawiki.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class WordAspect {

    private final ArgumentLogger argumentLogger;

    @Autowired
    public WordAspect(final ArgumentLogger argumentLogger) {
        this.argumentLogger = argumentLogger;
    }

        @Around("PointCuts.deleteMethodsWord()")
        public Object aroundDeleteAdvice(final ProceedingJoinPoint joinPoint) {
            return argumentLogger.processMethod(joinPoint, "delete",
                    arguments -> argumentLogger.logLongArguments(arguments, "Try delete word with id {}"),
                    arguments -> argumentLogger.logLongArguments(arguments, "Word with id {} delete"));
        }

        @Around("PointCuts.createMethodsWord()")
        public Object aroundCreateAdvice(final ProceedingJoinPoint joinPoint) {
            return argumentLogger.processMethod(joinPoint, "create",
                    arguments -> argumentLogger.logArguments(arguments, "Try add word",
                            arg -> arg instanceof Word, arg -> ((Word) arg).getTitle()),
                    arguments -> argumentLogger.logArguments(arguments, "Word {} add",
                            arg -> arg instanceof Word, arg -> ((Word) arg).getTitle()));
        }

        @Around("PointCuts.updateMethodsWord()")
        public Object aroundUpdateAdvice(final ProceedingJoinPoint joinPoint) {
            return argumentLogger.processMethod(joinPoint, "update",
                    arguments -> argumentLogger.logArguments(arguments, "Try change word",
                            arg -> true, arg -> ""),
                    arguments -> log.info("Method change word"));
        }

        @Around("PointCuts.readMethodsWord()")
        public Object aroundReadAdvice(final ProceedingJoinPoint joinPoint) {
            return argumentLogger.processMethod(joinPoint, "read",
                    arguments -> log.info("Try read all words"),
                    arguments -> log.info("Method read all words"));
        }
        @Around("PointCuts.getMethodsWord()")
        public Object aroundGetAdvice(final ProceedingJoinPoint joinPoint) {
            MethodSignature methodSignature =
                    (MethodSignature) joinPoint.getSignature();
            String method = methodSignature.getName();
            switch (method) {
                case "getWordByTitle" -> {
                    return argumentLogger.processMethod(joinPoint, "getWordByTitle",
                            arguments -> argumentLogger.logStringArguments(arguments, "Try find word by title {}"),
                            arguments -> argumentLogger.logWordArguments(arguments, "Word by title {} found"));
                }
                case "getWordById" -> {
                    return argumentLogger.processMethod(joinPoint, "getWordById",
                            arguments -> argumentLogger.logLongArguments(arguments, "Try find word by id {}"),
                            arguments -> argumentLogger.logWordArguments(arguments, "Word by id {} found"));
                }
                case "getExistingById" -> {
                    return argumentLogger.processMethod(joinPoint, "getExistingById",
                            arguments -> argumentLogger.logLongArguments(arguments, "Try find existing word by id {}"),
                            arguments -> argumentLogger.logString("Existing word found"));
                }
                case "getWordBySearch" -> {
                    return argumentLogger.processMethod(joinPoint, "getWordBySearch",
                            arguments -> argumentLogger.logSearchArguments(arguments, "Try find word by search {}"),
                            arguments -> argumentLogger.logString("Word by search found"));
                }
                case "getWordsFromPages" -> {
                    return argumentLogger.processMethod(joinPoint, "getWordsFromPages",
                            arguments -> argumentLogger.logSearchArguments(arguments, "Try get pages from search {}"),
                            arguments -> argumentLogger.logString("Word from pages get"));
                }
                default -> {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
    }


