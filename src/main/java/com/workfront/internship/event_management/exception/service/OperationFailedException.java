package com.workfront.internship.event_management.exception.service;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public class OperationFailedException extends RuntimeException {

    public OperationFailedException(String message) {
        super(message);
    }

    public OperationFailedException(String message, Exception e) {
        super(message, e);
    }

}
