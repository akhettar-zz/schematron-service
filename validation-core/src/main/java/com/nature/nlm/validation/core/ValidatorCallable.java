package com.nature.nlm.validation.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;

import com.nature.nlm.validation.core.exception.ValidationException;
import com.nature.nlm.validation.model.report.Report;
import com.thoughtworks.xstream.XStream;

/**
 * @author a.khettar
 * 
 */
public class ValidatorCallable implements Callable<Report> {

    private final Validator dtd;
    private final Validator schematron;

    /**
     * 
     */

    public ValidatorCallable(final Validator dtd, final XStream marshaller, final Validator schematron, InputStream xml) {

        this.dtd = dtd;
        this.schematron = schematron;

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public Report call() throws Exception {
        Report report = new Report();
        byte[] bytes = null;
        try {

            // copy into byte array for reuse.
            bytes = IOUtils.toByteArray(nlm);

            // Apply DTD validation
            LOG.debug("Applying DTD validation on {}: ", IOUtils.toString(new ByteArrayInputStream(bytes)));
            report = dtd.validate(new ByteArrayInputStream(bytes));

            // check if DTD validation successful, then proceed with schematron validation
            if (Util.validationSuccessful(report)) {
                LOG.info("DTD Validation successful");
                report = schematron.validate(new ByteArrayInputStream(bytes));
            }

            // log validation failure
            if (!Util.validationSuccessful(report)) {
                LOG.error("NLM Validation Failure for article: [{}] - Report : {}.", report.getHref(),
                        marshaller.toXML(report));
            } else {
                LOG.info("DTD and schematron Validation successful");
                report.setStatus(Constants.SUCCESS);
            }

            LOG.debug(marshaller.toXML(report));
            return report;

        } catch (ValidationException e) {
            report = Util.buildErrorReport(e);
        } catch (IOException e) {
            report = Util.buildErrorReport(e);
        } finally {
            try {

                Util.cleanUp(nlm);
            } catch (IOException e) {
                LOG.warn("Failed to close the inputStream", e);
            }
        }
        return report;
    }

}
