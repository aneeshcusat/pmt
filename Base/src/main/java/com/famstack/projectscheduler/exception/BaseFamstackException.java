package com.famstack.projectscheduler.exception;

/**
 * The Class famstackException.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
public class BaseFamstackException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1711647094707136020L;

    /**
     * Instantiates a new delivery interface exception.
     *
     * @param message the message
     * @param exception the exception
     */
    public BaseFamstackException(String message, Throwable exception) {
        super(message, exception);
    }
}
