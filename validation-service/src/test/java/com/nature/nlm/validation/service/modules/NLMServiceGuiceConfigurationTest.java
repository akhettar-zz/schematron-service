/**
 * 
 */
package com.nature.nlm.validation.service.modules;

import org.junit.Test;

import com.google.inject.Injector;
import com.nature.nlm.validation.service.resource.RestfulValidationServiceImp;

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
