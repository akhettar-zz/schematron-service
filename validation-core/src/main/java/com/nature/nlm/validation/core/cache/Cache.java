package com.nature.nlm.validation.core.cache;


/**
 * 
 * Simple In-memory cache.
 * 
 * @author a.khettar
 * 
 */
public interface Cache<K, T> {

    /**
     * Stores object in cache.
     * 
     * @param key
     *            the object key
     * @param value
     *            the object value.
     * @param expiry
     *            the expiry, in minutes by default.
     */
    void store(K key, T value, int expiry);

    /**
     * Stores object in cache.
     * 
     * @param key
     *            the object key
     * @param value
     *            the object value.
     * @param expiry
     *            the expiry, in minutes by default.
     */
    void store(K key, T value, int expiry, int timUnit);

    /**
     * Gets the object for given key.
     * 
     * @param key
     *            the given object key.
     */
    T get(K key);
}
