package org.example.mediawiki.aop;

import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PointCutsTest {

    @Test
    void testGetMethodsPagesPointcut() {
        assertPointcutNotNull(PointCuts.class, "getMethodsPages");
    }

    @Test
    void testCreateMethodsPagesPointcut() {
        assertPointcutNotNull(PointCuts.class, "createMethodsPages");
    }

    @Test
    void testUpdateMethodsPagesPointcut() {
        assertPointcutNotNull(PointCuts.class, "updateMethodsPages");
    }

    @Test
    void testDeleteMethodsPagesPointcut() {
        assertPointcutNotNull(PointCuts.class, "deleteMethodsPages");
    }

    @Test
    void testReadMethodsPagesPointcut() {
        assertPointcutNotNull(PointCuts.class, "readMethodsPages");
    }

    @Test
    void testGetMethodsSearchPointcut() {
        assertPointcutNotNull(PointCuts.class, "getMethodsSearch");
    }

    @Test
    void testCreateMethodsSearchPointcut() {
        assertPointcutNotNull(PointCuts.class, "createMethodsSearch");
    }

    @Test
    void testDeleteMethodsSearchPointcut() {
        assertPointcutNotNull(PointCuts.class, "deleteMethodsSearch");
    }

    @Test
    void testUpdateMethodsSearchPointcut() {
        assertPointcutNotNull(PointCuts.class, "updateMethodsSearch");
    }

    @Test
    void testReadMethodsSearchPointcut() {
        assertPointcutNotNull(PointCuts.class, "readMethodsSearch");
    }

    @Test
    void testGetMethodsWordPointcut() {
        assertPointcutNotNull(PointCuts.class, "getMethodsWord");
    }

    @Test
    void testCreateMethodsWordPointcut() {
        assertPointcutNotNull(PointCuts.class, "createMethodsWord");
    }

    @Test
    void testDeleteMethodsWordPointcut() {
        assertPointcutNotNull(PointCuts.class, "deleteMethodsWord");
    }

    @Test
    void testUpdateMethodsWordPointcut() {
        assertPointcutNotNull(PointCuts.class, "updateMethodsWord");
    }

    @Test
    void testReadMethodsWordPointcut() {
        assertPointcutNotNull(PointCuts.class, "readMethodsWord");
    }

    @Test
    void testGetMethodsWikiApiPointcut() {
        assertPointcutNotNull(PointCuts.class, "getMethodsWikiApi");
    }

    @Test
    void testMapMethodsWikiApiPointcut() {
        assertPointcutNotNull(PointCuts.class, "mapMethodsWikiApi");
    }

    private void assertPointcutNotNull(Class<?> clazz, String methodName) {
        try {
            Method method = clazz.getMethod(methodName);
            Pointcut pointcutAnnotation = method.getAnnotation(Pointcut.class);
            assertNotNull(pointcutAnnotation, "Pointcut expression is null");
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Method " + methodName + " not found in class " + clazz.getName(), e);
        }
    }
}
