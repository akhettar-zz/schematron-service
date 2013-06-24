package com.akhettar.validation.core.cache;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * Simple Object In-Memory cache to store none
 * serializable objects. The main use of this in-memory
 * cache in this project is to store compiled XSLT Tempates
 * which are not serialized objects, therefore cannot be
 * stored in memcached.
 * 
 * @author a.khettar
 * 
 */
public class ObjectMemoryCache<K, T> implements Cache<K, T> {

    /** Instance of cache. */
    private final ConcurrentHashMap<K, CachedObject<K, T>> cache = new ConcurrentHashMap<K, CachedObject<K, T>>();

    /*
     * (non-Javadoc)
     * 
     * @see com.nature.nlm.validation.core.cache.Cache#store(java.lang.Object,
     * java.lang.Object, int, java.util.concurrent.TimeUnit)
     */
    @Override
    public void store(K key, T value, final int expiry) {
        final CachedObject<K, T> object = new CachedObject<K, T>(key, value,
                expiry);
        cache.put(key, object);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.nature.nlm.validation.core.cache.Cache#store(java.lang.Object,
     * java.lang.Object, int, java.util.concurrent.TimeUnit)
     */
    @Override
    public void store(K key, T value, int expiry, int timeUnit) {

        if (key == null) {
            throw new IllegalArgumentException("Key may not be null");
        }
        final CachedObject<K, T> object = new CachedObject<K, T>(key, value,
                expiry, timeUnit);
        cache.put(key, object);
    }

    /**
     * Gets cached object, returns null if it has expired.
     * 
     * @param key
     *            cache key.
     * 
     * @return cached object.
     */
    @Override
    public T get(K key) {

        CachedObject<K, T> object = cache.get(key);

        if (object == null || object.hasExpired()) {
            // remove from cache.
            cache.remove(key);
            return null;
        }
        return object.getValue();

    }

}
