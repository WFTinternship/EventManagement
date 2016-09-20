package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.controller.util.CustomResponse;
import com.workfront.internship.event_management.controller.util.TestHttpServletRequest;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.User;
import com.workfront.internship.event_management.service.CategoryService;
import com.workfront.internship.event_management.service.EventService;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static com.workfront.internship.event_management.TestObjectCreator.createTestCategory;
import static com.workfront.internship.event_management.TestObjectCreator.createTestEvent;
import static com.workfront.internship.event_management.controller.util.PageParameters.*;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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

    private Model testModel;
    private TestHttpServletRequest testRequest;
    private Event testEvent;
    private Category testCategory;


    @Before
    public void setUp() {
        testRequest = new TestHttpServletRequest();
        testModel = new ExtendedModelMap();

        //create test objects, insert into db
        testCategory = createTestCategory();
        categoryService.addCategory(testCategory);

        testEvent = createTestEvent();
        testEvent.setCategory(testCategory);
        eventService.createEvent(testEvent);
    }

    @After
    public void tearDown() {
        testRequest = null;
        testModel = null;
        testEvent = null;
        testCategory = null;

        //delete test records from db
        categoryService.deleteAllCategories();
        eventService.deleteAllEvents();
    }

    @Test
    public void loadAllEventsAndCategories() {

        List<Event> testEventList = new ArrayList<>();
        testEventList.add(testEvent);

        List<Category> testCategoryList = new ArrayList<>();
        testCategoryList.add(testCategory);

        //method under test
        String pageView = eventController.loadAllEventsAndCategories(testRequest, testModel);

        List eventList = (ArrayList) testModel.asMap().get("events");
        List categoryList = (ArrayList) testModel.asMap().get("categories");

        //assertions
        assertNotNull("Event list is null", eventList);
        assertFalse("Event list is empty", eventList.isEmpty());
        assertEquals("Failed to load events", eventList.size(), 1);

        assertNotNull("Category list is null", categoryList);
        assertFalse("Category list is empty", categoryList.isEmpty());
        assertEquals("Failed to load categories", categoryList.size(), 1);

        assertEquals("Invalid redirect page", pageView, EVENTS_LIST_VIEW);
    }

    @Test
    public void getEvent() {
        //method under test
        String pageName = eventController.getEventDetails(testEvent.getId(), testModel);

        Event event = (Event) testModel.asMap().get("event");

        assertNotNull("Event not found", event);
        assertEquals("Wrong event id", event.getId(), testEvent.getId());
        assertEquals("Invalid redirect page", pageName, EVENT_DETAILS_VIEW);
    }

    @Test
    public void loadEvents_By_Category() {
        List<Event> testEventList = new ArrayList<>();
        testEventList.add(testEvent);

        testRequest.setAttribute("categoryId", testCategory.getId());

        //method under test
        CustomResponse response = eventController.loadEventsByCategory(testRequest);
        List<Event> eventList = (List<Event>) response.getResult();

        //assertions
        assertEquals("Status is incorrect", response.getStatus(), ACTION_SUCCESS);

        assertNotNull("Event list is null", eventList);
        assertFalse("Event list is empty", eventList.isEmpty());
        assertEquals("Incorrect event list size", eventList.size(), 1);
        assertEquals("Fail to load all events", eventList.get(0).getId(), testEvent.getId());

    }

    @Test
    public void goToCreateEventPage_Success() {
        testRequest.getSession().setAttribute("user", new User());

        //method under test
        String pageName = eventController.goToCreateEventPage(testRequest, testModel);

        Event emptyEvent = (Event) testModel.asMap().get("event");
        List<Category> categoryList = (List<Category>) testModel.asMap().get("categories");

        assertNotNull("Empty event not created", emptyEvent);
        assertNotNull("Category list is null", categoryList);
        assertEquals("Incorrect category list size", categoryList.size(), 1);
        assertEquals("Fail to load all categories", categoryList.get(0).getId(), testCategory.getId());
        assertEquals("Invalid redirect page", pageName, EVENT_EDIT_VIEW);
    }

}
