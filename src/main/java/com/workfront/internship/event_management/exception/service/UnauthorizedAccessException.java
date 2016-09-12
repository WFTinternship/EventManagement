package com.workfront.internship.event_management.exception.service;

/**
 * Created by Hermine Turshujyan 9/12/16.
 */
public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public UnauthorizedAccessException(String message, Exception e) {
        super(message, e);
    }
}
