package org.example.mediawiki.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Cache {

        private Map<String, Object> cache = new ConcurrentHashMap<>();

        public void put(String key, Object value) {
            cache.put(key, value);
        }

        public Object get(String key) {
            return cache.get(key);
        }

        public void remove(String key) {
            cache.remove(key);
        }

        public void clear() {
            cache.clear();
        }

    public Map<String, Object> getCache() {
        return this.cache;
    }

}
