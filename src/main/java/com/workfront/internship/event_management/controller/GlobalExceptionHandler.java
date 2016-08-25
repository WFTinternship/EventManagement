package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.service.CategoryServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Hermine Turshujyan 8/25/16.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(CategoryServiceImpl.class);
    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(Throwable.class)
    public String handleOtherException(HttpServletRequest request, DAOException e) {
        logger.info("Exception Occurred:: URL=" + request.getRequestURL());
        return DEFAULT_ERROR_VIEW;
    }

    @ExceptionHandler(DAOException.class)
    public ModelAndView handleSQLException(HttpServletRequest request, DAOException e) {
        logger.info("DAOException Occurred:: URL=" + request.getRequestURL());

        ModelAndView mov = new ModelAndView(DEFAULT_ERROR_VIEW);
        mov.addObject("message", "Database error");

        return mov;
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ModelAndView handleObjectNotFoundException(HttpServletRequest request, ObjectNotFoundException e) {
        logger.info("ObjectNotFoundException Occurred:: URL=" + request.getRequestURL());

        ModelAndView mov = new ModelAndView(DEFAULT_ERROR_VIEW);
        mov.addObject("message", e.getMessage());

        return mov;
    }
}
