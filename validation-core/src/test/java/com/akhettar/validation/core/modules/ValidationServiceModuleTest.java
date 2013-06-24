/**
 * 
 */
package com.akhettar.validation.core.modules;

import com.akhettar.validation.core.modules.ValidationServiceModule;
import com.akhettar.validation.core.schematron.NLMUriResolver;
import com.akhettar.validation.core.services.XMLDiffService;
import com.google.inject.Guice;
import com.google.inject.Injector;
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
