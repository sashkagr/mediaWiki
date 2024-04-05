package org.example.mediawiki.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

public class ControllerLoggingAspectTest {

    private ControllerLoggingAspect loggingAspect;

    @Mock
    private Logger mockLogger;

    @Mock
    private JoinPoint mockJoinPoint;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        loggingAspect = new ControllerLoggingAspect();
        loggingAspect.setLogger(mockLogger);
    }


    @Test
    public void testLogBefore() {
        Signature mockSignature = mock(Signature.class);
        when(mockSignature.toShortString()).thenReturn("MockMethod");
        Object[] args = {"arg1", "arg2"};

        when(mockJoinPoint.getSignature()).thenReturn(mockSignature);
        when(mockJoinPoint.getArgs()).thenReturn(args);

        loggingAspect.logBefore(mockJoinPoint);

        verify(mockLogger).info("Method {} called with arguments {}", "MockMethod", args);
    }

    @Test
    public void testLogAfterReturning() {
        Signature mockSignature = mock(Signature.class);
        when(mockSignature.toShortString()).thenReturn("MockMethod");
        Object result = "mockResult";

        when(mockJoinPoint.getSignature()).thenReturn(mockSignature);

        loggingAspect.logAfterReturning(mockJoinPoint, result);

        verify(mockLogger).info("Method {} returned {}", "MockMethod", result);
    }
}
