package com.akhettar.validation.core;


import static com.akhettar.validation.core.Constants.TEMP_FOLDER;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

import com.akhettar.validation.model.file.FileDetails;
import com.akhettar.validation.model.file.FileType;
import com.akhettar.validation.model.report.Message;
import com.akhettar.validation.model.report.Report;
import com.akhettar.validation.model.report.Reports;

/**
 * 
 * Utility class.
 * 
 * @author a.khettar
 * 
 */
public final class Util {

    /**
     * Private constructor to emphasize the concept
     * of utility class.
     * 
     */
    private Util() {
        // do nothing utiltiy class
    }

    /**
     * Build default error message.
     * 
     * @param error
     * @return Error in Report format.
     */
    public static Report buildErrorReport(Throwable e) {
        final Message message = new Message();
        message.setContent("Failure: " + e.getMessage());
        final Report report = new Report();
        report.getMessage().add(message);
        report.setStatus(Constants.FAILURE);
        return report;
    }

    /**
     * Build default error message.
     * 
     * @param error
     * @return Error in Report format.
     */
    public static Reports buildErrorReports(Throwable e) {
        final Message message = new Message();
        message.setContent("Failure: " + e.getMessage());
        final Report report = new Report();
        report.getMessage().add(message);
        final Reports reports = new Reports();
        reports.getReport().add(report);
        reports.setStatus(Constants.FAILURE);
        return reports;
    }


    /**
     * Marshals report to string.
     * 
     * @param report
     *            the report object.
     * @return string representation of {@link Report}.
     * @throws JAXBException
     *             failure to marshall.
     */
    public static String marshalToString(Report report) throws JAXBException {

        final JAXBContext ctx = JAXBContext.newInstance(Report.class);
        final Marshaller marshaller = ctx.createMarshaller();
        StringWriter sw = new StringWriter();
        marshaller.marshal(report, sw);
        return sw.toString();

    }

    /**
     * Marshals report to string.
     * 
     * @param report
     *            the report object.
     * @return string representation of {@link Report}.
     * @throws JAXBException
     *             failure to marshall.
     */
    public static String marshalReportsToString(Reports report) throws JAXBException {

        final JAXBContext ctx = JAXBContext.newInstance(Reports.class);
        final Marshaller marshaller = ctx.createMarshaller();
        StringWriter sw = new StringWriter();
        marshaller.marshal(report, sw);
        return sw.toString();

    }

    /**
     * Builds successful report.
     */
    public static Report buildSuccessfulReport() {
        final Report report = new Report();
        final Message message = new Message();
        message.setContent(Constants.VALIDATION_SUCCESSFUL);
        report.getMessage().add(message);
        report.setStatus(Constants.SUCCESS);
        return report;
    }

    /**
     * Create Temporary file based on the given file details.
     * 
     * @param file
     *            the given file details.
     * @return null if the file is of type XML.
     */
    public static File createTempFile(final FileDetails file) {

        if (file.getType().equals(FileType.XML)) {
            return null;
        }
        return new File(TEMP_FOLDER.concat("nlm.zip"));

    }

    /**
     * Set File details in the report.
     * 
     * @param report
     *            the given report
     * @param details
     *            file details.
     */
    public static void setFileDetails(Report report, FileDetails details) {

        // We currently setting the href attribute
        report.setHref(details.getName());

    }

    /**
     * Basic method checking the extension of the file
     * if it is an XML or not.
     * 
     * @param name
     *            the file name.
     * @return true if it's an xml file.
     */
    public static boolean notXML(String name) {
        String XML_FILE_REGX = ".*\\s*\\.xml$";
        Pattern p = Pattern.compile(XML_FILE_REGX);
        return !(p.matcher(name).matches());

    }

    /**
     * Close all the streams safely.
     * 
     * @param baio
     * @param nlm
     * @param dtdInput
     * @param schematronInput
     * @throws IOException
     */
    public static void cleanUp(InputStream nlm) throws IOException {

        if (nlm != null) {
            nlm.close();
        }

    }

    /**
     * Checks if either DTD or schematron validation is successful.
     * 
     * @param report
     *            the generated report.
     * @return true if dtd validaiton successful.
     * 
     */
    public static boolean validationSuccessful(Report report) {
        return report.getStatus().equalsIgnoreCase(Constants.SUCCESS);

    }

    /**
     * @param node
     */
    public static String toString(Node node) {
        StringWriter writer = new StringWriter();
        Transformer transformer;
        try {
            transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(node), new StreamResult(writer));
        } catch (Exception e) {
            // log error.
        }
        return writer.toString();
    }

    /**
     * Sets the overall Report status.
     * 
     * @param reports
     */
    public static void setReportsStatus(final Reports reports) {

        if (reports == null) {
            throw new IllegalArgumentException("reports may not be null");
        }
        if (reports.getReport().size() == 0) {
            return;
        }

        boolean failure = false;
        boolean success = false;
        for (Report report : reports.getReport()) {
            if (Constants.FAILURE.equals(report.getStatus())) {
                reports.setStatus(Constants.FAILURE);
                failure = true;

            } else {
                success = true;
            }
        }
        if (!failure) {
            reports.setStatus(Constants.SUCCESS);
            return;
        }
        if (failure && success) {
            reports.setStatus(Constants.FAILURE_PARTIAL);
        }
    }

}