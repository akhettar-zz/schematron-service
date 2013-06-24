package com.akhettar.validation.core.exception;

import org.junit.Test;

import com.akhettar.validation.core.exception.ModulesConfigurationException;

import static org.junit.Assert.assertEquals;

/**
 * @author a.khettar
 * 
 */
public class ModulesConfigurationExceptionTest {

    @Test
    public void test() {
        ModulesConfigurationException e1 = new ModulesConfigurationException(
                new Throwable("fatal error"));
        ModulesConfigurationException e2 = new ModulesConfigurationException(
                "validation error", new Throwable("fatal error"));
        ModulesConfigurationException e3 = new ModulesConfigurationException(
                "validation error");

        assertEquals("java.lang.Throwable: fatal error", e1.getMessage());
        assertEquals("validation error", e2.getMessage());
        assertEquals("validation error", e3.getMessage());

    }

}
