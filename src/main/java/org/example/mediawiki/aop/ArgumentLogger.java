package org.example.mediawiki.aop;

import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
@Data
public class ArgumentLogger {


    private Logger log = LoggerFactory.getLogger(this.getClass());


    public void setLogger(Logger logger) {
        this.log = logger;
    }

    public void logArguments(final Object[] arguments,
                             final String message,
                             final Predicate<Object> filter,
                             final Function<Object, String> mapper) {
        for (Object arg : arguments) {
            try {
                if (filter.test(arg)) {
                    Object mappedArg = mapper.apply(arg);
                    log.info(message, mappedArg);
                }
            } catch (Exception e) {
                log.error("Error processing argument");
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


