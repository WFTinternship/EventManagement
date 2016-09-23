package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.workfront.internship.event_management.TestObjectCreator.*;
import static junit.framework.TestCase.assertNotNull;

/**
 * Created by Hermine Turshujyan 8/26/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class EventServiceIntegrationTest {

    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    CategoryService categoryService;

    private Event testEvent;
    private User testUser;
    private Category testCategory;

    @Before
    public void setUp() {
        //create test user
        testUser = createTestUser();
        testUser = userService.addAccount(testUser);

        //create test category
        testCategory = createTestCategory();
        categoryService.addCategory(testCategory);

        testEvent = createTestEvent();
        testEvent.setCategory(testCategory);
        eventService.createEvent(testEvent);
    }

    @After
    public void tearDown() {
        //delete inserted test events from db
        userService.deleteAllUsers();
        categoryService.deleteAllCategories();
        eventService.deleteAllEvents();

        //delete test user object
        testEvent = null;
    }

    @Test
    public void addEvent_Success() {
//         EmailService emailService = Mockito.mock(EmailServiceImpl.class);
//         Whitebox.setInternalState(userService, "emailService", emailService);
//          when(emailService.sendVerificationEmail(testUser)).thenReturn(true);

        Event actualEvent = eventService.getEventById(testEvent.getId());
        assertNotNull(actualEvent);
    }
}
