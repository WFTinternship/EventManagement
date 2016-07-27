package com.workfront.internship.event_management.exception;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public class OperationFailedException extends RuntimeException {

    public OperationFailedException() {
    }

    //Constructor that accepts a message
    public OperationFailedException(String message) {
        super(message);
    }

    //Constructor that accepts a message
    public OperationFailedException(String message, Exception e) {
        super(message);
    }

}
