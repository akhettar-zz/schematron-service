package com.akhettar.validation.service.resource;

import java.io.InputStream;

import com.akhettar.validation.model.report.Report;
import com.akhettar.validation.model.report.Reports;

/**
 * 
 * The Validation service interface. It validates against NLM DTD and
 * Schematron rule.
 * 
 * @author a.khettar
 * 
 */
public interface RestfulValidationService {

    /**
     * Validates an input NLM XML as a String.
     * 
     * @param nlm
     *            the given NLM xml
     * @return {@link Report}
     */
    Report validate(final String nlm);

    /**
     * Validates an input stream - XML file.
     * 
     * @param stream
     *            the given input stream to validate.
     * @return {@link Reports}.
     */
    Reports validateXMLFile(final InputStream stream);

    /**
     * Validates an input stream as a Zip file containing a list of XML.
     * 
     * @param stream
     *            the given inputStream.
     * @return {@link Reports}
     */
    Reports validateZipFile(final InputStream stream);

    /**
     * Validates ZIP and XML files as inputStream.
     * 
     * @param stream
     *            the given zip/xml file in an inputStream.
     * @param fileName
     *            the given file name.
     * @return {@link Reports}.
     */
    Reports validateAnyFile(final InputStream stream, final String fileName);
}
