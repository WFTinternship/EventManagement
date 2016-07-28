package com.workfront.internship.event_management.exception.dao;

/**
 * Created by Hermine Turshujyan 7/28/16.
 */
public class ObjectNotFoundException extends Exception {

   /* public ObjectNotFoundException( ) {
    }

    public ObjectNotFoundException(Exception e) {
        super(e);
    }*/

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
