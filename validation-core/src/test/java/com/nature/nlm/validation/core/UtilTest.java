package com.nature.nlm.validation.core;

import java.io.File;

import org.junit.Test;

import com.nature.nlm.validation.model.file.FileDetails;
import com.nature.nlm.validation.model.file.FileType;
import com.nature.nlm.validation.model.report.Message;
import com.nature.nlm.validation.model.report.Report;
import com.nature.nlm.validation.model.report.Reports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author a.khettar
 * 
 */
public class UtilTest {

    /**
     * Test method for {@link com.nature.nlm.validation.core.Util#buildErrorReport(java.lang.Throwable)} .
     */
    @Test
    public void testBuildErrorMessage() {

        Exception e = new Exception("some message");
        Report report = Util.buildErrorReport(e);
        assertEquals("Failure: some message", report.getMessage().get(0).getContent());

    }

    /**
     * Test method for {@link com.nature.nlm.validation.core.Util#getSchematronTransformMD5()}.
     */
    @Test
    public void testGetSchematronTransformMD5() {

        assertNotNull(Util.getSchematronTransformMD5());
    }

    /**
     * Test method for {@link com.nature.nlm.validation.core.Util#getSchematronTransformMD5()}.
     */
    @Test
    public void testMarshalToString() throws Exception {

        final Report report = new Report();
        final String xml = Util.marshalToString(report);
        assertNotNull(xml);
    }

    /**
     * Test method for {@link
     * com.nature.nlm.core.util.createTempFile(FileType.XML)}.
     * 
     * @throws Exception
     */
    @Test
    public void testcreateTempFileXML() throws Exception {
        FileDetails detail = new FileDetails();
        detail.setType(FileType.XML);
        File file = Util.createTempFile(detail);
        assertNull(file);
    }

    /**
     * Test method for {@link
     * com.nature.nlm.core.util.createTempFile(FileType.ZIP)}.
     * 
     * @throws Exception
     */
    @Test
    public void testcreateTempFileZIP() throws Exception {
        FileDetails detail = new FileDetails();
        detail.setType(FileType.ZIP);
        File file = Util.createTempFile(detail);
        assertNotNull(file);
    }

    /**
     * Test method for {@link com.nature.nlm.core.util.setFileDetails()}.
     * 
     * @throws Exception
     */
    @Test
    public void testSetFileDetails() throws Exception {

        Report report = new Report();
        FileDetails details = new FileDetails();
        details.setName("dummy.xml");
        Util.setFileDetails(report, details);
        assertEquals("dummy.xml", report.getHref());
    }

    @Test
    public void testNotXML() {
        final String name1 = "dummy.xml";
        final String name2 = "dummy.pdf";
        final String name3 = "a.png";
        assertFalse(Util.notXML(name1));
        assertTrue(Util.notXML(name2));
        assertTrue(Util.notXML(name3));

    }

    @Test
    public void testValidationSuccessful() {

        Report report = new Report();
        Message message = new Message();
        message.setContent(Constants.VALIDATION_SUCCESSFUL);
        report.setStatus(Constants.SUCCESS);
        report.getMessage().add(message);
        assertTrue(Util.validationSuccessful(report));
    }

    @Test
    public void testValidationNotSuccessful() {

        Report report = new Report();
        report.setHref("nlm-valid.xml");
        Message message = new Message();
        message.setContent("failed validation");
        message.setValidatedBy("dtd");
        message.setType("error");
        report.setStatus(Constants.FAILURE);
        report.getMessage().add(message);
        assertFalse(Util.validationSuccessful(report));

    }

    @Test
    public void testBuildErrorReports() {
        Exception e = new Exception("failed");
        Util.buildErrorReports(e);
    }

    @Test
    public void testSetReportsStatusShouldBeNully() {
        Reports reports = new Reports();
        Util.setReportsStatus(reports);
        assertNull(reports.getStatus());
    }

    @Test
    public void testSetReportsStatusShouldBeFailurePartial() {
        Reports reports = new Reports();
        Report report1 = new Report();
        report1.setStatus(Constants.SUCCESS);
        Report report2 = new Report();
        report2.setStatus(Constants.FAILURE);
        reports.getReport().add(report1);
        reports.getReport().add(report2);
        Util.setReportsStatus(reports);
        assertEquals(Constants.FAILURE_PARTIAL, reports.getStatus());
    }

    @Test
    public void testSetReportsStatusShouldBeFailure() {
        Reports reports = new Reports();
        Report report1 = new Report();
        report1.setStatus(Constants.FAILURE);
        Report report2 = new Report();
        report2.setStatus(Constants.FAILURE);
        reports.getReport().add(report1);
        reports.getReport().add(report2);
        Util.setReportsStatus(reports);
        assertEquals(Constants.FAILURE, reports.getStatus());
    }

    @Test
    public void testSetReportsStatusShouldBeSucess() {
        Reports reports = new Reports();
        Report report1 = new Report();
        report1.setStatus(Constants.SUCCESS);
        Report report2 = new Report();
        report2.setStatus(Constants.SUCCESS);
        reports.getReport().add(report1);
        reports.getReport().add(report2);
        Util.setReportsStatus(reports);
        assertEquals(Constants.SUCCESS, reports.getStatus());
    }

    @Test
    public void testSetReportsStatusThrowAssertionError() {

        try {
            Util.setReportsStatus(null);
            fail("should have thrown an exception");
        } catch (RuntimeException e) {

        }

    }
}
