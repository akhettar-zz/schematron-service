package com.akhettar.validation.core.cache;

import org.junit.Before;
import org.junit.Test;

import com.akhettar.validation.core.cache.CachedObject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author a.khettar
 * 
 */
public class CachedObjectTest {

    private CachedObject<String, String> obj1;
    private CachedObject<String, String> obj2;
    private CachedObject<String, String> obj3;
    private CachedObject<String, String> obj4;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUpBeforeClass() throws Exception {

        obj1 = new CachedObject<String, String>("key1", "value1", 20);
        obj2 = new CachedObject<String, String>("key2", "valu2", 20);
        obj3 = new CachedObject<String, String>("key1", "value1", 20);
        obj4 = new CachedObject<String, String>(null, "value1", 20);

    }

    @Test
    public void testObjects() {

        assertTrue(obj1.equals(obj3));
        assertTrue(obj1.equals(obj1));
        assertFalse(obj1.equals(null));
        assertFalse(obj1.equals(new Object()));
        assertFalse(obj1.equals(obj2));
        assertEquals(obj3.hashCode(), obj1.hashCode());
        assertFalse(obj1.equals(obj4));

    }

    @Test
    public void testHasExpired() {
        assertFalse(obj1.hasExpired());
    }

    @Test
    public void testGetValue() {
        assertEquals("value1", obj1.getValue());
    }

}
