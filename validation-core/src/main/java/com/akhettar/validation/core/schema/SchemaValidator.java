package com.akhettar.validation.core.schema;

/**
 * Schema DTD validator. It validates the NLM input XML against
 * the NLM DTD.
 * @author a.khettar
 *
 */

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.akhettar.validation.core.Constants;
import com.akhettar.validation.core.Util;
import com.akhettar.validation.core.Validator;
import com.akhettar.validation.core.exception.ValidationException;
import com.akhettar.validation.model.report.Report;
import com.google.inject.Inject;

public class SchemaValidator implements Validator {

    private final Logger LOGGER = LoggerFactory.getLogger(SchemaValidator.class);
    private final XMLReader reader;
    private final NLMErrorHandler handler;

    @Inject
    public SchemaValidator(XMLReader reader, NLMErrorHandler handler) {
        this.reader = reader;
        this.handler = handler;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.nature.nlm.service.validator.core.Validator#validate(java.lang.String
     * )
     */
    @Override
    public Report validate(InputStream xml) {

        Report report = new Report();

        try {

            // try parse
            reader.setErrorHandler(handler);
            reader.parse(new InputSource(xml));
            report.getMessage().addAll(handler.getMessages());
            if (handler.getMessages().size() == 0) {
                report = Util.buildSuccessfulReport();
            } else {
                report.setStatus(Constants.FAILURE);
            }
            return report;

        } catch (SAXException e) {
            LOGGER.error(e.getMessage());
            report.getMessage().addAll(handler.getMessages());
            report.setStatus(Constants.FAILURE);
            return report;

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new ValidationException(e.getMessage(), e);
        } finally {
            // clean up the handler messages for batch validation
            handler.getMessages().clear();
        }

    }

}
