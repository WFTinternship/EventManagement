package com.workfront.internship.event_management.spring;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Created by Hermine Turshujyan 8/19/16.
 */
@ControllerAdvice
public class AppBindingInitializer {

//    @InitBinder
//    public void initBinder (WebDataBinder binder ) {
//        StringTrimmerEditor stringTrimmer = new StringTrimmerEditor(true);
//        binder.registerCustomEditor(String.class, stringTrimmer);
//    }
}