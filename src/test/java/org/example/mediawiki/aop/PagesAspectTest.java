package org.example.mediawiki.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagesAspectTest {

    @Mock
    private ProceedingJoinPoint joinPointMock;

    @Mock
    private MethodSignature methodSignatureMock;

    @Mock
    private ArgumentLogger argumentLoggerMock;

    @InjectMocks
    private PagesAspect aspect;

    @Test
    void testAroundDeleteAdvice() throws Throwable {
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Page with id delete");

        Object result = aspect.aroundDeleteAdvice(joinPointMock);

        assertEquals("Page with id delete", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundCreateAdvice() throws Throwable {
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Page Test add");

        Object result = aspect.aroundCreateAdvice(joinPointMock);

        assertEquals("Page Test add", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundUpdateAdvice() throws Throwable {
        Object result = aspect.aroundUpdateAdvice(joinPointMock);

        assertEquals(null, result); // As no specific return value is provided
    }

    @Test
    void testAroundReadAdvice() throws Throwable {
        Object result = aspect.aroundReadAdvice(joinPointMock);

        assertEquals(null, result); // As no specific return value is provided
    }

    @Test
    void testCheckStartMethod_GetPageByPageId() throws Throwable {
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getPageByPageId");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Page by id delete");

        Object result = aspect.checkStartMethod(joinPointMock);

        assertEquals("Page by id delete", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testCheckStartMethod_GetPagesBySearch() throws Throwable {
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getPagesBySearch");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("All pages by search are get");

        Object result = aspect.checkStartMethod(joinPointMock);

        assertEquals("All pages by search are get", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testCheckStartMethod_DefaultCase() throws Throwable {
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("someOtherMethod");

        Object result = aspect.checkStartMethod(joinPointMock);

        assertEquals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), result);
    }
}
