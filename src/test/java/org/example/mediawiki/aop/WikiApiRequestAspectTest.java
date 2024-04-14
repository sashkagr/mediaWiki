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
class WikiApiRequestAspectTest {

    @Mock
    private ProceedingJoinPoint joinPointMock;

    @Mock
    private MethodSignature methodSignatureMock;

    @Mock
    private ArgumentLogger argumentLoggerMock;

    @InjectMocks
    private WikiApiRequestAspect aspect;

    @Test
    void testAroundMapAdvice() throws Throwable {
        // Arrange
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Method map to word");

        // Act
        Object result = aspect.aroundMapAdvice(joinPointMock);

        // Assert
        assertEquals("Method map to word", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundGetAdvice_GetDescriptionByTitle() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getDescriptionByTitle");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Method get all pages");

        // Act
        Object result = aspect.aroundGetAdvice(joinPointMock);

        // Assert
        assertEquals("Method get all pages", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundGetAdvice_GetDescriptionByPageId() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getDescriptionByPageId");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Method get description by pageId");

        // Act
        Object result = aspect.aroundGetAdvice(joinPointMock);

        // Assert
        assertEquals("Method get description by pageId", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundGetAdvice_GetResponse() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getResponse");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Method get response");

        // Act
        Object result = aspect.aroundGetAdvice(joinPointMock);

        // Assert
        assertEquals("Method get response", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

    @Test
    void testAroundGetAdvice_GetApiSearchResponsesWords() throws Throwable {
        // Arrange
        when(joinPointMock.getSignature()).thenReturn(methodSignatureMock);
        when(methodSignatureMock.getName()).thenReturn("getApiSearchResponsesWords");
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Method get description by pageId");

        // Act
        Object result = aspect.aroundGetAdvice(joinPointMock);

        // Assert
        assertEquals("Method get description by pageId", result);
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
