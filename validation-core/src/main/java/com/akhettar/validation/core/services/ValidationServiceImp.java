package com.akhettar.validation.core.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akhettar.validation.core.Constants;
import com.akhettar.validation.core.Util;
import com.akhettar.validation.core.Validator;
import com.akhettar.validation.core.exception.ValidationException;
import com.akhettar.validation.model.file.FileType;
import com.akhettar.validation.model.report.Report;
import com.akhettar.validation.model.report.Reports;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.thoughtworks.xstream.XStream;

/**
 * Validation service: carries out DTD and schematron validation.
 * Note that if the given NLM xml is not valid against the DTD,
 * the schematron validation is not performed.
 * 
 * @author a.khettar
 * 
 */
public class ValidationServiceImp implements ValidationService {

    private final Validator dtd;
    private final Validator schematron;
    private final Logger LOG = LoggerFactory.getLogger(ValidationServiceImp.class);
    private final String TEMP_FOLDER;

    private final XStream marshaller;

    @Inject
    public ValidationServiceImp(@Named("dtd") final Validator dtd, final XStream marshaller,
            @Named("schematron") final Validator schematron, @Named(Constants.TEMP_FOLDER) String temp_folder) {

        this.dtd = dtd;
        this.schematron = schematron;
        this.TEMP_FOLDER = temp_folder;
        this.marshaller = marshaller;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.nature.nlm.core.services.ValidatorService#validate(java.lang.String)
     */
    @Override
    public Report validate(final InputStream nlm) {
        Report report = new Report();
        byte[] bytes = null;
        try {

            // copy into byte array for reuse.
            bytes = IOUtils.toByteArray(nlm);

            // Apply DTD validation
            LOG.debug("Applying DTD validation on {}: ", IOUtils.toString(new ByteArrayInputStream(bytes)));
            report = dtd.validate(new ByteArrayInputStream(bytes));

            // check if DTD validation successful, then proceed with schematron validation
            if (!Util.validationSuccessful(report))
            {
                LOG.error("JATS Validation Failure for article: [{}] - Report : {}.", report.getHref(),
                        marshaller.toXML(report));
                return report;
            }
            report = schematron.validate(new ByteArrayInputStream(bytes));

            // log validation failure
            if (!Util.validationSuccessful(report)) {
                LOG.error("JATS Validation Failure for article: [{}] - Report : {}.", report.getHref(),
                        marshaller.toXML(report));
            } else {
                LOG.debug("DTD and schematron Validation successful, file: " + report.getHref());
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.nature.nlm.validation.core.services.ValidationService#validate(java
     * .io.InputStream, java.lang.String)
     */
    @Override
    public Reports validate(InputStream stream, String fileName) {
        Reports reports = null;

        if (Util.notXML(fileName)) {
            reports = handleZipFile(stream, fileName);

        } else {
            reports = handleXMLFile(stream, fileName);
        }

        return reports;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.nature.nlm.validation.core.services.ValidationService#validate(java
     * .io.InputStream,
     * com.nature.nlm.validation.core.model.file.FileType)
     */
    @Override
    public Reports validate(InputStream stream, FileType type) {
        Reports reports = null;
        if (type.equals(FileType.ZIP)) {
            reports = handleZipFile(stream, null);
        } else {
            reports = handleXMLFile(stream, null);
        }
        return reports;
    }

    /**
     * Handles a Zip file
     * 
     * @param stream
     *            the given inputStream
     * @param fileName
     *            file details
     * 
     * @return Validation reports.
     */
    @SuppressWarnings("unchecked")
    private Reports handleZipFile(final InputStream stream, final String fileName) {

        final String tempFileName = UUID.randomUUID().toString().concat(".zip");
        File destination = new File(TEMP_FOLDER.concat(tempFileName));
        Reports reports = new Reports();
        try {

            FileUtils.copyInputStreamToFile(stream, destination);
            ZipFile zipFile = new ZipFile(destination);

            final Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();
            reports.setHref(fileName);
            while (entries.hasMoreElements()) {

                final ZipEntry entry = entries.nextElement();

                // skip if it's not xml
                if (entry.isDirectory() || Util.notXML(entry.getName())) {
                    continue;
                }
                final InputStream in = zipFile.getInputStream(entry);
                final Report report = this.validate(in);
                report.setHref(entry.getName());
                reports.getReport().add(report);

            }

            Util.setReportsStatus(reports);
        } catch (IOException e) {
            reports = Util.buildErrorReports(e);

        } finally {
            // delete temporary zip file
            boolean deleted = FileUtils.deleteQuietly(destination);
            if (!deleted) {
                LOG.warn("Failed to delete temporary file: {}", destination);
            }
        }
        return reports;

    }

    /**
     * Handles XML file.
     * 
     * @param stream
     *            the inputStream
     * @param fileName
     *            file details.
     */
    private Reports handleXMLFile(final InputStream stream, final String fileName) {
        final Reports reports = new Reports();
        final Report report = this.validate(stream);
        report.setHref(fileName);
        reports.getReport().add(report);
        reports.setStatus(report.getStatus());
        report.setStatus(null);
        return reports;
    }

}
