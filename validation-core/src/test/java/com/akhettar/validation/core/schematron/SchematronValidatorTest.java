package com.akhettar.validation.core.schematron;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xml.sax.XMLReader;

import com.akhettar.validation.core.Constants;
import com.akhettar.validation.core.exception.ValidationException;
import com.akhettar.validation.model.report.Report;

/**
 * 
 * Schematron validation test.
 * 
 * @author a.khettar
 * 
 */
public class SchematronValidatorTest {

    @Mock
    Unmarshaller unmarshaller;

    @Mock
    XMLReader reader;

    @Mock
    Report report;

    @Mock
    Templates schematrons;

    @Mock
    InputStream stream;

    @Mock
    Templates reportTemplates;

    SchematronValidator validator;

    @Mock
    URIResolver resolver;

    @Mock
    Transformer transformer;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUpBefore() throws Exception {

        MockitoAnnotations.initMocks(this);
        validator = spy(new SchematronValidator(schematrons, unmarshaller, reportTemplates, reader, resolver));
    }

    @Test
    public void testSuccessFulValidation() throws Exception {

        final InputStream in = this.getClass().getClassLoader().getResourceAsStream("valid-nlm.xml");
        when(schematrons.newTransformer()).thenReturn(transformer);
        when(reportTemplates.newTransformer()).thenReturn(transformer);

        Report report = validator.validate(in);
        assertEquals(Constants.SUCCESS, report.getStatus());
    }

    @Test
    public void testThrowTransformerException() throws Exception {
        final InputStream in = this.getClass().getClassLoader().getResourceAsStream("valid-nlm.xml");

        when(schematrons.newTransformer()).thenThrow(new TransformerConfigurationException("transformer exception"));
        try {
            validator.validate(in);
            fail("should have thrown Validation Exception");
        } catch (ValidationException e) {
            assertEquals("Failed to apply schematron validation transform", e.getMessage());
        }
    }
}
