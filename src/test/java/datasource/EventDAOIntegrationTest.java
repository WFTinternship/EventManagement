package datasource;

import com.workfront.internship.event_management.dao.*;
import com.workfront.internship.event_management.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Hermine Turshujyan 7/15/16.
 */
public class EventDAOIntegrationTest {
    private static UserDAO userDAO;
    private static EventDAO eventDAO;
    private static CategoryDAO categoryDAO;
    private static RecurrenceTypeDAO recurrenceTypeDAO;
    private Category testCategory;
    private Event testEvent;
    private User testUser1;
    private User testUser2;
    private RecurrenceType testRecurrenceType;

    @BeforeClass
    public static void setUpClass() {
        userDAO = new UserDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        eventDAO = new EventDAOImpl();
        recurrenceTypeDAO = new RecurrenceTypeDAOImpl();
    }

    @Before
    public void setUp() {

        //create test users, insert into db
        testUser1 = TestHelper.createTestUser();
        testUser2 = TestHelper.createTestUser();
        int id1 = userDAO.addUser(testUser1);
        testUser1.setId(id1);
        int id2 = userDAO.addUser(testUser2);
        testUser2.setId(id2);

        //create test categody, insert into db
        testCategory = TestHelper.createTestCategory();
        int categoryId = categoryDAO.addCategory(testCategory);
        testCategory.setId(categoryId);

        //create test recurrence type, insert into db
        testRecurrenceType = TestHelper.createTestRecurrenceType();
        int recurrenceTypeId = recurrenceTypeDAO.addRecurrenceType(testRecurrenceType);
        testRecurrenceType.setId(recurrenceTypeId);

        testEvent = TestHelper.createTestEventWithRecurrencesAndInvitations(testUser1, testUser2, testRecurrenceType);

        //insert event into db
        testEvent.setCategory(testCategory);
        int id = eventDAO.addEvent(testEvent);
        testEvent.setId(id);
    }

    @After
    public void tearDown() {
        //delete test records from db
        userDAO.deleteAllUsers();
        recurrenceTypeDAO.deleteAllRecurrenceTypes();
        categoryDAO.deleteAllCategories();
        eventDAO.deleteAllEvents();
    }

    @Test
    public void addEvent_Success() {
        //test record already inserted, read inserted data
        Event event = eventDAO.getEventById(testEvent.getId());

        assertNotNull(event);
        assertEqualEvents(event, testEvent);
    }


    @Test
    public void getEventById_Found() {
        //test method
        Event event = eventDAO.getEventById(testEvent.getId());

        assertNotNull(event);
        assertEqualEventsWithFullInfo(event, testEvent);
    }

    @Test
    public void getEventById_Not_Found() {
        //test method
        Event event = eventDAO.getEventById(TestHelper.NON_EXISTING_ID);

        assertNull(event);
    }

    @Test
    public void getAllEvents_Found() {
        //testing method
        List<Event> eventList = eventDAO.getAllEvents();

        assertNotNull(eventList);
        assertEquals(eventList.size(), 1);
        assertEqualEvents(eventList.get(0), testEvent);
    }

    @Test
    public void getAllEvents_EmptyList() {
        //delete inserted test record
        eventDAO.deleteEvent(testEvent.getId());

        List<Event> eventList = eventDAO.getAllEvents();

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
    }

    @Test
    public void getEventsByCategoryId_Found() {
        //testing method
        List<Event> eventList = eventDAO.getEventsByCategory(testCategory.getId());

        assertNotNull(eventList);
        assertEquals(eventList.size(), 1);
        assertEqualEvents(eventList.get(0), testEvent);
    }

    @Test
    public void getEventsByCategoryId_Empty_List() {
        //delete inserted test record
        List<Event> eventList = eventDAO.getEventsByCategory(TestHelper.NON_EXISTING_ID);

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
    }


    @Test
    public void deleteEvent_Found() {
        //testing method
        boolean deleted = eventDAO.deleteEvent(testEvent.getId());

        Event event = eventDAO.getEventById(testEvent.getId());

        assertTrue(deleted);
        assertNull(event);
    }

    @Test
    public void deleteEvent_Not_Found() {
        //testing method
        boolean deleted = eventDAO.deleteEvent(TestHelper.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteAllEvents_Found() {
        //testing method
        boolean deleted = eventDAO.deleteAllEvents();

        List<Event> eventList = eventDAO.getAllEvents();

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
        assertTrue(deleted);
    }

    @Test
    public void deleteAllEvent_Not_Found() {
        //delete inserted event
        eventDAO.deleteEvent(testEvent.getId());

        //testing method
        boolean deleted = eventDAO.deleteAllEvents();

        assertFalse(deleted);
    }

    //helper methods
    private void assertEqualEvents(Event actualEvent, Event expectedEvent) {
        assertEquals(actualEvent.getId(), expectedEvent.getId());
        assertEquals(actualEvent.getTitle(), expectedEvent.getTitle());
        assertEquals(actualEvent.getCategory().getId(), expectedEvent.getCategory().getId());
        assertEquals(actualEvent.getShortDescription(), expectedEvent.getShortDescription());
        assertEquals(actualEvent.getFullDescription(), expectedEvent.getFullDescription());
        assertEquals(actualEvent.getLocation(), expectedEvent.getLocation());
        assertEquals(actualEvent.getLat(), expectedEvent.getLat(), 0);
        assertEquals(actualEvent.getLng(), expectedEvent.getLng(), 0);
        assertEquals(actualEvent.getFilePath(), expectedEvent.getFilePath());
        assertEquals(actualEvent.getImagePath(), expectedEvent.getImagePath());
        assertNotNull(actualEvent.getCreationDate());
        //assertEquals(actualEvent.getLastModifiedDate(), expectedEvent.getLastModifiedDate());
        assertEquals(actualEvent.isPublicAccessed(), expectedEvent.isPublicAccessed());
        assertEquals(actualEvent.isGuestsAllowed(), expectedEvent.isGuestsAllowed());
    }

    private void assertEqualEventsWithFullInfo(Event actualEvent, Event expectedEvent) {
        assertEqualEvents(actualEvent, expectedEvent);

        assertNotNull(actualEvent.getInvitations());
        assertNotNull(actualEvent.getEventRecurrences());
        assertEquals(actualEvent.getEventRecurrences().size(), expectedEvent.getEventRecurrences().size());
        assertEquals(actualEvent.getInvitations().size(), expectedEvent.getInvitations().size());
    }

}
