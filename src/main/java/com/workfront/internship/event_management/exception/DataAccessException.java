package com.workfront.internship.event_management.exception;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public class DataAccessException extends OperationFailedException {
    public DataAccessException() {
    }

    //Constructor that accepts a message
    public DataAccessException(String message) {
        super(message);
    }

}
