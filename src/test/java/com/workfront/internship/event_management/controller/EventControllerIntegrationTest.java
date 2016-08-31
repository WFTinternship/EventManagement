package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.service.EventService;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Hermine Turshujyan 8/30/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class EventControllerIntegrationTest {

    @Autowired
    private EventService eventService;

    private HttpServletRequest testRequest;

}
