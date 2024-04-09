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
public class PagesAspectTest {

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
        // Arrange
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Page with id delete");

        // Act
        Object result = aspect.aroundDeleteAdvice(joinPointMock);

        // Assert
        assertEquals("Page with id delete", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundCreateAdvice() throws Throwable {
        // Arrange
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Page Test add");

        // Act
        Object result = aspect.aroundCreateAdvice(joinPointMock);

        // Assert
        assertEquals("Page Test add", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundUpdateAdvice() throws Throwable {
        // Arrange
        // No specific behavior to mock, as the method does not call the argument logger

        // Act
        Object result = aspect.aroundUpdateAdvice(joinPointMock);

        // Assert
        assertEquals(null, result); // As no specific return value is provided
    }

    @Test
    void testAroundReadAdvice() throws Throwable {
        // Arrange
        // No specific behavior to mock, as the method does not call the argument logger

        // Act
        Object result = aspect.aroundReadAdvice(joinPointMock);

        // Assert
        assertEquals(null, result); // As no specific return value is provided
    }

    @Test
    void testCheckStartMethod_GetPageByPageId() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getPageByPageId");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Page by id delete");

        // Act
        Object result = aspect.checkStartMethod(joinPointMock);

        // Assert
        assertEquals("Page by id delete", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testCheckStartMethod_GetPagesBySearch() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getPagesBySearch");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("All pages by search are get");

        // Act
        Object result = aspect.checkStartMethod(joinPointMock);

        // Assert
        assertEquals("All pages by search are get", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testCheckStartMethod_DefaultCase() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("someOtherMethod");

        // Act
        Object result = aspect.checkStartMethod(joinPointMock);

        // Assert
        assertEquals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), result);
    }
}
