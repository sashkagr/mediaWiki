package org.example.mediawiki.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordAspectTest {

    @Test
    void aroundDeleteAdvice() throws Throwable {
        ArgumentLogger argumentLogger = Mockito.mock(ArgumentLogger.class);
        Logger logger = Mockito.mock(Logger.class);
        WordAspect aspect = new WordAspect(argumentLogger);
        aspect.setLogger(logger);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);

        aspect.aroundDeleteAdvice(joinPoint);

        Mockito.verify(argumentLogger).processMethod(
                eq(joinPoint),
                any(),
                any());
    }

    @Test
    void aroundCreateAdvice_CreateMethod() throws Throwable {
        ArgumentLogger argumentLogger = Mockito.mock(ArgumentLogger.class);
        Logger logger = Mockito.mock(Logger.class);
        WordAspect aspect = new WordAspect(argumentLogger);
        aspect.setLogger(logger);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("create");

        aspect.aroundCreateAdvice(joinPoint);

        Mockito.verify(argumentLogger).processMethod(
                eq(joinPoint),
                any(),
                any());
    }

    @Test
    void aroundCreateAdvice_OtherMethod() throws Throwable {
        ArgumentLogger argumentLogger = Mockito.mock(ArgumentLogger.class);
        Logger logger = Mockito.mock(Logger.class);
        WordAspect aspect = new WordAspect(argumentLogger);
        aspect.setLogger(logger);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("someOtherMethod");

        aspect.aroundCreateAdvice(joinPoint);

        Mockito.verify(argumentLogger).processMethod(
                eq(joinPoint),
                any(),
                any());
    }

    @Test
    void aroundUpdateAdvice() throws Throwable {
        ArgumentLogger argumentLogger = Mockito.mock(ArgumentLogger.class);
        Logger logger = Mockito.mock(Logger.class);
        WordAspect aspect = new WordAspect(argumentLogger);
        aspect.setLogger(logger);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);

        aspect.aroundUpdateAdvice(joinPoint);

        Mockito.verify(argumentLogger).processMethod(
                eq(joinPoint),
                any(),
                any());
    }

    @Test
    void aroundReadAdvice() throws Throwable {
        ArgumentLogger argumentLogger = Mockito.mock(ArgumentLogger.class);
        Logger logger = Mockito.mock(Logger.class);
        WordAspect aspect = new WordAspect(argumentLogger);
        aspect.setLogger(logger);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);

        aspect.aroundReadAdvice(joinPoint);

        Mockito.verify(argumentLogger).processMethod(
                eq(joinPoint),
                any(),
                any());
    }

    @Mock
    private ProceedingJoinPoint joinPointMock;

    @Mock
    private MethodSignature methodSignatureMock;

    @Mock
    private ArgumentLogger argumentLoggerMock;

    @InjectMocks
    private WordAspect aspect;

    @ParameterizedTest
    @MethodSource("provideTestData")
    void testAroundGetAdvice(String methodName, String expectedResult) throws Throwable {
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn(methodName);
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn(expectedResult);

        Object result = aspect.aroundGetAdvice(joinPointMock);

        assertEquals(expectedResult, result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                arguments("getWordByTitle", "Word by title found"),
                arguments("getWordById", "Word by id found"),
                arguments("getExistingById", "Existing word found"),
                arguments("getWordBySearch", "Word by search found"),
                arguments("getWordsFromPages", "Word from pages get")
        );
    }
}
