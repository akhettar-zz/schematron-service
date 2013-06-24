/**
 * 
 */
package com.akhettar.validation.service.modules;

import org.junit.Test;

import com.akhettar.validation.service.modules.NLMServiceGuiceConfiguration;
import com.akhettar.validation.service.resource.RestfulValidationServiceImp;
import com.google.inject.Injector;

import static org.junit.Assert.assertNotNull;

/**
 * @author a.khettar
 * 
 */
public class NLMServiceGuiceConfigurationTest {

    @Test
    public void testConfigure() throws Exception {

        NLMServiceGuiceConfiguration config = new NLMServiceGuiceConfiguration();
        Injector inj = config.getInjector();
        assertNotNull(inj);
        RestfulValidationServiceImp service = inj.getInstance(RestfulValidationServiceImp.class);
        assertNotNull(service);

    }

}
