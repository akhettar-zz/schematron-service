package com.nature.nlm.validation.core.services;

import com.nature.nlm.validation.model.report.Report;
import com.nature.nlm.validation.model.report.Reports;

/**
 * 
 * Test fixture.
 * 
 * 
 * 
 * @author a.khettar
 * 
 */
public class ValidationServiceTestFixture {

    public final String DTD_ERROR = "scibx-dtd-error.xml";
    public final String DTD_SCHEMATRON_ERROR = "scibx-dtd-schematron-error.xml";
    public final String NOT_WELL_FOREMD = "scibx-not-well-formed.xml";
    public final String NOT_XML = "scibx-not-xml.pdf";
    public final String SCHEMATRON_ERROR = "scibx-schematron-error.xml";
    public final String VALID = "scibx-valid.xml";

    /**
     * Gets the relevant report based on href
     * 
     * @param reports
     *            the reports containing list of report
     * @param fileName
     * @param file
     *            name.
     * @return {@link Report}.
     */
    public Report getReport(Reports reports, String fileName) {

        for (Report report : reports.getReport()) {
            if (report.getHref().equalsIgnoreCase(fileName)) {
                return report;
            }
        }
        return null;
    }

}
