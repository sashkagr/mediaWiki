package org.example.mediawiki.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.example.mediawiki.modal.Search;
import org.example.mediawiki.modal.Word;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

class ArgumentLoggerTest {

    private ArgumentLogger argumentLogger;
    private ProceedingJoinPoint joinPoint;

    @BeforeEach
    void setUp() {
        argumentLogger = new ArgumentLogger();
        joinPoint = mock(ProceedingJoinPoint.class);
    }

    @Test
    void testProcessMethod_Success() throws Throwable {
        Consumer<Object[]> startLogger = mock(Consumer.class);
        Consumer<Object[]> endLogger = mock(Consumer.class);
        Object[] arguments = {"arg1", "arg2"};

        when(joinPoint.getArgs()).thenReturn(arguments);
        when(joinPoint.proceed()).thenReturn("result");

        Object result = argumentLogger.processMethod(joinPoint, startLogger, endLogger);

        verify(startLogger).accept(arguments);
        verify(endLogger).accept(arguments);
        assert (result.equals("result"));
    }

    @Test
    void testProcessMethod_Exception() throws Throwable {
        Consumer<Object[]> startLogger = mock(Consumer.class);
        Consumer<Object[]> endLogger = mock(Consumer.class);
        Object[] arguments = {"arg1", "arg2"};

        when(joinPoint.getArgs()).thenReturn(arguments);
        when(joinPoint.proceed()).thenThrow(new RuntimeException());

        Object result = argumentLogger.processMethod(joinPoint, startLogger, endLogger);

        verify(startLogger).accept(arguments);
        verify(endLogger).accept(arguments);
        assert (result instanceof ResponseEntity);
        assert (((ResponseEntity<?>) result).getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void logLongArguments() {
        Logger logger = Mockito.mock(Logger.class);
        ArgumentLogger argumentLogger = new ArgumentLogger();
        argumentLogger.setLogger(logger);
        Object[] arguments = {100L};
        String message = "Logging long arguments";

        argumentLogger.logLongArguments(arguments, message);

        verify(logger).info("Logging long arguments", "100");
    }

    @Test
    void logStringArguments() {
        Logger logger = Mockito.mock(Logger.class);
        ArgumentLogger argumentLogger = new ArgumentLogger();
        argumentLogger.setLogger(logger);
        Object[] arguments = {"string1"};
        String message = "Logging string arguments";

        argumentLogger.logStringArguments(arguments, message);

        verify(logger).info("Logging string arguments", "string1");
    }

    @Test
    void logSearchArguments() {
        Logger logger = Mockito.mock(Logger.class);
        ArgumentLogger argumentLogger = new ArgumentLogger();
        argumentLogger.setLogger(logger);
        Search search1 = new Search();
        search1.setTitle("Title1");
        Search search2 = new Search();
        search2.setTitle("Title2");
        Object[] arguments = {search1};
        String message = "Logging search arguments";

        argumentLogger.logSearchArguments(arguments, message);

        verify(logger).info("Logging search arguments", "Title1");
    }

    @Test
    void logWordArguments() {
        Logger logger = Mockito.mock(Logger.class);
        ArgumentLogger argumentLogger = new ArgumentLogger();
        argumentLogger.setLogger(logger);
        Word word1 = new Word();
        word1.setTitle("Word1");
        Word word2 = new Word();
        word2.setTitle("Word2");
        Object[] arguments = {word1};
        String message = "Logging word arguments";

        argumentLogger.logWordArguments(arguments, message);

        verify(logger).info("Logging word arguments", "Word1");
    }


}
