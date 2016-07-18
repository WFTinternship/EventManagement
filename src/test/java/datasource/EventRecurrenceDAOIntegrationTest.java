package datasource;

import com.workfront.internship.event_management.DAO.*;
import com.workfront.internship.event_management.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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


    private Category testCategory;
    private Event testEvent;
    private RecurrenceType testRecurrenceType;
    private EventRecurrence testEventRecurrence;

    @BeforeClass
    public static void setUpClass() {
        categoryDAO = new CategoryDAOImpl();
        eventDAO = new EventDAOImpl();
        recurrenceTypeDAO = new RecurrenceTypeDAOImpl();
        eventRecurrenceDAO = new EventRecurrenceDAOImpl();
    }

    @Before
    public void setUp() {
        // create test objects
        testCategory = TestHelper.createTestCategory();
        testEvent = TestHelper.createTestEvent();
        testRecurrenceType = TestHelper.createTestRecurrenceType();
        testEventRecurrence = TestHelper.createTestEventRecurrence();

        //insert test objects data into db
        insertTestObjectsIntoDB();
    }

    @After
    public void tearDown() {
        categoryDAO.deleteAllCategories();
        recurrenceTypeDAO.deleteAllRecurrenceTypes();
        eventRecurrenceDAO.deleteAllEventRecurrences();
        eventDAO.deleteAllEvents();
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
    public void getAllEventRecurrencesByEventId__Empty_List() {
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
        testEventRecurrence.setRepeatOn("updated repeat_on value")
                .setRepeatInterval(7);

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
    public void deleteEventRecurrence_Found() {
        //testing method
        boolean deleted = eventRecurrenceDAO.deleteEventRecurrence(testEventRecurrence.getId());

        EventRecurrence eventRecurrence = eventRecurrenceDAO.getEventRecurrenceById(testEventRecurrence.getId());

        assertTrue(deleted);
        assertNull(eventRecurrence);
    }

    @Test
    public void deleteEventRecurrence_Not_Found() {
        //test method
        boolean deleted = eventRecurrenceDAO.deleteEventRecurrence(TestHelper.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteAllEventRecurrences_Found() {
        //testing method
        boolean deleted = eventRecurrenceDAO.deleteAllEventRecurrences();

        List<EventRecurrence> eventRecurrenceList = eventRecurrenceDAO.getAllEventRecurrences();

        assertNotNull(eventRecurrenceList);
        assertTrue(eventRecurrenceList.isEmpty());
        assertTrue(deleted);
    }

    @Test
    public void deleteAllEventRecurrences_Not_Found() {
        //delete inserted test record
        eventRecurrenceDAO.deleteEventRecurrence(testEventRecurrence.getId());

        //testing method
        boolean deleted = eventRecurrenceDAO.deleteAllEventRecurrences();

        assertFalse(deleted);
    }


    //helper methods
    private void createTestObjects() {

    }

    private void insertTestObjectsIntoDB() {
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

        //insert event recurrence info into db
        testEventRecurrence.setEventId(testEvent.getId())
                .setRecurrenceType(testRecurrenceType);
        int eventRecurrenceId = eventRecurrenceDAO.addEventRecurrence(testEventRecurrence);
        testEventRecurrence.setId(eventRecurrenceId);
    }


    private void assertEqualEventRecurrences(EventRecurrence actualEventRecurrence, EventRecurrence expectedEventRecurrence) {
        assertEquals(actualEventRecurrence.getEventId(), expectedEventRecurrence.getEventId());
        assertEquals(actualEventRecurrence.getRecurrenceType().getId(), expectedEventRecurrence.getRecurrenceType().getId());
        assertEquals(actualEventRecurrence.getRepeatOn(), expectedEventRecurrence.getRepeatOn());
        assertEquals(actualEventRecurrence.getRepeatInterval(), expectedEventRecurrence.getRepeatInterval());
        assertNotNull(actualEventRecurrence.getRepeatEndDate());
    }
}
