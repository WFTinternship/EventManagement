package com.workfront.internship.event_management.exception;

/**
 * Created by Hermine Turshujyan 7/28/16.
 */
//@ResponseStatus(value = HttpStatus.NOT_FOUND, reason="No such object")
public class ObjectNotFoundException extends RuntimeException {

   /* public ObjectNotFoundException( ) {
    }

    public ObjectNotFoundException(Exception e) {
        super(e);
    }*/

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
