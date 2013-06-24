package com.akhettar.validation.service.modules;

import com.akhettar.validation.core.modules.ValidationServiceModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;

/**
 * @author a.khettar
 * 
 */
public class NLMServiceGuiceConfiguration extends GuiceServletContextListener {

    /*
     * (non-Javadoc)
     * 
     * @see com.google.inject.servlet.GuiceServletContextListener#getInjector()
     */
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(Stage.PRODUCTION, new ValidationServiceModule(), new NLMServletModule());

    }

}
