package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.UnauthorizedAccessException;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static com.workfront.internship.event_management.controller.util.CongrollerConstants.DEFAULT_ERROR_VIEW;

/**
 * Created by Hermine Turshujyan 8/25/16.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    public String handleOtherException(HttpServletRequest request) {
        logger.info("Exception Occurred:: URL=" + request.getRequestURL());
        return DEFAULT_ERROR_VIEW;
    }

    @ExceptionHandler(DAOException.class)
    public ModelAndView handleDAOException(HttpServletRequest request) {
        logger.info("DAOException Occurred:: URL=" + request.getRequestURL());

        ModelAndView mov = new ModelAndView(DEFAULT_ERROR_VIEW);
        mov.addObject("message", "Database error!");

        return mov;
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ModelAndView handleObjectNotFoundException(HttpServletRequest request, ObjectNotFoundException e) {
        logger.info("ObjectNotFoundException Occurred:: URL=" + request.getRequestURL());

        ModelAndView mov = new ModelAndView(DEFAULT_ERROR_VIEW);
        mov.addObject("message", e.getMessage());

        return mov;
    }

    @ExceptionHandler(InvalidObjectException.class)
    public ModelAndView handleInvalidObjectException(HttpServletRequest request, InvalidObjectException e) {
        logger.info("InvalidObjectException Occurred:: URL=" + request.getRequestURL());

        ModelAndView mov = new ModelAndView(DEFAULT_ERROR_VIEW);
        mov.addObject("message", e.getMessage());

        return mov;
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ModelAndView handleUnauthorizedAccessException(HttpServletRequest request, UnauthorizedAccessException e) {
        logger.info("UnauthorizedAccessException Occurred:: URL=" + request.getRequestURL());

        ModelAndView mov = new ModelAndView(DEFAULT_ERROR_VIEW);
        mov.addObject("message", e.getMessage());
        mov.addObject("additionalErrorInfo", "Please login to perform this action!");

        return mov;
    }
}
