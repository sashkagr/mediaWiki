package org.example.mediawiki.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Mockito.*;

class ControllerLoggingAspectTest {
    private ControllerLoggingAspect aspect;

    @Mock
    private Logger loggerMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        aspect = new ControllerLoggingAspect();
        aspect.setLogger(loggerMock);
    }

//    @Test
//    void logBefore_shouldLogMethodAndArguments() {
//        // Given
//        JoinPoint joinPointMock = mock(JoinPoint.class);
//        Signature signatureMock = mock(Signature.class);
//        when(joinPointMock.getSignature()).thenReturn(signatureMock);
//        when(signatureMock.toString()).thenReturn("TestMethod");
//        Object[] args = new Object[]{"arg1", "arg2"};
//        when(joinPointMock.getArgs()).thenReturn(args);
//
//        // When
//        aspect.logBefore(joinPointMock);
//
//        // Then
//        verify(loggerMock).info(eq("Method {} called with arguments {}"), anyString(), aryEq(args));
//    }
    @Test
    void logAfterReturning_shouldLogMethodAndResult() {
        // Given
        JoinPoint joinPointMock = mock(JoinPoint.class);
        Signature signatureMock = mock(MethodSignature.class);
        when(joinPointMock.getSignature()).thenReturn(signatureMock);
        when(signatureMock.toString()).thenReturn("TestMethod");
        Object result = "result";

        // When
        aspect.logAfterReturning(joinPointMock, result);

        // Then
        verify(loggerMock).info(any(String.class), any(Object.class), any(Object.class));
    }
}
