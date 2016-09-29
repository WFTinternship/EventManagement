package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.*;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualRecurrences;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static junit.framework.TestCase.*;

/**
 * Created by Hermine Turshujyan 8/30/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class RecurrenceServiceIntegrationTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private RecurrenceTypeService recurrenceTypeService;
    @Autowired
    private RecurrenceOptionService recurrenceOptionService;

    private Category testCategory;
    private User testUser;
    private Event testEvent;
    private RecurrenceType testRecurrenceType;
    private RecurrenceOption testRecurrenceOption;
    private Recurrence testEventRecurrence;

    @Before
    public void setUp() {


        createTestObjects();
        insertTestObjectsIntoDB();
    }

    @After
    public void tearDown() {
        deleteTestRecordsFromDB();
        deleteTestObjects();
    }

    @Test
    public void addEventRecurrence() {
        //test category already inserted in setup, read record by categoryId
        Recurrence eventRecurrence = recurrenceService.getRecurrenceById(testEventRecurrence.getId());

        assertNotNull(eventRecurrence);
        assertEqualRecurrences(eventRecurrence, testEventRecurrence);
    }

    @Test
    public void getEventRecurrencesByEventId() {
        //test method
        List<Recurrence> eventRecurrenceList = recurrenceService.getRecurrencesByEventId(testEvent.getId());

        assertNotNull(eventRecurrenceList);
        assertEquals(eventRecurrenceList.size(), 1);
        assertEqualRecurrences(eventRecurrenceList.get(0), testEventRecurrence);
    }

    @Test
    public void getAllEventRecurrencesByEventId() {
        //test method
        List<Recurrence> eventRecurrenceList = recurrenceService.getAllRecurrences();

        assertNotNull(eventRecurrenceList);
        assertEquals(eventRecurrenceList.size(), 1);
        assertEqualRecurrences(eventRecurrenceList.get(0), testEventRecurrence);
    }

    @Test
    public void updateEventRecurrence() {
        //update recurrence type info
        testEventRecurrence.setRepeatInterval(7);

        //test method
        recurrenceService.editRecurrence(testEventRecurrence);

        //read updated record from db
        Recurrence eventRecurrence = recurrenceService.getRecurrenceById(testEventRecurrence.getId());

        assertNotNull(eventRecurrence);
        assertEqualRecurrences(eventRecurrence, testEventRecurrence);
    }


    @Test(expected = ObjectNotFoundException.class)
    public void deleteEventRecurrence() {
        //testing method
        boolean success = recurrenceService.deleteRecurrence(testEventRecurrence.getId());
        assertTrue(success);

        recurrenceService.getRecurrenceById(testEventRecurrence.getId());
    }


    @Test
    public void deleteAllEventRecurrences() {
        //testing method
        recurrenceService.deleteAllRecurrences();

        List<Recurrence> eventRecurrenceList = recurrenceService.getAllRecurrences();

        assertNotNull(eventRecurrenceList);
        assertTrue(eventRecurrenceList.isEmpty());
    }


    //helper methods
    private void createTestObjects() {
        testUser = createTestUser();
        testCategory = createTestCategory();
        testEvent = createTestEvent();
        testRecurrenceType = createTestRecurrenceType();
        testEventRecurrence = createTestRecurrence();
        testRecurrenceOption = createTestRecurrenceOption();
    }

    private void insertTestObjectsIntoDB() {
        //insert user into db and get generated id
        testUser = userService.addAccount(testUser);

        //insert category into db and get generated id
        testCategory = categoryService.addCategory(testCategory);

        //insert event into db and get generated id
        testEvent.setCategory(testCategory).setOrganizer(testUser);
        testEvent = eventService.createEvent(testEvent);

        //insert test recurrence type
        testRecurrenceType = recurrenceTypeService.addRecurrenceType(testRecurrenceType);

        //insert test recurrence option
        testRecurrenceOption.setRecurrenceTypeId(testRecurrenceType.getId());
        testRecurrenceOption = recurrenceOptionService.addRecurrenceOption(testRecurrenceOption);

        //insert event recurrence info into db
        testEventRecurrence.setEventId(testEvent.getId())
                .setRecurrenceType(testRecurrenceType)
                .setRecurrenceOption(testRecurrenceOption);

        testEventRecurrence = recurrenceService.addRecurrence(testEventRecurrence);
    }

    private void deleteTestObjects() {
        testEventRecurrence = null;
        testRecurrenceType = null;
        testEvent = null;
        testCategory = null;
    }

    private void deleteTestRecordsFromDB() {
        categoryService.deleteAllCategories();
        recurrenceTypeService.deleteAllRecurrenceTypes();
        recurrenceService.deleteAllRecurrences();
        eventService.deleteAllEvents();
    }
}
