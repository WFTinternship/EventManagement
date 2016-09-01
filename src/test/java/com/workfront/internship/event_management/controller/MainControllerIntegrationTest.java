package com.workfront.internship.event_management.controller;

import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
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
import static com.workfront.internship.event_management.controller.util.PageParameters.HOME_VIEW;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Hermine Turshujyan 8/31/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class MainControllerIntegrationTest {

    @Autowired
    private MainController mainController;
    @Autowired
    private EventService eventService;
    @Autowired
    private CategoryService categoryService;

    private Model testModel;
    private Event testEvent;

    @Before
    public void setUp() {
        testModel = new ExtendedModelMap();

        //create test objects, insert into db
        Category testCategory = createTestCategory();
        testCategory = categoryService.addCategory(testCategory);

        testEvent = createTestEvent();
        testEvent.setCategory(testCategory);
        eventService.createEvent(testEvent);
    }

    @After
    public void tearDown() {
        testModel = null;
        testEvent = null;

        categoryService.deleteAllCategories();
        eventService.deleteAllEvents();
    }

    @Test
    public void loadUpcomingEvents() {

        List<Event> testEventList = new ArrayList<>();
        testEventList.add(testEvent);

        //method under test
        String pageView = mainController.loadUpcomingEvents(testModel);

        List eventList = (ArrayList) testModel.asMap().get("events");
        assertNotNull(eventList);
        assertFalse(eventList.isEmpty());
        assertEquals(eventList.size(), 1);
        assertEquals(pageView, HOME_VIEW);
    }
}
