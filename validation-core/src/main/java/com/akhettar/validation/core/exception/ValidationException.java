/**
 * 
 */
package com.akhettar.validation.core.exception;

/**
 * 
 * Wrapper for Validation Exception.
 * 
 * @author a.khettar
 * 
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -1410060665575894883L;

    /**
     * Public constructor with message parameter.
     * 
     * @param message
     *            given message.
     */
    public ValidationException(String message) {
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
    public ValidationException(String message, Throwable e) {
        super(message, e);
    }

    /**
     * Public constructor with throwable parameter
     * 
     * @param e
     *            given throwable.
     */
    public ValidationException(Throwable e) {
        super(e);
    }

}
