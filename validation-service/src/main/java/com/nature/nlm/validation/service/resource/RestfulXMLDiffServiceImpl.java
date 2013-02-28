package com.nature.nlm.validation.service.resource;

import com.google.inject.Inject;
import com.nature.nlm.validation.core.Util;
import com.nature.nlm.validation.core.exception.ValidationException;
import com.nature.nlm.validation.core.services.XMLDiffService;
import com.nature.nlm.validation.model.report.Report;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: a.khettar
 * Date: 18/07/2012
 * Time: 09:44
 * To change this template use File | Settings | File Templates.
 */
@Path("/xmldiff")
public class RestfulXMLDiffServiceImpl implements RestfulXMLDiffService {


     private XMLDiffService service;


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
        
        Report report = null;
        try{
            return service.diff(xml);
        }catch (ValidationException e)
        {
            return report = Util.buildErrorReport(e);
        }
    }
}
