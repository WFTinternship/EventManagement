package com.workfront.internship.event_management.exception;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public class OperationFailedException extends Exception {

    public OperationFailedException() {
    }

    //Constructor that accepts a message
    public OperationFailedException(String message) {
        super(message);
    }
}
