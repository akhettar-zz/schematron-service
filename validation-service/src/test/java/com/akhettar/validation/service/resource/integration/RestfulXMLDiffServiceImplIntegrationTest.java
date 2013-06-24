package com.akhettar.validation.service.resource.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.Test;

import com.akhettar.validation.core.Constants;
import com.akhettar.validation.model.report.Report;
import com.akhettar.validation.service.modules.NLMServiceGuiceConfiguration;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

/**
 * Created by IntelliJ IDEA.
 * User: a.khettar
 * Date: 18/07/2012
 * Time: 09:59
 * To change this template use File | Settings | File Templates.
 */
public class RestfulXMLDiffServiceImplIntegrationTest extends JerseyGuiceTest{


    private Client client;
    private static final String URL = "http://localhost:" + DEFAULT_PORT;

    /**
     * Public constructor to get the server started
     */
    public RestfulXMLDiffServiceImplIntegrationTest() {

        super(new NLMServiceGuiceConfiguration(), "/rest/*");
    }

    @Override
    @Before
    public void setUP() throws Exception {
        client = Client.create();
        super.setUP();
    }


    @Test
    public void testSimilarArticles()throws Exception{

        final InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("xmldiff/similar.xml");
        final WebResource webResource = client.resource(URL + "/rest/xmldiff");
        Report report = webResource.type(MediaType.TEXT_XML).post(Report.class, stream);
        assertTrue(report.getMessage().size() == 1);
        assertEquals(Constants.SUCCESS, report.getStatus());
        assertEquals("Similar article", report.getMessage().get(0).getContent());
    }

    @Test
    public void testDifferentArticles()throws Exception{

        final InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("xmldiff/different.xml");
        final WebResource webResource = client.resource(URL + "/rest/xmldiff");
        Report report = webResource.type(MediaType.TEXT_XML).post(Report.class, stream);
        assertEquals(Constants.FAILURE, report.getStatus());
        assertEquals(1, report.getMessage().size());
        assertEquals("Expected number of child nodes '8' but was '6' - comparing <pubfm...> at /article[1]/pubfm[1] to <pubfm...> at /article[1]/pubfm[1]", report.getMessage().get(0).getContent());
    }

    @Test
    public void testSimilarIgnoreWhiteSpaceAndComments()throws Exception{

        final InputStream stream = this.getClass().getClassLoader()
                .getResourceAsStream("xmldiff/similar-ignore-whitespace.xml");
        final WebResource webResource = client.resource(URL + "/rest/xmldiff");
        Report report = webResource.type(MediaType.TEXT_XML).post(Report.class, stream);
        assertTrue(report.getMessage().size() == 1);
        assertEquals(Constants.SUCCESS, report.getStatus());
        assertEquals("Similar article", report.getMessage().get(0).getContent());
    }
}
