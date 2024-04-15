package org.example.mediawiki.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CacheTest {

    private Cache cache;

    @BeforeEach
    void setUp() {
        cache = new Cache();
    }

    @Test
    void testPutAndGet() {
        String key = "testKey";
        String value = "testValue";

        cache.put(key, value);

        assertEquals(value, cache.get(key));
    }

    @Test
    void testRemove() {
        String key = "testKey";
        String value = "testValue";
        cache.put(key, value);

        cache.remove(key);

        assertNull(cache.get(key));
    }

    @Test
    void testClear() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        cache.clear();

        assertEquals(0, cache.getCache().size());
    }

    @Test
    void testEviction() {
        for (int i = 0; i < 201; i++) {
            cache.put("key" + i, "value" + i);
        }

        cache.put("keyNew", "valueNew");

        assertNull(cache.get("key0"));
        assertNotNull(cache.get("keyNew"));
    }
}
