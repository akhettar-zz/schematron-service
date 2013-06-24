package com.akhettar.validation.service.modules;

import com.akhettar.validation.service.resource.RestfulValidationServiceImp;
import com.akhettar.validation.service.resource.RestfulXMLDiffServiceImpl;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * 
 * Servlet Module configuration
 * 
 * @author a.khettar
 * 
 */
public class NLMServletModule extends JerseyServletModule {

    @Override
    protected void configureServlets() {

        bind(RestfulValidationServiceImp.class);
        bind(RestfulXMLDiffServiceImpl.class);
        serve("/rest/*").with(GuiceContainer.class);

    }

}
