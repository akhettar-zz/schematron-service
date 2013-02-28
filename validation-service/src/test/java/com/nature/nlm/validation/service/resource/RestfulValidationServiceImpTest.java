package com.nature.nlm.validation.service.resource;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nature.nlm.validation.core.exception.ValidationException;
import com.nature.nlm.validation.core.services.ValidationService;
import com.nature.nlm.validation.model.file.FileDetails;
import com.nature.nlm.validation.model.file.FileType;
import com.nature.nlm.validation.model.report.Report;
import com.nature.nlm.validation.model.report.Reports;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * @author a.khettar
 * 
 */
public class RestfulValidationServiceImpTest {

    @Mock
    ValidationService service;
    @Mock
    Report report;
    @Mock
    Reports reports;
    RestfulValidationServiceImp serviceImp;
    @Mock
    InputStream stream;
    @Mock
    FileDetails fileDetails;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUpBeforeClass() throws Exception {
        MockitoAnnotations.initMocks(this);
        serviceImp = spy(new RestfulValidationServiceImp(service));
    }

    /**
     * Test method for
     * {@link com.nature.nlm.validation.service.resource.RestfulValidationServiceImp#validate(java.lang.String)}.
     */
    @Test
    public void testValidateSuccessful() {

        String nlm = anyString();
        when(service.validate(IOUtils.toInputStream(nlm))).thenReturn(report);
        assertEquals(report, serviceImp.validate(nlm));

    }

    /**
     * Test method for
     * {@link com.nature.nlm.validation.service.resource.RestfulValidationServiceImp#validate(java.lang.String)}.
     */
    @Test
    public void testValidateFailurel() {

        String nlm = anyString();
        when(service.validate(IOUtils.toInputStream(nlm))).thenThrow(new ValidationException("dtd validation failure"));
        assertEquals("Failure: dtd validation failure", serviceImp.validate(nlm).getMessage().get(0).getContent());

    }

    /**
     * Test method for
     * {@link com.nature.nlm.validation.service.resource.RestfulValidationServiceImp#validate(java.io.InputStream)}.
     */
    @Test
    public void testValidateXMLFile() {

        when(service.validate(stream, FileType.XML)).thenReturn(reports);
        Reports reports = serviceImp.validateXMLFile(stream);
        assertNotNull(reports);

    }

    /**
     * Test method for
     * {@link com.nature.nlm.validation.service.resource.RestfulValidationServiceImp#validate(java.io.InputStream)}.
     */
    @Test
    public void testValidateZipFile() {

        when(service.validate(stream, FileType.ZIP)).thenReturn(reports);
        Reports reports = serviceImp.validateZipFile(stream);
        assertNotNull(reports);

    }

    /**
     * Test method for {@link
     * com.nature.nlm.service.validation.resource.RestfulValidationServiceImp#validateXMLFile(java.io.InputStream,
     * com.nature.nlm.core.model.FileDetails details)
     */
    @Test
    public void testValidateXMLFileFailure() {
        when(service.validate(eq(stream), any(FileType.class))).thenThrow(
                new ValidationException("dtd validation failure"));
        assertEquals("Failure: dtd validation failure", serviceImp.validateXMLFile(stream).getReport().get(0)
                .getMessage().get(0).getContent());

    }

    /**
     * Test method for {@link
     * com.nature.nlm.service.validation.resource.RestfulValidationServiceImp#validateXMLFile(java.io.InputStream,
     * com.nature.nlm.core.model.FileDetails details)
     */
    @Test
    public void testValidateZipFileFailure() {
        when(service.validate(eq(stream), any(FileType.class))).thenThrow(
                new ValidationException("dtd validation failure"));
        assertEquals("Failure: dtd validation failure", serviceImp.validateZipFile(stream).getReport().get(0)
                .getMessage().get(0).getContent());

    }

    /**
     * Test method for {@link
     * com.nature.nlm.service.validation.resource.RestfulValidationServiceImp#validateXMLFile(java.io.InputStream,
     * com.nature.nlm.core.model.FileDetails details)
     */
    @Test
    public void testValidateAnyFileFailure() {
        String fileName = "dummy.zip";
        when(service.validate(stream, fileName)).thenThrow(new ValidationException("dtd validation failure"));
        assertEquals("Failure: dtd validation failure", serviceImp.validateAnyFile(stream, fileName).getReport().get(0)
                .getMessage().get(0).getContent());

    }

    /**
     * Test method for {@link
     * com.nature.nlm.service.validation.resource.RestfulValidationServiceImp#validateXMLFile(java.io.InputStream,
     * com.nature.nlm.core.model.FileDetails details)
     */
    @Test
    public void testValidateAnyFile() {
        final String fileName = "dummy.xml";
        when(service.validate(stream, fileName)).thenReturn(reports);
        Reports reports = serviceImp.validateAnyFile(stream, fileName);
        assertNotNull(reports);

    }
}
