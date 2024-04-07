package org.example.mediawiki.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
@Slf4j
public class ArgumentLogger {

    public void logArguments(final Object[] arguments,
                             final String message,
                             final Predicate<Object> filter,
                             final Function<Object, String> mapper) {
        for (Object arg : arguments) {
            if (filter.test(arg)) {
                log.info(message, mapper.apply(arg));
            } else {
                log.info(message, arg);
            }
        }
    }


    public void logLongArguments(final Object[] arguments,
                                 final String message) {
        logArguments(arguments, message, Long.class::isInstance,
                Object::toString);
    }

    public void logStringArguments(final Object[] arguments,
                                   final String message) {
        logArguments(arguments, message, String.class::isInstance,
                String.class::cast);
    }

    public void logSearchArguments(final Object[] arguments,
                                   final String message) {
        logArguments(arguments, message, Search.class::isInstance,
                arg -> ((Search) arg).getTitle());
    }

    public void logString(final String message) {
        log.info(message);
    }


    public void logWordArguments(final Object[] arguments,
                                 final String message) {
        logArguments(arguments, message,
                Word.class::isInstance,
                arg -> ((Word) arg).getTitle());
    }


    public Object processMethod(final ProceedingJoinPoint joinPoint,
                                final Consumer<Object[]> startLogger,
                                final Consumer<Object[]> endLogger) {
        Object[] arguments = joinPoint.getArgs();

        startLogger.accept(arguments);

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            result = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        endLogger.accept(arguments);

        return result;
    }
}


