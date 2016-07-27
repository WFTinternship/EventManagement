package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.TestHelper;
import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.model.*;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Hermine Turshujyan 7/13/16.
 */
public class EventRecurrenceDAOIntegrationTest {

    private static CategoryDAO categoryDAO;
    private static EventDAO eventDAO;
    private static EventRecurrenceDAO eventRecurrenceDAO;
    private static RecurrenceTypeDAO recurrenceTypeDAO;
    private static RecurrenceOptionDAO recurrenceOptionDAO;



    private Category testCategory;
    private Event testEvent;
    private RecurrenceType testRecurrenceType;
    private RecurrenceOption testRecurrenceOption;
    private EventRecurrence testEventRecurrence;

    @BeforeClass
    public static void setUpClass() throws DAOException {
        categoryDAO = new CategoryDAOImpl();
        eventDAO = new EventDAOImpl();
        recurrenceTypeDAO = new RecurrenceTypeDAOImpl();
        eventRecurrenceDAO = new EventRecurrenceDAOImpl();
        recurrenceOptionDAO = new RecurrenceOptionDAOImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        categoryDAO = null;
        eventDAO = null;
        recurrenceTypeDAO = null;
        eventRecurrenceDAO = null;
    }


    @Before
    public void setUp() throws DAOException {
        createTestObjects();
        insertTestObjectsIntoDB();
    }

    @After
    public void tearDown() throws DAOException {
        deleteTestRecordsFromDB();
        deleteTestObjects();
    }

    @Test
    public void addEventRecurrence_Success() {
        //test category already inserted in setup, read record by categoryId
        EventRecurrence eventRecurrence = eventRecurrenceDAO.getEventRecurrenceById(testEventRecurrence.getId());

        assertNotNull(eventRecurrence);
        assertEqualEventRecurrences(eventRecurrence, testEventRecurrence);
    }

    @Test(expected = RuntimeException.class)
    public void addEventRecurrence_Dublicate_Entry() {
        //test method
        eventRecurrenceDAO.addEventRecurrence(testEventRecurrence);
    }

    @Test
    public void getEventRecurrenceById_Found() {
        //test method
        EventRecurrence eventRecurrence = eventRecurrenceDAO.getEventRecurrenceById(testEventRecurrence.getId());

        assertNotNull(eventRecurrence);
        assertEqualEventRecurrences(eventRecurrence, testEventRecurrence);
    }

    @Test
    public void getEventRecurrenceById_Not_Found() {
        //test method
        EventRecurrence eventRecurrence = eventRecurrenceDAO.getEventRecurrenceById(TestHelper.NON_EXISTING_ID);

        assertNull(eventRecurrence);
    }

    @Test
    public void getEventRecurrencesByEventId_Found() {
        //test method
        List<EventRecurrence> eventRecurrenceList = eventRecurrenceDAO.getEventRecurrencesByEventId(testEvent.getId());

        assertNotNull(eventRecurrenceList);
        assertEquals(eventRecurrenceList.size(), 1);
        assertEqualEventRecurrences(eventRecurrenceList.get(0), testEventRecurrence);
    }

    @Test
    public void getEventRecurrencesByEventId__Empty_List() {
        //test method
        List<EventRecurrence> eventRecurrenceList = eventRecurrenceDAO.getEventRecurrencesByEventId(TestHelper.NON_EXISTING_ID);

        assertNotNull(eventRecurrenceList);
        assertTrue(eventRecurrenceList.isEmpty());
    }

    @Test
    public void getAllEventRecurrencesByEventId_Found() {
        //test method
        List<EventRecurrence> eventRecurrenceList = eventRecurrenceDAO.getAllEventRecurrences();

        assertNotNull(eventRecurrenceList);
        assertEquals(eventRecurrenceList.size(), 1);
        assertEqualEventRecurrences(eventRecurrenceList.get(0), testEventRecurrence);
    }

    @Test
    public void getAllEventRecurrencesByEventId__Empty_List() throws DAOException {
        //delete inserted test record
        eventRecurrenceDAO.deleteEventRecurrence(testEventRecurrence.getId());

        //test method
        List<EventRecurrence> eventRecurrenceList = eventRecurrenceDAO.getAllEventRecurrences();

        assertNotNull(eventRecurrenceList);
        assertTrue(eventRecurrenceList.isEmpty());
    }


    @Test
    public void updateEventRecurrence_Success() {
        //update recurrence type info
        testEventRecurrence.setRepeatInterval(7);

        //test method
        boolean updated = eventRecurrenceDAO.updateEventRecurrence(testEventRecurrence);

        //read updated record from db
        EventRecurrence eventRecurrence = eventRecurrenceDAO.getEventRecurrenceById(testEventRecurrence.getId());

        assertNotNull(eventRecurrence);
        assertEqualEventRecurrences(eventRecurrence, testEventRecurrence);
    }

