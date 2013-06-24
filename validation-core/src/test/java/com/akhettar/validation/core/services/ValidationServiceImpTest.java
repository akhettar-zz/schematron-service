package com.akhettar.validation.core.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.akhettar.validation.core.modules.ValidationServiceModule;
import com.akhettar.validation.model.file.FileType;
import com.akhettar.validation.model.report.Report;
import com.akhettar.validation.model.report.Reports;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Unit test for {@link ValidationServiceImp}.
 * 
 * @author a.khettar
 * 
 */
public class ValidationServiceImpTest extends ValidationServiceTestFixture {

    private ValidationService service;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        Injector injector = Guice.createInjector(new ValidationServiceModule());
        service = injector.getInstance(ValidationService.class);

    }

    /**
     * Test method for {@link com.akhettar.validation.core.services.ValidationServiceImp#validate(InputStream)} .
     */
    @Test
    public void testValidateValidNLMXML() throws Exception {

        InputStream nlm = this.getClass().getClassLoader().getResourceAsStream(VALID);
        final Report report = service.validate(nlm);
        assertValidNLMXML(report);

    }

    /**
     * Test method for {@link com.akhettar.validation.core.services.ValidationServiceImp#validate(InputStream)} .
     */
    @Test
    public void testValidateDTDError() throws Exception {

        InputStream nlm = this.getClass().getClassLoader().getResourceAsStream(DTD_ERROR);
        final Report report = service.validate(nlm);
        this.assertDTDError(report);

    }

    /**
     * Test method for {@link com.akhettar.validation.core.services.ValidationServiceImp#validate(InputStream)} .
     */
    @Test
    public void testValidateSchematronError() throws Exception {

        InputStream nlm = this.getClass().getClassLoader().getResourceAsStream(SCHEMATRON_ERROR);
        final Report report = service.validate(nlm);
        assertSchematronError(report);

    }

    /**
     * Test method for {@link com.akhettar.validation.core.services.ValidationServiceImp#validate(InputStream)} .
     */
    @Test
    public void testValidateDTDAndSchematronError() throws Exception {

        InputStream nlm = this.getClass().getClassLoader().getResourceAsStream(DTD_SCHEMATRON_ERROR);
        final Report report = service.validate(nlm);
        this.assertDTDAndSchematronError(report);

    }

    /**
     * Test method for {@link com.akhettar.validation.core.services.ValidationServiceImp#validate(InputStream)} .
     */
    @Test
    public void testValidateNotWellFormedXML() throws Exception {

        InputStream nlm = this.getClass().getClassLoader().getResourceAsStream(NOT_WELL_FOREMD);
        final Report report = service.validate(nlm);
        assertNotWellFormed(report);

    }

    /**
     * Test method for {@link com.akhettar.validation.core.services.ValidationServiceImp#validate(InputStream)} .
     */
    @Test
    public void testValidateNotXML() throws Exception {

        InputStream nlm = this.getClass().getClassLoader().getResourceAsStream(NOT_XML);
        final Report report = service.validate(nlm);
        assertNotXML(report);

    }

    /**
     * Test flat zip file.
     */
    @Test
    public void testValidateZipFile() throws Exception {

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("jats.zip");

        Reports reports = service.validate(in, FileType.ZIP);
        assertEquals(5, reports.getReport().size());
        this.assertZipFile(reports);

    }

    /**
     * Test method for
     * {@link com.akhettar.validation.core.services.ValidationServiceImp#validate(InputStream,FileType)} .
     * 
     * @throws Exception
     */
    @Test
    public void testValidagteStructuredZipFile() throws Exception {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("jats-structured.zip");
        Reports reports = service.validate(in, FileType.ZIP);
        assertEquals(5, reports.getReport().size());
    }

    /**
     * Test method for
     * {@link com.akhettar.validation.core.services.ValidationServiceImp#validate(InputStream,FileType)} .
     * 
     * @throws Exception
     */
    @Test
    public void testSchematronErrorFile() throws Exception {
        InputStream nlm = this.getClass().getClassLoader().getResourceAsStream(SCHEMATRON_ERROR);
        final Reports reports = service.validate(nlm, FileType.XML);
        assertEquals(1, reports.getReport().size());
        assertSchematronError(reports.getReport().get(0));

    }

    /**
     * Test method for {@link com.akhettar.validation.core.services.ValidationServiceImp#validate(InputStream,String)}
     * .
     * 
     * @throws Exception
     */
    @Test
    public void testValidateFileXML() throws Exception {
        InputStream nlm = this.getClass().getClassLoader().getResourceAsStream(SCHEMATRON_ERROR);
        final Reports reports = service.validate(nlm, SCHEMATRON_ERROR);
        assertEquals(1, reports.getReport().size());
        assertEquals(SCHEMATRON_ERROR, reports.getReport().get(0).getHref());
        assertSchematronError(reports.getReport().get(0));

    }

    /**
     * Test method for {@link com.akhettar.validation.core.services.ValidationServiceImp#validate(InputStream,String)}
     * .
     * 
     * @throws Exception
     */
    @Test
    public void testValidateFileZIP() throws Exception {
        InputStream nlm = this.getClass().getClassLoader().getResourceAsStream("jats.zip");
        Reports reports = service.validate(nlm, "jats.zip");
        assertEquals(5, reports.getReport().size());
        this.assertZipFile(reports);

    }

    //    /**
    //     * Test method for {@link com.nature.nlm.validation.core.services.ValidationServiceImp#validate(InputStream,String)}
    //     * .
    //     * 
    //     * @throws Exception
    //     */
    //    @Test
    //    public void testValidateLargZipFile() throws Exception {
    //        StopWatch watch = new StopWatch();
    //
    //        InputStream nlm = this.getClass().getClassLoader().getResourceAsStream("Valid.zip");
    //        watch.start();
    //        Reports reports = service.validate(nlm, "nlm.zip");
    //        watch.stop();

    //
    //    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                  Private Methods
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Asserts Zip file.
     * 
     * @param reports
     */
    private void assertZipFile(Reports reports) {
        // DTD and schematron error
        final Report report1 = getReport(reports, DTD_SCHEMATRON_ERROR);
        this.assertDTDAndSchematronError(report1);

        // DTD errors
        final Report report2 = getReport(reports, DTD_ERROR);
        this.assertDTDError(report2);

        // not well formed
        final Report report3;
        report3 = getReport(reports, NOT_WELL_FOREMD);
        this.assertNotWellFormed(report3);

        // valid NLM XML.
        final Report report6 = getReport(reports, VALID);
        this.assertValidNLMXML(report6);

    }

    /**
     * Asserts Valid XML.
     * 
     * @param report
     */
    private void assertValidNLMXML(final Report report) {
        assertEquals("VALIDATION SUCCESSFUL", report.getMessage().get(0).getContent());
        assertNotNull(report);
    }

    /**
     * Asserts DTD error.
     * 
     * @param report
     */
    private void assertDTDError(final Report report) {
        assertNotNull(report);
        assertEquals(1, report.getMessage().size());
        assertTrue(report.getMessage().get(0).getContent().contains("The content of element type \"back\" must match"));
        assertEquals(new Integer(212), report.getMessage().get(0).getLine());
        assertEquals(new Integer(8), report.getMessage().get(0).getColumn());
        assertEquals("error", report.getMessage().get(0).getType());
        assertEquals("DTD", report.getMessage().get(0).getValidatedBy());
    }

    /**
     * Asserts Schematron error.
     * 
     * @param report
     */
    private void assertSchematronError(final Report report) {
        assertNotNull(report);

        assertTrue(report.getMessage().get(0).getContent()
                .contains("The \"journal-id\" element should have attribute:"));
        assertEquals(new Integer(6), report.getMessage().get(0).getLine());
        assertEquals(new Integer(21), report.getMessage().get(0).getColumn());
        assertEquals("/article[1]/front[1]/journal-meta[1]/journal-id[1]", report.getMessage().get(0).getLocation());
        assertEquals("schematron", report.getMessage().get(0).getValidatedBy());
        assertEquals("jmeta1", report.getMessage().get(0).getId());

        assertTrue(report
                .getMessage()
                .get(11)
                .getContent()
                .contains(
                        "Use \"list-type\" attribute to show type of list used. Allowed values are: none, bullet, number, lcletter"));
        assertEquals(new Integer(130), report.getMessage().get(11).getLine());
        assertEquals(new Integer(20), report.getMessage().get(11).getColumn());
        assertEquals("/article[1]/body[1]/sec[3]/p[1]/list[1]", report.getMessage().get(11).getLocation());
        assertEquals("schematron", report.getMessage().get(11).getValidatedBy());
        assertEquals("list3a", report.getMessage().get(11).getId());

    }

    /**
     * Asserts DTD and Schemaron error.
     * 
     * @param report
     */
    private void assertDTDAndSchematronError(final Report report) {
        assertNotNull(report);
        assertEquals(2, report.getMessage().size());

        assertTrue(report.getMessage().get(0).getContent()
                .contains("The content of element type \"journal-meta\" must match"));
        assertEquals(new Integer(10), report.getMessage().get(0).getLine());
        assertEquals(new Integer(20), report.getMessage().get(0).getColumn());
        assertEquals("error", report.getMessage().get(0).getType());
        assertEquals("DTD", report.getMessage().get(0).getValidatedBy());

        assertTrue(report
                .getMessage()
                .get(1)
                .getContent()
                .contains(
                        "The content of element type \"name\" must match \"(((surname,given-names?)|given-names),prefix?,suffix?)\""));
        assertEquals(new Integer(125), report.getMessage().get(1).getLine());
        assertEquals(new Integer(84), report.getMessage().get(1).getColumn());
        assertEquals("error", report.getMessage().get(1).getType());
        assertEquals("DTD", report.getMessage().get(1).getValidatedBy());
    }

    /**
     * Asserts not well formed
     * 
     * @param report
     */
    private void assertNotWellFormed(final Report report) {
        assertNotNull(report);
        assertEquals(1, report.getMessage().size());

        assertTrue(report.getMessage().get(0).getContent()
                .contains("XML document structures must start and end within the same entity."));
        assertEquals(new Integer(82), report.getMessage().get(0).getLine());
        assertEquals(new Integer(1), report.getMessage().get(0).getColumn());
        assertEquals("fatal-error", report.getMessage().get(0).getType());
        assertEquals("DTD", report.getMessage().get(0).getValidatedBy());
    }

    /**
     * Asserts not xml.
     * 
     * @param report
     */
    private void assertNotXML(final Report report) {
        assertNotNull(report);
        assertEquals(1, report.getMessage().size());

        assertTrue(report.getMessage().get(0).getContent().contains("Content is not allowed in prolog"));
        assertEquals(new Integer(1), report.getMessage().get(0).getLine());
        assertEquals(new Integer(1), report.getMessage().get(0).getColumn());
        assertEquals("fatal-error", report.getMessage().get(0).getType());
        assertEquals("DTD", report.getMessage().get(0).getValidatedBy());
    }

}
