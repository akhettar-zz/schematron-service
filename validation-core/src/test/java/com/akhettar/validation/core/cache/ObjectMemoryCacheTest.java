package com.akhettar.validation.core.cache;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import com.akhettar.validation.core.cache.ObjectMemoryCache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit test {@link ObjectMemoryCache}
 * 
 * @author a.khettar
 * 
 */
public class ObjectMemoryCacheTest {

    private ObjectMemoryCache<String, String> cache;

    @Before
    public void setUP() throws Exception {
        cache = new ObjectMemoryCache<String, String>();
    }

    /**
     * Test method for
     * {@link com.akhettar.validation.core.cache.ObjectMemoryCache#store(java.lang.String, java.lang.Class, Expiry)}
     * .
     */
    @Test
    public void testStore() {

        //store something in the case
        cache.store("key1", "value1", 2);
        cache.store("key2", "value2", 2);

        assertEquals("value1", cache.get("key1"));
        assertEquals("value2", cache.get("key2"));
    }

    /**
     * Test method for
     * {@link com.akhettar.validation.core.cache.ObjectMemoryCache#get(java.lang.String)}
     * .
     */
    @Test
    public void testGetExpired() throws Exception {

        //store something in the case
        cache.store("key1", "value1", 1, Calendar.SECOND);
        cache.store("key2", "value2", 1);

        // assert null
        Thread.sleep(2000);
        assertNull(cache.get("key1"));
        assertEquals("value2", cache.get("key2"));

    }

    /**
     * Test method for
     * {@link com.akhettar.validation.core.cache.ObjectMemoryCache#get(java.lang.String)}
     * .
     */
    @Test
    public void testGetReturnsNull() throws Exception {

        assertNull(cache.get("key1"));

    }
}
