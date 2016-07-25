package com.workfront.internship.event_management.exception;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public class DAOException extends Exception {
    public DAOException() {
    }

    //Constructor that accepts a message
    public DAOException(String message) {
        super(message);
    }

}
