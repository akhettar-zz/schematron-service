package com.akhettar.validation.core;


import java.io.InputStream;

import com.akhettar.validation.model.report.Report;

/**
 * DTD and Schematro validator interface.
 * 
 * @author a.khettar
 * 
 */
public interface Validator {

    /**
     * Validate the given NLM XML.
     * 
     * @param nlm
     * @return
     */
    Report validate(final InputStream nlm);

}