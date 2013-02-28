/**
 * 
 */
package com.nature.nlm.validation.core.modules;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.nature.nlm.validation.core.schematron.NLMUriResolver;
import com.nature.nlm.validation.core.services.XMLDiffService;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * @author a.khettar
 * 
 */
public class ValidationServiceModuleTest {

    @Test
    public void testSuccess() {

        Injector inj = Guice.createInjector(new ValidationServiceModule());
        final NLMUriResolver resolver = inj.getInstance(NLMUriResolver.class);
        final XMLDiffService service = inj.getInstance(XMLDiffService.class);
        assertNotNull(resolver);
    }

}
