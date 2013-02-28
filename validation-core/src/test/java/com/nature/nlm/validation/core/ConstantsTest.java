package com.nature.nlm.validation.core;

import org.junit.Test;

import com.nature.nlm.validation.core.Constants;

import static org.junit.Assert.assertEquals;

/**
 * @author a.khettar
 * 
 */
public class ConstantsTest {

    @Test
    public void test() {

        assertEquals("VALIDATION SUCCESSFUL", Constants.VALIDATION_SUCCESSFUL);
        assertEquals("svrlToReport.xsl", Constants.REPORT_XSLT);

    }
}
