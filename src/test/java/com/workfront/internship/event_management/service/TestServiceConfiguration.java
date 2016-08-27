package com.workfront.internship.event_management.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by Hermine Turshujyan 8/25/16.
 */

@Configuration
@Profile("test")
@ComponentScan(basePackages = "com.workfront.internship.event_management")
public class TestServiceConfiguration {

    @Bean
    public UserService userService() {
        UserService userService = new UserServiceImpl();
        return userService;
    }

}