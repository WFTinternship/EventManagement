package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.ObjectNotFoundException;
import com.workfront.internship.event_management.model.*;
import org.junit.*;

import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualRecurrences;
import static junit.framework.TestCase.*;

/**
 * Created by Hermine Turshujyan 7/13/16.
 */
public class RecurrenceDAOIntegrationTest {

    private static CategoryDAO categoryDAO;
    private static EventDAO eventDAO;
    private static RecurrenceDAO eventRecurrenceDAO;
    private static RecurrenceTypeDAO recurrenceTypeDAO;
    private static RecurrenceOptionDAO recurrenceOptionDAO;

    private Category testCategory;
    private Event testEvent;
    private RecurrenceType testRecurrenceType;
    private RecurrenceOption testRecurrenceOption;
    private Recurrence testEventRecurrence;

    @BeforeClass
    public static void setUpClass() throws DAOException {
        categoryDAO = new CategoryDAOImpl();
        eventDAO = new EventDAOImpl();
        recurrenceTypeDAO = new RecurrenceTypeDAOImpl();
        eventRecurrenceDAO = new RecurrenceDAOImpl();
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
    public void setUp() throws DAOException, DuplicateEntryException {
        createTestObjects();
        insertTestObjectsIntoDB();
    }

    @After
    public void tearDown() throws DAOException {
        deleteTestRecordsFromDB();
        deleteTestObjects();
    }

    @Test
    public void addEventRecurrence_Success() throws DAOException, ObjectNotFoundException {
        //test category already inserted in setup, read record by categoryId
        Recurrence eventRecurrence = eventRecurrenceDAO.getRecurrenceById(testEventRecurrence.getId());

        assertNotNull(eventRecurrence);
        assertEqualRecurrences(eventRecurrence, testEventRecurrence);
    }

    @Test
    public void getEventRecurrenceById_Found() throws DAOException, ObjectNotFoundException {
        //test method
        Recurrence eventRecurrence = eventRecurrenceDAO.getRecurrenceById(testEventRecurrence.getId());

        assertNotNull(eventRecurrence);
        assertEqualRecurrences(eventRecurrence, testEventRecurrence);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getEventRecurrenceById_Not_Found() throws DAOException, ObjectNotFoundException {
        //test method
        Recurrence eventRecurrence = eventRecurrenceDAO.getRecurrenceById(TestObjectCreator.NON_EXISTING_ID);

        assertNull(eventRecurrence);
    }

    @Test
    public void getEventRecurrencesByEventId_Found() throws DAOException {
        //test method
        List<Recurrence> eventRecurrenceList = eventRecurrenceDAO.getRecurrencesByEventId(testEvent.getId());

        assertNotNull(eventRecurrenceList);
        assertEquals(eventRecurrenceList.size(), 1);
        assertEqualRecurrences(eventRecurrenceList.get(0), testEventRecurrence);
    }

    @Test
    public void getEventRecurrencesByEventId__Empty_List() throws DAOException {
        //test method
        List<Recurrence> eventRecurrenceList = eventRecurrenceDAO.getRecurrencesByEventId(TestObjectCreator.NON_EXISTING_ID);

        assertNotNull(eventRecurrenceList);
        assertTrue(eventRecurrenceList.isEmpty());
    }

    @Test
    public void getAllEventRecurrencesByEventId_Found() throws DAOException {
        //test method
        List<Recurrence> eventRecurrenceList = eventRecurrenceDAO.getAllRecurrences();

        assertNotNull(eventRecurrenceList);
        assertEquals(eventRecurrenceList.size(), 1);
        assertEqualRecurrences(eventRecurrenceList.get(0), testEventRecurrence);
    }

    @Test
    public void getAllEventRecurrencesByEventId__Empty_List() throws DAOException, ObjectNotFoundException {
        //delete inserted test record
        eventRecurrenceDAO.deleteRecurrence(testEventRecurrence.getId());

        //test method
        List<Recurrence> eventRecurrenceList = eventRecurrenceDAO.getAllRecurrences();

        assertNotNull(eventRecurrenceList);
        assertTrue(eventRecurrenceList.isEmpty());
    }


    @Test
    public void updateEventRecurrence_Success() throws DAOException, ObjectNotFoundException {
        //update recurrence type info
        testEventRecurrence.setRepeatInterval(7);

        //test method
        eventRecurrenceDAO.updateRecurrence(testEventRecurrence);

        //read updated record from db
        Recurrence eventRecurrence = eventRecurrenceDAO.getRecurrenceById(testEventRecurrence.getId());

        assertNotNull(eventRecurrence);
        assertEqualRecurrences(eventRecurrence, testEventRecurrence);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void updateEventRecurrence_Not_Found() throws DAOException, ObjectNotFoundException {
        //create new event recurrence  object with no id
        Recurrence newEventRecurrence = TestObjectCreator.createTestRecurrence();

        //test method
        eventRecurrenceDAO.updateRecurrence(newEventRecurrence);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteEventRecurrence_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        eventRecurrenceDAO.deleteRecurrence(testEventRecurrence.getId());

        Recurrence eventRecurrence = eventRecurrenceDAO.getRecurrenceById(testEventRecurrence.getId());

        assertNull(eventRecurrence);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteEventRecurrence_Not_Found() throws DAOException, ObjectNotFoundException {
        //test method
        eventRecurrenceDAO.deleteRecurrence(TestObjectCreator.NON_EXISTING_ID);
    }

    @Test
    public void deleteAllEventRecurrences_Found() throws DAOException {
        //testing method
        eventRecurrenceDAO.deleteAllRecurrences();

        List<Recurrence> eventRecurrenceList = eventRecurrenceDAO.getAllRecurrences();

        assertNotNull(eventRecurrenceList);
        assertTrue(eventRecurrenceList.isEmpty());
    }

    @Test
    public void deleteAllEventRecurrences_Not_Found() throws DAOException, ObjectNotFoundException {
        //delete inserted test record
        eventRecurrenceDAO.deleteRecurrence(testEventRecurrence.getId());

        //testing method
        eventRecurrenceDAO.deleteAllRecurrences();
    }


    //helper methods
    private void createTestObjects() {
        testCategory = TestObjectCreator.createTestCategory();
        testEvent = TestObjectCreator.createTestEvent();
        testRecurrenceType = TestObjectCreator.createTestRecurrenceType();
        testEventRecurrence = TestObjectCreator.createTestRecurrence();
        testRecurrenceOption = TestObjectCreator.createTestRecurrenceOption();
    }

    private void insertTestObjectsIntoDB() throws DAOException, DuplicateEntryException {
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
                .setRecurrenceOption(testRecurrenceOption);

        int eventRecurrenceId = eventRecurrenceDAO.addRecurrence(testEventRecurrence);
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
        eventRecurrenceDAO.deleteAllRecurrences();
        eventDAO.deleteAllEvents();
    }
}
