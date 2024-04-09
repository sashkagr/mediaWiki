package org.example.mediawiki.aop;

import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PointCutsTest {

    @Test
    public void testGetMethodsPagesPointcut() {
        assertPointcutNotNull(PointCuts.class, "getMethodsPages");
    }

    @Test
    public void testCreateMethodsPagesPointcut() {
        assertPointcutNotNull(PointCuts.class, "createMethodsPages");
    }

    @Test
    public void testUpdateMethodsPagesPointcut() {
        assertPointcutNotNull(PointCuts.class, "updateMethodsPages");
    }

    @Test
    public void testDeleteMethodsPagesPointcut() {
        assertPointcutNotNull(PointCuts.class, "deleteMethodsPages");
    }

    @Test
    public void testReadMethodsPagesPointcut() {
        assertPointcutNotNull(PointCuts.class, "readMethodsPages");
    }

    @Test
    public void testGetMethodsSearchPointcut() {
        assertPointcutNotNull(PointCuts.class, "getMethodsSearch");
    }

    @Test
    public void testCreateMethodsSearchPointcut() {
        assertPointcutNotNull(PointCuts.class, "createMethodsSearch");
    }

    @Test
    public void testDeleteMethodsSearchPointcut() {
        assertPointcutNotNull(PointCuts.class, "deleteMethodsSearch");
    }

    @Test
    public void testUpdateMethodsSearchPointcut() {
        assertPointcutNotNull(PointCuts.class, "updateMethodsSearch");
    }

    @Test
    public void testReadMethodsSearchPointcut() {
        assertPointcutNotNull(PointCuts.class, "readMethodsSearch");
    }

    @Test
    public void testGetMethodsWordPointcut() {
        assertPointcutNotNull(PointCuts.class, "getMethodsWord");
    }

    @Test
    public void testCreateMethodsWordPointcut() {
        assertPointcutNotNull(PointCuts.class, "createMethodsWord");
    }

    @Test
    public void testDeleteMethodsWordPointcut() {
        assertPointcutNotNull(PointCuts.class, "deleteMethodsWord");
    }

    @Test
    public void testUpdateMethodsWordPointcut() {
        assertPointcutNotNull(PointCuts.class, "updateMethodsWord");
    }

    @Test
    public void testReadMethodsWordPointcut() {
        assertPointcutNotNull(PointCuts.class, "readMethodsWord");
    }

    @Test
    public void testGetMethodsWikiApiPointcut() {
        assertPointcutNotNull(PointCuts.class, "getMethodsWikiApi");
    }

    @Test
    public void testMapMethodsWikiApiPointcut() {
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
