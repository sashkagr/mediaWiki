package org.example.mediawiki.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Cache {

        private Map<String, Object> projectCache = new ConcurrentHashMap<>();

        public void put(String key, Object value) {
            projectCache.put(key, value);
        }

        public Object get(String key) {
            return projectCache.get(key);
        }

        public void remove(String key) {
            projectCache.remove(key);
        }

        public void clear() {
            projectCache.clear();
        }

    public Map<String, Object> getCache() {
        return this.projectCache;
    }

}
