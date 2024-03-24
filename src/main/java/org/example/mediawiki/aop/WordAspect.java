package org.example.mediawiki.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class WordAspect {

    @Around("PointCuts.deleteMethodsWord()")
    public Object aroundDeleteAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        Long ids = null;
        if (methodSignature.getName().equals("delete")) {
            Object[] arguments = joinPoint.getArgs();
            for (Object arg : arguments) {
                if (arg instanceof Long id) {
                    ids = id;
                    log.info("Try delete word with id {}", id);
                }
            }
        }
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Word with id {} delete", ids);
        return result;
    }

    @Around("PointCuts.createMethodsWord()")
    public Object aroundCreateAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        Object[] arguments = joinPoint.getArgs();
        Word words = null;
        if (methodSignature.getName().equals("create")) {
            for (Object arg : arguments) {
                if (arg instanceof Word word) {
                    words = word;
                    log.info("Try add word with description {}",
                            words.getDescription());
                }
            }
        }
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Word with description {} add", words.getDescription());
        return result;
    }

    @Around("PointCuts.updateMethodsWord()")
    public Object aroundUpdateAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        if (methodSignature.getName().equals("update")) {
                    log.info("Try change word");
        }
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Method change word");
        return result;
    }

    @Around("PointCuts.readMethodsWord()")
    public Object aroundReadAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        if (methodSignature.getName().equals("read")) {
            log.info("Try read all words");
        }
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Method read all words");
        return result;
    }

    public void checkStartMethod(final String method, final Object[] arguments) {
        switch (method) {
            case "getWordByTitle" -> checkWordByTitle(arguments);
            case "getWordById" -> checkWordById(arguments);
            case "getExistingById" -> checkExistingById(arguments);
            case "getWordBySearch" -> checkWordBySearch(arguments);
            case "getWordsFromPages" -> checkWordsFromPages(arguments);
            default -> {}
        }
    }

    private void checkWordByTitle(final Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof String title) {
                log.info("Try find word by title {}", title);
            }
        }
    }

    private void checkWordById(final Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof Long id) {
                log.info("Try find word by id {}", id);
            }
        }
    }

    private void checkExistingById(final Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof Long id) {
                log.info("Try find existing word by id {}", id);
            }
        }
    }

    private void checkWordBySearch(final Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof Search search) {
                log.info("Try find word by search {}", search.getTitle());
            }
        }
    }

    private void checkWordsFromPages(final Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof Search search) {
                log.info("Try get pages from search {}", search.getTitle());
            }
        }
    }

    public void checkEndMethod(final String method, final Object[] arguments) {
        switch (method) {
            case "getWordByTitle" -> checkEndWordByTitle(arguments);
            case "getWordById" -> checkEndWordById(arguments);
            case "getExistingById" -> checkEndExistingById(arguments);
            case "getWordsFromPages" -> log.info("Method get pages from search");
            case "getWordBySearch" -> log.info("All words by search are get");
            default -> {}
        }
    }

    private void checkEndWordByTitle(final Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof Word word) {
                log.info("Method find word by title {}", word.getTitle());
            }
        }
    }

    private void checkEndWordById(final Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof Word word) {
                log.info("Method find word by id {}", word.getId());
            }
        }
    }

    private void checkEndExistingById(final Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof Boolean flag) {
                log.info("Method find existing word by id: {}", flag);
            }
        }
    }


    @Around("PointCuts.getMethodsWord()")
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
        checkEndMethod(methodSignature.getName(), arguments);
        return result;
    }
}
