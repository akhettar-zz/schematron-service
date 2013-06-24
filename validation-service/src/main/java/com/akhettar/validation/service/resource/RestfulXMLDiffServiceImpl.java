package com.akhettar.validation.service.resource;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.akhettar.validation.core.Util;
import com.akhettar.validation.core.exception.ValidationException;
import com.akhettar.validation.core.services.XMLDiffService;
import com.akhettar.validation.model.report.Report;
import com.google.inject.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: a.khettar
 * Date: 18/07/2012
 * Time: 09:44
 * To change this template use File | Settings | File Templates.
 */
@Path("/xmldiff")
public class RestfulXMLDiffServiceImpl implements RestfulXMLDiffService {


     private final XMLDiffService service;


    /**
     * Constructor
     * @param service
     */
    @Inject
    public RestfulXMLDiffServiceImpl (final XMLDiffService service)
    {
        this.service = service;
    }




    /**
     * Apply xml diff using XMLUnit library.
     *
     * @param xml a wrapper containing both XML to apply
     *            diff against.
     * @return full report of the xml diff.
     */
    @Override
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    @Consumes(MediaType.TEXT_XML)
    public Report diff(final InputStream xml) {
        
        try{
            return service.diff(xml);
        }catch (ValidationException e)
        {
            return Util.buildErrorReport(e);
        }
    }
}
