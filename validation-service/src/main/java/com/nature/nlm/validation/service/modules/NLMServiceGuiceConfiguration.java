package com.nature.nlm.validation.service.modules;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;
import com.nature.nlm.validation.core.modules.ValidationServiceModule;

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
