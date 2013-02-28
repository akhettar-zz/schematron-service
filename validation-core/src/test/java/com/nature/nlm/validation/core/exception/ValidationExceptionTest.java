package com.nature.nlm.validation.core.exception;

import org.junit.Test;

import com.nature.nlm.validation.core.exception.ValidationException;

import static org.junit.Assert.assertEquals;

/**
 * @author a.khettar
 * 
 */
public class ValidationExceptionTest {

    @Test
    public void test() {
        ValidationException e1 = new ValidationException(new Throwable(
                "fatal error"));
        ValidationException e2 = new ValidationException("validation error",
                new Throwable("fatal error"));

        assertEquals("java.lang.Throwable: fatal error", e1.getMessage());
        assertEquals("validation error", e2.getMessage());

    }

}
