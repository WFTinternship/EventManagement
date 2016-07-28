package com.workfront.internship.event_management.exception.service;

/**
 * Created by Hermine Turshujyan 7/28/16.
 */
public class InvalidObjectException extends RuntimeException {

    public InvalidObjectException(String message) {
        super(message);
    }

    /*public InvalidObjectException(String message, Exception e) {
        super(message, e);
    }*/
}
