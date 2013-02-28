package com.nature.nlm.validation.core.schema;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.nature.nlm.validation.core.exception.ValidationException;
import com.nature.nlm.validation.model.report.Report;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

/**
 * Unit test for {@link SchemaValidator}.
 * 
 * @author a.khettar
 * 
 */
public class SchemaValidatorTest {

    @Mock
    NLMErrorHandler handler;
    @Mock
    XMLReader reader;
    @Mock
    Report report;
    @Mock
    EntityResolver resolver;

    SchemaValidator validator;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUpBefore() throws Exception {
        MockitoAnnotations.initMocks(this);
        validator = new SchemaValidator(reader, handler);
    }

    /**
     * Test method for {@link com.nature.nlm.validation.core.schema.SchemaValidator#validate(java.lang.String)} .
     */
    @Test
    public void testValidateIOException() throws Exception {

        String nlm = "some xml";
        try {
            InputStream in = IOUtils.toInputStream(nlm);
            doThrow(new IOException()).when(reader).parse(any(InputSource.class));
            validator.validate(eq(in));
            fail("Failed to throw IOException");
        } catch (ValidationException e) {
            // do nothing.
        }

    }

    /**
     * Test method for {@link com.nature.nlm.validation.core.schema.SchemaValidator#validate(java.lang.String)} .
     */
    @Test
    public void testValidate() throws Exception {

        String nlm = "xml";
        InputStream in = IOUtils.toInputStream(nlm);
        doNothing().when(reader).parse(new InputSource(in));
        final Report report = validator.validate(in);
        assertEquals("VALIDATION SUCCESSFUL", report.getMessage().get(0).getContent());

    }
}
