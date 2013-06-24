package com.akhettar.validation.core.exception;

/**
 * 
 * Guice configuration exception.
 * 
 * @author a.khettar
 * 
 */
public class ModulesConfigurationException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 7299682301058270889L;

    /**
     * Public constructor with message parameter.
     * 
     * @param message
     *            given message.
     */
    public ModulesConfigurationException(String message) {
        super(message);
    }

    /**
     * Public constructor with message and exception instance.
     * 
     * @param message
     *            given message.
     * @param e
     *            given throwable.
     */
    public ModulesConfigurationException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * Public constructor with throwable parameter
     * 
     * @param e
     *            given throwable.
     */
    public ModulesConfigurationException(Throwable e) {
        super(e);
    }

}
