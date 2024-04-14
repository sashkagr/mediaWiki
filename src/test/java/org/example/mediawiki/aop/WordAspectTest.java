package org.example.mediawiki.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WordAspectTest {

    @Test
    void aroundDeleteAdvice() throws Throwable {
        // Arrange
        ArgumentLogger argumentLogger = Mockito.mock(ArgumentLogger.class);
        Logger logger = Mockito.mock(Logger.class);
        WordAspect aspect = new WordAspect(argumentLogger);
        aspect.setLogger(logger);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);

        // Act
        aspect.aroundDeleteAdvice(joinPoint);

        // Assert
        Mockito.verify(argumentLogger).processMethod(
                eq(joinPoint),
                any(),
                any());
    }

    @Test
    void aroundCreateAdvice_CreateMethod() throws Throwable {
        // Arrange
        ArgumentLogger argumentLogger = Mockito.mock(ArgumentLogger.class);
        Logger logger = Mockito.mock(Logger.class);
        WordAspect aspect = new WordAspect(argumentLogger);
        aspect.setLogger(logger);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("create");

        // Act
        aspect.aroundCreateAdvice(joinPoint);

        // Assert
        Mockito.verify(argumentLogger).processMethod(
                eq(joinPoint),
                any(),
                any());
    }

    @Test
    void aroundCreateAdvice_OtherMethod() throws Throwable {
        // Arrange
        ArgumentLogger argumentLogger = Mockito.mock(ArgumentLogger.class);
        Logger logger = Mockito.mock(Logger.class);
        WordAspect aspect = new WordAspect(argumentLogger);
        aspect.setLogger(logger);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);
        when(joinPoint.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("someOtherMethod");

        // Act
        aspect.aroundCreateAdvice(joinPoint);

        // Assert
        Mockito.verify(argumentLogger).processMethod(
                eq(joinPoint),
                any(),
                any());
    }

    @Test
    void aroundUpdateAdvice() throws Throwable {
        // Arrange
        ArgumentLogger argumentLogger = Mockito.mock(ArgumentLogger.class);
        Logger logger = Mockito.mock(Logger.class);
        WordAspect aspect = new WordAspect(argumentLogger);
        aspect.setLogger(logger);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);

        // Act
        aspect.aroundUpdateAdvice(joinPoint);

        // Assert
        Mockito.verify(argumentLogger).processMethod(
                eq(joinPoint),
                any(),
                any());
    }

    @Test
    void aroundReadAdvice() throws Throwable {
        // Arrange
        ArgumentLogger argumentLogger = Mockito.mock(ArgumentLogger.class);
        Logger logger = Mockito.mock(Logger.class);
        WordAspect aspect = new WordAspect(argumentLogger);
        aspect.setLogger(logger);
        ProceedingJoinPoint joinPoint = Mockito.mock(ProceedingJoinPoint.class);

        // Act
        aspect.aroundReadAdvice(joinPoint);

        // Assert
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

    @Test
    void testAroundGetAdvice_GetWordByTitle() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getWordByTitle");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Word by title found");

        // Act
        Object result = aspect.aroundGetAdvice(joinPointMock);

        // Assert
        assertEquals("Word by title found", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }
    @Test
    void testAroundGetAdvice_GetWordById() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getWordById");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Word by id found");

        // Act
        Object result = aspect.aroundGetAdvice(joinPointMock);

        // Assert
        assertEquals("Word by id found", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundGetAdvice_GetExistingById() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getExistingById");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Existing word found");

        // Act
        Object result = aspect.aroundGetAdvice(joinPointMock);

        // Assert
        assertEquals("Existing word found", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundGetAdvice_GetWordBySearch() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getWordBySearch");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Word by search found");

        // Act
        Object result = aspect.aroundGetAdvice(joinPointMock);

        // Assert
        assertEquals("Word by search found", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundGetAdvice_GetWordsFromPages() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getWordsFromPages");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Word from pages get");

        // Act
        Object result = aspect.aroundGetAdvice(joinPointMock);

        // Assert
        assertEquals("Word from pages get", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundGetAdvice_DefaultCase() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("someOtherMethod");

        // Act
        Object result = aspect.aroundGetAdvice(joinPointMock);

        // Assert
        assertEquals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), result);
    }

}
