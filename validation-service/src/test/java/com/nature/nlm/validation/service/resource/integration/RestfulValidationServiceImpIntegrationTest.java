package com.nature.nlm.validation.service.resource.integration;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.nature.nlm.validation.core.Constants;
import com.nature.nlm.validation.model.report.Report;
import com.nature.nlm.validation.model.report.Reports;
import com.nature.nlm.validation.service.modules.NLMServiceGuiceConfiguration;
import com.nature.nlm.validation.service.resource.RestfulValidationServiceImp;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Integration Test for {@link RestfulValidationServiceImp}.
 * 
 * @author a.khettar
 * 
 */
public class RestfulValidationServiceImpIntegrationTest extends JerseyGuiceTest {

    private Client client;
    private static final String URL = "http://localhost:" + DEFAULT_PORT;
    private static final String NLM_XML_DIR = "nlm-xml/";
    private static final String NLM_ZIP_DIR = "nlm-zip/";

    /**
     * Public constructor to get the server started
     */
    public RestfulValidationServiceImpIntegrationTest() {

        super(new NLMServiceGuiceConfiguration(), "/rest/*");
    }

    @Override
    @Before
    public void setUP() throws Exception {
        client = Client.create();
        super.setUP();
    }

    @Test
    public void testPostXML() throws Exception {

        final InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream(NLM_XML_DIR + "scibx-valid.xml");
        final String nlm = IOUtils.toString(stream);
        final WebResource webResource = client.resource(URL + "/rest/xml");
        Report report = webResource.type(MediaType.TEXT_XML).post(Report.class, nlm);
        assertTrue(report.getMessage().size() == 1);
        assertEquals(Constants.VALIDATION_SUCCESSFUL, report.getMessage().get(0).getContent());
    }

    @Test
    public void testUploadFileXML() throws Exception {

        final InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream(NLM_XML_DIR + "scibx-valid.xml");
        final WebResource webResource = client.resource(URL + "/rest/stream/xml");
        Reports reports = webResource.type(MediaType.APPLICATION_XML).post(Reports.class, stream);
        assertTrue(reports.getReport().size() == 1);
        assertTrue(reports.getReport().get(0).getMessage().size() == 1);
        assertEquals(Constants.VALIDATION_SUCCESSFUL, reports.getReport().get(0).getMessage().get(0).getContent());
    }

    @Test
    public void testUploadFileZip() throws Exception {

        final InputStream stream = this.getClass().getClassLoader().getResourceAsStream(NLM_ZIP_DIR + "nlm.zip");
        final WebResource webResource = client.resource(URL + "/rest/stream/zip");
        Reports reports = webResource.type("application/zip").post(Reports.class, stream);
        assertEquals(5, reports.getReport().size());

        Report report = getReport(reports, "scibx-dtd-schematron-error.xml");
        assertDTDAndSchematronError(report);

    }

    //    @Test
    //    public void testUploadLargeFileZip() throws Exception {
    //
    //        final InputStream stream = this.getClass().getClassLoader().getResourceAsStream(NLM_ZIP_DIR + "Large.zip");
    //        final WebResource webResource = client.resource(URL + "/rest/stream/zip");
    //        Reports reports = webResource.type("application/zip").post(Reports.class, stream);
    //        assertEquals(300, reports.getReport().size());
    //
    //    }
}
