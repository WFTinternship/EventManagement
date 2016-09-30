package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.common.DateParser;
import com.workfront.internship.event_management.controller.util.CustomResponse;
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.workfront.internship.event_management.TestObjectCreator.*;
import static com.workfront.internship.event_management.TestObjectCreator.VALID_IMAGE_NAME;
import static com.workfront.internship.event_management.TestObjectCreator.WEB_CONTENT_ROOT;
import static com.workfront.internship.event_management.controller.util.CongrollerConstants.*;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by Hermine Turshujyan 8/30/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class EventControllerIntegrationTest {

    @Autowired
    private EventController eventController;
    @Autowired
    private EventService eventService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;

    private TestHttpServletRequest testRequest;
    private TestHttpSession testSession;
    private Event testEvent;
    private Category testCategory;
    private User testUser;

    @Before
    public void setUp() {
        testRequest = new TestHttpServletRequest();
        testSession = new TestHttpSession();

        //create test objects, insert into db
        testUser = createTestUser();
        userService.addAccount(testUser);

        testCategory = createTestCategory();
        categoryService.addCategory(testCategory);

        testEvent = createTestEvent()
                .setCategory(testCategory)
                .setOrganizer(testUser);
        eventService.createEvent(testEvent);
    }

    @After
    public void tearDown() {
        //delete test records from db
        eventService.deleteAllEvents();
        categoryService.deleteAllCategories();
        userService.deleteAllUsers();
    }

    @Test
    public void loadAllEventsAndCategories_Public() {
        //method under test
        String pageView = eventController.loadEventsByCategory(testRequest);

        List<Event> eventList = (List<Event>) testRequest.getAttribute("events");
        List<Category> categoryList = (List<Category>) testRequest.getAttribute("categories");

        //assertions
        assertEquals("Invalid redirect page", pageView, EVENTS_LIST_VIEW);

        assertNotNull("Event list is null", eventList);
        assertFalse("Event list is empty", eventList.isEmpty());
        assertEquals("Failed to load events", eventList.size(), 1);
        assertEquals("Failed to load event", eventList.get(0).getId(), testEvent.getId());

        assertNotNull("Category list is null", categoryList);
        assertFalse("Category list is empty", categoryList.isEmpty());
        assertEquals("Failed to load categories", categoryList.size(), 1);
        assertEquals("Failed to load event", categoryList.get(0).getId(), testCategory.getId());
    }

    @Test
    public void loadPastEvents_Public() {

        //make test event past, insert into db
        testEvent.setEndDate(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        eventService.createEvent(testEvent);

        //method under test
        String pageView = eventController.loadPastEvents(testRequest);

        //assertions
        List eventList = (List<Event>) testRequest.getAttribute("events");
        String listHeader = (String) testRequest.getAttribute("listHeader");

        assertEquals("Invalid redirect page", pageView, EVENTS_LIST_VIEW);

        assertEquals("Invalid header for past events page", listHeader, PAST_EVENTS_HEADER);

        assertNotNull("Event list is null", eventList);
        assertFalse("Event list is empty", eventList.isEmpty());
        assertEquals("Failed to load events", eventList.size(), 1);
    }

    @Test
    public void loadUpcomingEvents_Public() {

        //testEvent is already upcoming
        //method under test
        String pageView = eventController.loadUpcomingEvents(testRequest);

        //assertions
        List eventList = (List<Event>) testRequest.getAttribute("events");
        String listHeader = (String) testRequest.getAttribute("listHeader");

        assertEquals("Invalid redirect page", pageView, EVENTS_LIST_VIEW);
        assertEquals("Invalid header for past events page", listHeader, UPCOMING_EVENTS_HEADER);

        assertNotNull("Event list is null", eventList);
        assertFalse("Event list is empty", eventList.isEmpty());
        assertEquals("Failed to load events", eventList.size(), 1);
    }

    @Test
    public void loadEventDetails_Success() {
        //method under test
        String pageView = eventController.loadEventDetails(testEvent.getId(), testRequest);

        Event actualEvent = (Event) testRequest.getAttribute("event");

        assertEquals("Invalid redirect page", pageView, EVENT_DETAILS_VIEW);
        assertNotNull("Event not found", actualEvent);
        assertEquals("Wrong event id", actualEvent.getId(), testEvent.getId());
    }

    @Test
    public void goToCreateEventPage_Success() {
        testRequest.getSession().setAttribute("user", new User());

        //method under test
        String pageName = eventController.goToCreateEventPage(testRequest);

        List<Category> categoryList = (List<Category>) testRequest.getAttribute("categories");
        String action = (String) testRequest.getAttribute("action");
        Event emptyEvent = (Event) testRequest.getAttribute("event");

        assertEquals("Invalid redirect page", EVENT_EDIT_VIEW, pageName);
        assertEquals("Invalid action", ACTION_CREATE_EVENT, action);

        assertNotNull("Category list is null", categoryList);
        assertFalse("Category list is empty", categoryList.isEmpty());
        assertEquals("Failed to load categories", categoryList.size(), 1);
        assertEquals("Failed to load event", categoryList.get(0).getId(), testCategory.getId());

        assertNotNull("Failed to create empty event object", emptyEvent);
    }


    @Test
    public void saveEvent_Create_Success() {
        //delete inserted event from db
        eventService.deleteAllEvents();

        testRequest.getSession().setAttribute("user", testUser);
        testRequest.setParameter("action", "create");

        testRequest.setParameter("eventTitle", testEvent.getTitle());
        testRequest.setParameter("shortDesc", testEvent.getShortDescription());
        testRequest.setParameter("fullDesc", testEvent.getFullDescription());

        testRequest.setParameter("categoryId", Integer.toString(testEvent.getCategory().getId()));
        testRequest.setParameter("eventId", "0");
        testRequest.setParameter("publicAccessed", "1");
        testRequest.setParameter("guestsAllowed", "1");
        testRequest.setParameter("startDate", DateParser.getDateStringFromDate(testEvent.getStartDate()));
        testRequest.setParameter("endDate", DateParser.getDateStringFromDate(testEvent.getEndDate()));
        testRequest.setParameter("startTime", DateParser.getTimeStringFromDate(testEvent.getStartDate()));
        testRequest.setParameter("endTime", DateParser.getTimeStringFromDate(testEvent.getEndDate()));
        testRequest.setParameter("categoryId", Integer.toString(testEvent.getCategory().getId()));
        testRequest.setParameter("location", testEvent.getLocation());
        testRequest.setParameter("lng", "1.1");
        testRequest.setParameter("lat", "1.1");

        //method under test
        CustomResponse response = eventController.saveEvent(testRequest, null, null);

        List<Event> userOrganizedEvents = eventService.getUserOrganizedEvents(testUser.getId());

        assertEquals("Create event failed", response.getStatus(), ACTION_SUCCESS);
        assertNotNull("User organized event list is null", userOrganizedEvents);
        assertFalse("User organized event list is empty", userOrganizedEvents.isEmpty());
    }

}
