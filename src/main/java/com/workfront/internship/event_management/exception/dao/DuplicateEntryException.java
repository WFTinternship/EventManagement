package com.workfront.internship.event_management.exception.dao;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public class DuplicateEntryException extends RuntimeException {

    public DuplicateEntryException(String message, Exception e) {
        super(message, e);
    }

    public DuplicateEntryException(String message) {
        super(message);
    }
}
