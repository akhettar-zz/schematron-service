package com.akhettar.validation.service.resource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;

import com.akhettar.validation.core.Constants;
import com.akhettar.validation.core.Util;
import com.akhettar.validation.core.exception.ValidationException;
import com.akhettar.validation.core.services.ValidationService;
import com.akhettar.validation.model.file.FileType;
import com.akhettar.validation.model.report.Report;
import com.akhettar.validation.model.report.Reports;
import com.google.inject.Inject;

/**
 * Example resource class hosted at the URI path "/myresource"
 */
@Path("/")
public class RestfulValidationServiceImp implements RestfulValidationService {

    private final ValidationService service;
    private static List<String> requests = new ArrayList<String>();


    @Inject
    public RestfulValidationServiceImp(ValidationService service) {
        this.service = service;
    }

    /**
     * Validates NLM XML as an XML input string.
     * 
     */
    @Override
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @Consumes(MediaType.TEXT_XML)
    @Path("xml")
    public Report validate(final String nlm) {
        requests.add(nlm);

        Report report = null;
        try {

            report = service.validate(IOUtils.toInputStream(nlm));
            return report;

        } catch (ValidationException e) {
            report = Util.buildErrorReport(e);
        }
        return report;

    }

    /**
     * Validates NLM XML as Inputstream : XML File.
     * 
     */
    @Override
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @Consumes(MediaType.APPLICATION_XML)
    @Path("stream/xml")
    public Reports validateXMLFile(final InputStream stream) {

        Reports reports = null;
        try {

            reports = service.validate(stream, FileType.XML);
            return reports;

        } catch (ValidationException e) {
            reports = Util.buildErrorReports(e);
        }
        return reports;

    }

    /**
     * Validates NLM XML as Inputstream : Zip File
     * 
     */
    @Override
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @Consumes(Constants.APPLICATION_ZIP)
    @Path("stream/zip")
    public Reports validateZipFile(final InputStream stream) {

        Reports reports = null;
        try {

            reports = service.validate(stream, FileType.ZIP);
            return reports;

        } catch (ValidationException e) {
            reports = Util.buildErrorReports(e);
        }
        return reports;

    }

    /**
     * Validates NLM XML as Inputstream , file details is provided.
     * 
     */
    @Override
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @Path("stream/{fileName}")
    public Reports validateAnyFile(final InputStream stream, @PathParam("fileName") String fileName) {

        Reports reports = null;
        try {

            reports = service.validate(stream, fileName);
            return reports;

        } catch (ValidationException e) {
            reports = Util.buildErrorReports(e);
        }
        return reports;

    }
}