    @Test
    public void updateEventRecurrence_Not_Found() {
        //create new event recurrence  object with no id
        EventRecurrence newEventRecurrence = TestHelper.createTestEventRecurrence();

        //test method
        boolean updated = eventRecurrenceDAO.updateEventRecurrence(newEventRecurrence);
        assertFalse(updated);
    }

    @Test
    public void deleteEventRecurrence_Found() throws DAOException {
        //testing method
        boolean deleted = eventRecurrenceDAO.deleteEventRecurrence(testEventRecurrence.getId());

        EventRecurrence eventRecurrence = eventRecurrenceDAO.getEventRecurrenceById(testEventRecurrence.getId());

        assertTrue(deleted);
        assertNull(eventRecurrence);
    }

    @Test
    public void deleteEventRecurrence_Not_Found() throws DAOException {
        //test method
        boolean deleted = eventRecurrenceDAO.deleteEventRecurrence(TestHelper.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteAllEventRecurrences_Found() throws DAOException {
        //testing method
        boolean deleted = eventRecurrenceDAO.deleteAllEventRecurrences();

        List<EventRecurrence> eventRecurrenceList = eventRecurrenceDAO.getAllEventRecurrences();

        assertNotNull(eventRecurrenceList);
        assertTrue(eventRecurrenceList.isEmpty());
        assertTrue(deleted);
    }

    @Test
    public void deleteAllEventRecurrences_Not_Found() throws DAOException {
        //delete inserted test record
        eventRecurrenceDAO.deleteEventRecurrence(testEventRecurrence.getId());

        //testing method
        boolean deleted = eventRecurrenceDAO.deleteAllEventRecurrences();

        assertFalse(deleted);
    }


    //helper methods
    private void createTestObjects() {
        testCategory = TestHelper.createTestCategory();
        testEvent = TestHelper.createTestEvent();
        testRecurrenceType = TestHelper.createTestRecurrenceType();
        testEventRecurrence = TestHelper.createTestEventRecurrence();
        testRecurrenceOption = TestHelper.createTestRecurrenceOption();
    }

    private void insertTestObjectsIntoDB() throws DAOException {
        //insert category into db and get generated id
        int categoryId = categoryDAO.addCategory(testCategory);
        testCategory.setId(categoryId);

        //insert event into db and get generated id
        testEvent.setCategory(testCategory);
        int eventId = eventDAO.addEvent(testEvent);
        testEvent.setId(eventId);

        //insert test recurrence type
        int recurrenceTypeId = recurrenceTypeDAO.addRecurrenceType(testRecurrenceType);
        testRecurrenceType.setId(recurrenceTypeId);

        //insert test recurrence option
        testRecurrenceOption.setRecurrenceTypeId(testRecurrenceType.getId());
        int recurrenceOptionId = recurrenceOptionDAO.addRecurrenceOption(testRecurrenceOption);
        testRecurrenceOption.setId(recurrenceOptionId);

        //insert event recurrence info into db
        testEventRecurrence.setEventId(testEvent.getId())
                .setRecurrenceType(testRecurrenceType)
                .setRecurrenceOptionId(recurrenceOptionId);
        int eventRecurrenceId = eventRecurrenceDAO.addEventRecurrence(testEventRecurrence);
        testEventRecurrence.setId(eventRecurrenceId);
    }

    private void deleteTestObjects() {
        testEventRecurrence = null;
        testRecurrenceType = null;
        testEvent = null;
        testCategory = null;
    }

    private void deleteTestRecordsFromDB() throws DAOException {
        categoryDAO.deleteAllCategories();
        recurrenceTypeDAO.deleteAllRecurrenceTypes();
        eventRecurrenceDAO.deleteAllEventRecurrences();
        eventDAO.deleteAllEvents();
    }

    private void assertEqualEventRecurrences(EventRecurrence actualEventRecurrence, EventRecurrence expectedEventRecurrence) {
        assertEquals(actualEventRecurrence.getEventId(), expectedEventRecurrence.getEventId());
        assertEquals(actualEventRecurrence.getRecurrenceType().getId(), expectedEventRecurrence.getRecurrenceType().getId());
        assertEquals(actualEventRecurrence.getRecurrenceOptionId(), expectedEventRecurrence.getRecurrenceOptionId());
        assertEquals(actualEventRecurrence.getRepeatInterval(), expectedEventRecurrence.getRepeatInterval());
        assertNotNull(actualEventRecurrence.getRepeatEndDate());
    }

}
