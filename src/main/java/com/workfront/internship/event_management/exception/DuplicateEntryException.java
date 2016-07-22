package com.workfront.internship.event_management.exception;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public class DuplicateEntryException extends OperationFailedException {

    public DuplicateEntryException() {
    }

    //Constructor that accepts a message
    public DuplicateEntryException(String message) {
        super(message);
    }


}
