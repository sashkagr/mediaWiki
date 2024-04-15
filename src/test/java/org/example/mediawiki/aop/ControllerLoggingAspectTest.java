package org.example.mediawiki.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;

import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.Mockito.*;

class ControllerLoggingAspectTest {

    private ControllerLoggingAspect aspect;


    @Captor
    private ArgumentCaptor<String> captor;

    @Mock
    private Logger loggerMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        aspect = new ControllerLoggingAspect();
        aspect.setLogger(loggerMock);
    }

    @Test
    void logBefore_shouldLogMethodAndArguments() {
        JoinPoint joinPointMock = mock(JoinPoint.class);
        Signature signatureMock = mock(Signature.class);
        when(joinPointMock.getSignature()).thenReturn(signatureMock);
        when(signatureMock.toString()).thenReturn(null);
        Object[] args = new Object[]{"arg1", "arg2"};
        when(joinPointMock.getArgs()).thenReturn(args);

        aspect.logBefore(joinPointMock);

        verify(loggerMock).info(eq("Method {} called with arguments {}"), any(), aryEq(args));
    }

    @Test
    void logAfterReturning_shouldLogMethodAndResult() {
        JoinPoint joinPointMock = mock(JoinPoint.class);
        Signature signatureMock = mock(MethodSignature.class);
        when(joinPointMock.getSignature()).thenReturn(signatureMock);
        when(signatureMock.toString()).thenReturn("TestMethod");
        Object result = "result";

        aspect.logAfterReturning(joinPointMock, result);

        verify(loggerMock).info(any(String.class), any(Object.class), any(Object.class));
    }
}
