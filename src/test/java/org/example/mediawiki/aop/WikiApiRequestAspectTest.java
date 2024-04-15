package org.example.mediawiki.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;
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
        when(argumentLoggerMock.processMethod(any(), any(), any())).thenReturn("Method map to word");

        Object result = aspect.aroundMapAdvice(joinPointMock);

        assertEquals("Method map to word", result);
        verify(argumentLoggerMock).processMethod(eq(joinPointMock), any(), any());
    }

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
                arguments("getDescriptionByTitle", "Method get all pages"),
                arguments("getDescriptionByPageId", "Method get description by pageId"),
                arguments("getResponse", "Method get response"),
                arguments("getApiSearchResponsesWords", "Method get description by pageId")
        );
    }
}
