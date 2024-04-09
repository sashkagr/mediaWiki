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
public class SearchAspectTest {

    @Mock
    private ProceedingJoinPoint joinPointMock;

    @Mock
    private MethodSignature methodSignatureMock;

    @Mock
    private ArgumentLogger argumentLoggerMock;

    @InjectMocks
    private SearchAspect aspect;

    @Test
    void testAroundDeleteAdvice() throws Throwable {
        // Arrange
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Search with id delete");

        // Act
        Object result = aspect.aroundDeleteAdvice(joinPointMock);

        // Assert
        assertEquals("Search with id delete", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundCreateAdvice_CreateMethod() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("create");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Search Test add");

        // Act
        Object result = aspect.aroundCreateAdvice(joinPointMock);

        // Assert
        assertEquals("Search Test add", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundCreateAdvice_CreateSearchAndPagesMethod() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("createSearchAndPages");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Method create pages and search");

        // Act
        Object result = aspect.aroundCreateAdvice(joinPointMock);

        // Assert
        assertEquals("Method create pages and search", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundCreateAdvice_OtherMethod() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("someOtherMethod");

        // Act
        Object result = aspect.aroundCreateAdvice(joinPointMock);

        // Assert
        assertEquals(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR), result);
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
    void testCheckStartMethod_GetSearchById() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getSearchById");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Search by id found");

        // Act
        Object result = aspect.checkStartMethod(joinPointMock);

        // Assert
        assertEquals("Search by id found", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testCheckStartMethod_GetSearchByTitle() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getSearchByTitle");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Search by title found");

        // Act
        Object result = aspect.checkStartMethod(joinPointMock);

        // Assert
        assertEquals("Search by title found", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testCheckStartMethod_GetSearchExistingById() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getSearchExistingById");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Method find existing search");

        // Act
        Object result = aspect.checkStartMethod(joinPointMock);

        // Assert
        assertEquals("Method find existing search", result);
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
