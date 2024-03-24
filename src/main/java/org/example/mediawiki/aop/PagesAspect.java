package org.example.mediawiki.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.mediawiki.modal.Pages;
import org.example.mediawiki.modal.Search;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class PagesAspect {
    @Around("PointCuts.deleteMethodsPages()")
    public Object aroundDeleteAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        Long ids = null;
        if (methodSignature.getName().equals("delete")) {
            Object[] arguments = joinPoint.getArgs();
            for (Object arg : arguments) {
                if (arg instanceof Long id) {
                    ids = id;
                    log.info("Try delete page with id {}", id);
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
        log.info("Page with id {} delete", ids);
        return result;
    }

    @Around("PointCuts.createMethodsPages()")
    public Object aroundCreateAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        Object[] arguments = joinPoint.getArgs();
        Pages pages = null;
        if (methodSignature.getName().equals("create")) {
            for (Object arg : arguments) {
                if (arg instanceof Pages page) {
                    pages = page;
                    log.info("Try add page",
                            pages.getTitle());
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
        log.info("Page {} add", pages.getTitle());
        return result;
    }

    @Around("PointCuts.updateMethodsPages()")
    public Object aroundUpdateAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        if (methodSignature.getName().equals("update")) {
            log.info("Try change page");
        }
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Method change page");
        return result;
    }

    @Around("PointCuts.readMethodsPages()")
    public Object aroundReadAdvice(final ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature =
                (MethodSignature) joinPoint.getSignature();
        if (methodSignature.getName().equals("read")) {
            log.info("Try read all pages");
        }
        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Method read all pages");
        return result;
    }

    public void checkStartMethod(final String method,
                                 final Object[] arguments) {
        switch (method) {
            case "getPageByPageId" -> {
                for (Object arg : arguments) {
                    if (arg instanceof Long id) {
                        log.info("Try find page by id {}", id);
                    }
                }
            }
            case "getPagesBySearch" -> {
                for (Object arg : arguments) {
                    if (arg instanceof Search search) {
                        log.info("Try find page by search {}",
                                search.getTitle());
                    }
                }
            }
            default -> {
                break;
            }
        }
    }

    public void checkEndMethod(final String method,
                               final Object[] arguments) {
        switch (method) {
            case "getPageByPageId" -> {
                for (Object arg : arguments) {
                    if (arg instanceof Pages page) {
                        log.info("Method find page by pageId {} ",
                                page.getPageId());
                    }
                }
            }
            case "getPagesBySearch" -> log.info("All pages by search are get");
            default -> {
                break;
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
