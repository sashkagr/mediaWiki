package org.example.mediawiki.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Cache {
    private static final int MAXSIZE = 200;
    private final Map<String, Object> wikiCache = new ConcurrentHashMap<>();

    public void put(final String key, final Object value) {
        wikiCache.put(key, value);
        if (wikiCache.size() >= MAXSIZE) {
            wikiCache.clear();
        }
    }

    public Object get(final String key) {
        return wikiCache.get(key);
    }

    public void remove(final String key) {
        wikiCache.remove(key);
    }

    public void clear() {
        wikiCache.clear();
    }

    public Map<String, Object> getCache() {
        return this.wikiCache;
    }

}
