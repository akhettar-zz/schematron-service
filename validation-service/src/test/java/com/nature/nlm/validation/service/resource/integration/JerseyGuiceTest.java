package com.nature.nlm.validation.service.resource.integration;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.nature.nlm.validation.model.report.Report;
import com.nature.nlm.validation.model.report.Reports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * 
 * Simple Jersey Guice Test fixture. It starts Runs Jersey server
 * in memory and stops it the test suite is finished.
 * 
 * @author a.khettar
 * 
 */
public class JerseyGuiceTest {

    private final Logger logger = LoggerFactory.getLogger(JerseyGuiceTest.class);
    private final GuiceServletContextListener ctxListener;
    private final int port;
    private final Server server;
    private final static String DEFAULT_PATH_SPEC = "/*";
    private String pathSpec;
    public final static int DEFAULT_PORT = 9090;

    /**
     * Public constructor
     * 
     * @param ctxLister
     *            an instance of {@link GuiceServletContextListener}.
     */
    public JerseyGuiceTest(final GuiceServletContextListener ctxLister) {
        this(ctxLister, DEFAULT_PORT);
    }

    /**
     * Public constructor
     * 
     * @param ctxLister
     *            an instance of {@link GuiceServletContextListener}.
     */
    public JerseyGuiceTest(final GuiceServletContextListener ctxLister, final int port) {
        this.ctxListener = ctxLister;
        this.port = port;
        this.server = new Server(port);

    }

    public JerseyGuiceTest(final GuiceServletContextListener ctxLister, final String pathSpec) {
        this(ctxLister);
        this.pathSpec = pathSpec;
    }

    @Before
    public void setUP() throws Exception {

        logger.info("Starting the server on port: {}", (port == 0) ? port : DEFAULT_PORT);

        // create context
        ServletContextHandler handler = new ServletContextHandler(server, "/");

        // add guice listener
        handler.addEventListener(ctxListener);

        // add GuiceFilter 
        handler.addFilter(GuiceFilter.class, pathSpec != null ? pathSpec : DEFAULT_PATH_SPEC, null);

        // add DefaultServlet for embedded Jetty. 
        // Failing to do this will cause 404 errors.
        // This is not needed if web.xml is used instead.
        handler.addServlet(DefaultServlet.class, "/");

        server.start();
        logger.info("Server started");

    }

    @After
    public void tearDown() throws Exception {
        logger.info("Stopping the server");
        server.stop();
    }

    /**
     * Gets the relevant report based on href
     * 
     * @param reports
     *            the reports containing list of report
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

    /**
     * @param report
     */
    public void assertDTDAndSchematronError(final Report report) {
        assertNotNull(report);
        assertEquals(2, report.getMessage().size());

        assertTrue(report.getMessage().get(0).getContent()
                .contains("The content of element type \"journal-meta\" must match"));
        assertEquals(new Integer(10), report.getMessage().get(0).getLine());
        assertEquals(new Integer(20), report.getMessage().get(0).getColumn());
        assertEquals("error", report.getMessage().get(0).getType());
        assertEquals("DTD", report.getMessage().get(0).getValidatedBy());

        assertTrue(report.getMessage().get(1).getContent()
                .contains("The content of element type \"name\" must match \"(surname,given-names?,prefix?"));
        assertEquals(new Integer(125), report.getMessage().get(1).getLine());
        assertEquals(new Integer(84), report.getMessage().get(1).getColumn());
        assertEquals("error", report.getMessage().get(1).getType());
        assertEquals("DTD", report.getMessage().get(1).getValidatedBy());
    }
}
