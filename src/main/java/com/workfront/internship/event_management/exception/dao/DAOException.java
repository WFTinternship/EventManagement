package com.workfront.internship.event_management.exception.dao;

/**
 * Created by Hermine Turshujyan 7/22/16.
 */
public class DAOException extends Exception {

    public DAOException(Exception e) {
        super("Database error!", e);
    }

    public DAOException(String message, Exception e) {
        super(message, e);
    }
}
