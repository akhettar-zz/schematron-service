package com.akhettar.validation.core.cache;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class representing the cached object.
 * 
 * @author a.khettar
 * 
 */
public class CachedObject<K, T> {

    private final K key;
    private final long expiry;
    private final T value;

    /**
     * Constructor.
     * 
     * @param key
     *            the object key.
     * @param value
     *            the object value.
     * @param expiry
     *            in minute by default.
     * @param timeUnit  time unit
     *
     */
    public CachedObject(K key, T value, int expiry, int timeUnit) {
        this.key = key;
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(timeUnit, expiry);
        this.expiry = calendar.getTimeInMillis();
        this.value = value;
    }

    /**
     * Constructor default in minuutes
     * 
     * @param key
     *            the object key.
     * @param value
     *            the object value.
     * @param expiry
     *            in minute by default.

     */
    public CachedObject(K key, T value, int expiry) {
        this(key, value, expiry, Calendar.MINUTE);
    }

    /**
     * {@inheritDoc}
     * 
     */

    public T getValue() {
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /**
     * Checks if the object has expired.
     * 
     * @return true if the cached object has expired.
     */
    public boolean hasExpired() {
        return (expiry - System.currentTimeMillis()) < 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof CachedObject))
            return false;
        CachedObject other = (CachedObject) obj;
        if (key == null) {
            if (other.key != null)
                return false;
        } else if (!key.equals(other.key))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

}
