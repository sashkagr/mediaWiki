package org.example.mediawiki.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


public class ControllerLoggingAspectTest {

    @Test
    public void logBefore() {
        // Arrange
        Logger logger = Mockito.mock(Logger.class);
        ControllerLoggingAspect aspect = new ControllerLoggingAspect();
        aspect.setLogger(logger);
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);

        Mockito.when(joinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.toShortString()).thenReturn("methodName");
        Mockito.when(joinPoint.getArgs()).thenReturn(new Object[]{"arg1", "arg2"});

        // Act
        aspect.logBefore(joinPoint);

        // Assert
        ArgumentCaptor<Object[]> argsCaptor = ArgumentCaptor.forClass(Object[].class);
        verify(logger).info(Mockito.eq("Method {} called with arguments {}"), Mockito.eq("methodName"), argsCaptor.capture());
        assertEquals("arg1", argsCaptor.getValue()[0].toString());
    }

    @Test
    public void logAfterReturning() {
        // Arrange
        Logger logger = Mockito.mock(Logger.class);
        ControllerLoggingAspect aspect = new ControllerLoggingAspect();
        aspect.setLogger(logger);
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);

        Mockito.when(joinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.toShortString()).thenReturn("methodName");

        // Act
        aspect.logAfterReturning(joinPoint, "returnValue");

        // Assert
        verify(logger).info("Method {} returned {}", "methodName", "returnValue");
    }
}
