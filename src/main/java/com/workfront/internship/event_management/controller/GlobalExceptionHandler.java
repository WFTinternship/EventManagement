package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.exception.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.service.CategoryServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

/**
 * Created by Hermine Turshujyan 8/25/16.
 */

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = Logger.getLogger(CategoryServiceImpl.class);
    public static final String DEFAULT_ERROR_VIEW = "error";
    public static final String NOT_FOUND_ERROR_VIEW = "error404";

   /* @ExceptionHandler(Throwable.class)
    public ModelAndView handleOtherException(HttpServletRequest request, DAOException e){
        logger.info("Exception Occured:: URL=" + request.getRequestURL());

        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);
        mav.addObject("name", e.getClass().getSimpleName());
        mav.addObject("message", e.getMessage());

        return mav;
    }*/

    @ExceptionHandler({DAOException.class, SQLException.class})
    public String handleSQLException(HttpServletRequest request, DAOException e) {
        logger.info("SQLException Occurred:: URL=" + request.getRequestURL());
        return DEFAULT_ERROR_VIEW;
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleObjectNotFoundException(HttpServletRequest request, ObjectNotFoundException e) {
        logger.info("ObjectNotFoundException Occurred:: URL=" + request.getRequestURL());
        return NOT_FOUND_ERROR_VIEW;
    }
}
