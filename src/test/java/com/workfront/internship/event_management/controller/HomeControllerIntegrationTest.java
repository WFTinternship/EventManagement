package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.TestHttpServletRequest;
import com.workfront.internship.event_management.controller.util.TestHttpSession;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.CategoryService;
import com.workfront.internship.event_management.service.EventService;
import com.workfront.internship.event_management.service.UserService;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static com.workfront.internship.event_management.TestObjectCreator.createTestCategory;
import static com.workfront.internship.event_management.TestObjectCreator.createTestEvent;
import static com.workfront.internship.event_management.TestObjectCreator.createTestUser;
import static com.workfront.internship.event_management.controller.util.CongrollerConstants.HOME_VIEW;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by Hermine Turshujyan 8/31/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class HomeControllerIntegrationTest {

    @Autowired
    private HomeController homeController;
    @Autowired
    private EventService eventService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    private TestHttpServletRequest testRequest;
    private TestHttpSession testSession;
    private Event testEvent;
    private User testUser;
    private Category testCategory;


    @Before
    public void setUp() {
        //create test objects, insert into db
        testUser = createTestUser();
        testUser = userService.addAccount(testUser);

        testCategory = createTestCategory();
        testCategory = categoryService.addCategory(testCategory);

        testEvent = createTestEvent()
                .setCategory(testCategory)
                .setOrganizer(testUser);
        eventService.createEvent(testEvent);

        testRequest = spy(TestHttpServletRequest.class);
        testSession = spy(TestHttpSession.class);
    }

    @After
    public void tearDown() {
        testEvent = null;
        testCategory = null;
        testUser = null;

        eventService.deleteAllEvents();
        userService.deleteAllUsers();
        categoryService.deleteAllCategories();
    }

    @Test
    public void loadUpcomingEvents_PublicOnly() {
        List<Event> upcomingEvents = new ArrayList<>();
        upcomingEvents.add(testEvent);

        //method under test
        String pageView = homeController.loadUpcomingEvents(testRequest);
        List<Event> actualEventList = (List<Event>) testRequest.getAttribute("events");

        assertEquals("Invalid redirect page", pageView, HOME_VIEW);
        assertNotNull("Event list is null", upcomingEvents);
        assertFalse("Event list is empty", upcomingEvents.isEmpty());
        assertEquals("Fail to load public upcoming events", upcomingEvents.size(), 1);
    }

    @Test
    public void loadUpcomingEvents_All() {
        testSession.setAttribute("user", testUser);

        List<Event> upcomingEvents = new ArrayList<>();
        upcomingEvents.add(testEvent);

        //method under test
        String pageView = homeController.loadUpcomingEvents(testRequest);
        List<Event> actualEventList = (List<Event>) testRequest.getAttribute("events");

        assertEquals("Invalid redirect page", pageView, HOME_VIEW);
        assertNotNull("Event list is null", upcomingEvents);
        assertFalse("Event list is empty", upcomingEvents.isEmpty());
        assertEquals("Fail to load all upcoming events", upcomingEvents.size(), 1);
    }
}
