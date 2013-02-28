package com.nature.nlm.validation.core.xmldiff;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.nature.nlm.validation.core.Constants;
import com.nature.nlm.validation.core.exception.ValidationException;
import com.nature.nlm.validation.core.modules.ValidationServiceModule;
import com.nature.nlm.validation.model.report.Report;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: a.khettar
 * Date: 16/07/2012
 * Time: 18:00
 * To change this template use File | Settings | File Templates.
 */
public class XMLDiffValidatorTest extends TestCase {

    private XMLDiffValidator validator;

    @Override
    @Before
    public void setUp() throws Exception {

        Injector inj = Guice.createInjector(new ValidationServiceModule());
        validator = inj.getInstance(XMLDiffValidator.class);
    }

    /**
     * test that the articles are similar
     */

    @Test
    public void testSimilar() throws Exception {

        final InputStream xml = ClassLoader.getSystemResourceAsStream("xmldiff/similar.xml");

        final Report report = validator.applyDiff(xml);

        assertEquals("Similar article", report.getMessage().get(0).getContent());
        assertEquals(Constants.SUCCESS, report.getStatus());

    }

    /**
     * test that the articles are different
     */

    @Test
    public void testDifferent() throws Exception {

        final InputStream xml = ClassLoader.getSystemResourceAsStream("xmldiff/different-volum-not-present.xml");

        final Report report = validator.applyDiff(xml);

        assertEquals(Constants.FAILURE, report.getStatus());


    }

    @Test
    public void testThrowUnmarshallExcaption() throws Exception
    {
        final InputStream xml = ClassLoader.getSystemResourceAsStream("xmldiff/not-correct-xml.xml");
        try {
            validator.applyDiff(xml);
            fail("should have thrown an exception");
        }catch (ValidationException e)
        {
            assertEquals("java.lang.IllegalArgumentException: The input XML must contain only two well formatted XMLs to apply the diff", e.getLocalizedMessage());
        }
    }

}
