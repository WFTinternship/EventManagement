package com.workfront.internship.event_management.spring;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hermineturshujyan on 9/19/16.
 */
    public class GlobalBindingInitializer implements WebBindingInitializer {

        public void initBinder(WebDataBinder binder, WebRequest request) {
            binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("dd/MM/yyyy"), true));
        }

    }

