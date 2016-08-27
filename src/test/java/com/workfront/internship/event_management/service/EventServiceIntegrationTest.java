package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.TestCase.assertNotNull;

/**
 * Created by Hermine Turshujyan 8/26/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestServiceConfiguration.class)
public class EventServiceIntegrationTest {

    @Autowired
    private EventService eventService;

    private Event testEvent;
    private User testUser;


    @Before
    public void setUp() throws DAOException, DuplicateEntryException {
        testEvent = TestObjectCreator.createTestEvent();

        //create test user
        testUser = TestObjectCreator.createTestUser();
    }

    @After
    public void tearDown() throws DAOException {
        //delete inserted test users from db
        eventService.deleteEvent(testEvent.getId());

        //delete test user object
        testEvent = null;
    }

    @Test
    public void addAccount_Success() {
        // EmailService emailService = Mockito.mock(EmailServiceImpl.class);
        // Whitebox.setInternalState(userService, "emailService", emailService);
        //  when(emailService.sendVerificationEmail(testUser)).thenReturn(true);

        eventService.createEvent(testEvent);

        Event actualEvent = eventService.getEventById(testEvent.getId());
        assertNotNull(actualEvent);
    }
}
