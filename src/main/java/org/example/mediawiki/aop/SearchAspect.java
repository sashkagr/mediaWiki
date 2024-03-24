package org.example.mediawiki.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.mediawiki.modal.Search;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class SearchAspect {
    @Around("PointCuts.deleteMethodsSearch()")
    public Object aroundDeleteAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        Long ids = null;
        if (methodSignature.getName().equals("delete")) {
            Object[] arguments = joinPoint.getArgs();
            for (Object arg : arguments) {
                if (arg instanceof Long id) {
                    ids = id;
                    log.info("Try delete search with id {}", id);
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
        log.info("Search with id {} delete", ids);
        return result;
    }

    @Around("PointCuts.createMethodsSearch()")
    public Object aroundCreateAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        Object[] arguments = joinPoint.getArgs();
        Search search1 = null;
        if (methodSignature.getName().equals("create")) {
            for (Object arg : arguments) {
                if (arg instanceof Search search) {
                    search1 = search;
                    log.info("Try add page",
                            search.getTitle());
                }
            }
        }
        if (methodSignature.getName().equals("createSearchAndPages")) {
            for (Object arg : arguments) {
                if (arg instanceof String name) {
                    log.info("Try create pages and search by search name {}",
                            name);
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
        if (methodSignature.getName().equals("create")) {
            log.info("Search {} add", search1.getTitle());
        }
        if (methodSignature.getName().equals("createSearchAndPages")) {
            log.info("Method create pages and search ");
        }
        return result;
    }

    @Around("PointCuts.updateMethodsPages()")
    public Object aroundUpdateAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        if (methodSignature.getName().equals("update")) {
            log.info("Try change search");
        }
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Method change search");
        return result;
    }

    @Around("PointCuts.readMethodsPages()")
    public Object aroundReadAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        if (methodSignature.getName().equals("read")) {
            log.info("Try read all searches");
        }
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Method read all searches");
        return result;
    }

    public void checkStartMethod(final String method,
                                 final Object[] arguments) {
        switch (method) {
            case "getSearchById" -> checkSearchById(arguments);
            case "getSearchExistingById" -> checkSearchExistingById(arguments);
            case "getSearchByTitle" -> checkSearchByTitle(arguments);
            default -> {
                break;
            }
        }
    }

    private void checkSearchById(final Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof Long id) {
                log.info("Try find search by id {}", id);
            }
        }
    }

    private void checkSearchExistingById(final Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof Long id) {
                log.info("Try find existing search by id {}", id);
            }
        }
    }

    private void checkSearchByTitle(final Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof String title) {
                log.info("Try find search by title {}", title);
            }
        }
    }

    public void checkEndMethod(final String method, final Object[] arguments) {
        switch (method) {
            case "getSearchById" -> checkEndSearchById(arguments);
            case "getSearchExistingById" ->
                    log.info("Method find existing search");
            case "getSearchByTitle" -> checkEndSearchByTitle(arguments);
            default -> {
                break;
            }
        }
    }

    private void checkEndSearchById(final Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof Search search) {
                log.info("Method find search by id {}", search.getId());
            }
        }
    }

    private void checkEndSearchByTitle(final Object[] arguments) {
        for (Object arg : arguments) {
            if (arg instanceof Search search) {
                log.info("Method find search by title {}", search.getTitle());
            }
        }
    }


    @Around("PointCuts.getMethodsPages()")
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
