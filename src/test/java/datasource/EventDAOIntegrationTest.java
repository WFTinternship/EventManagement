package datasource;

import com.workfront.internship.event_management.datasource.CategoryDAO;
import com.workfront.internship.event_management.datasource.CategoryDAOImpl;
import com.workfront.internship.event_management.datasource.EventDAO;
import com.workfront.internship.event_management.datasource.EventDAOImpl;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Hermine Turshujyan 7/15/16.
 */
public class EventDAOIntegrationTest {

    private static EventDAO eventDAO;
    private static CategoryDAO categoryDAO;
    private Category testCategory;
    private Event testEvent;

    @BeforeClass
    public static void setUpClass() {
        categoryDAO = new CategoryDAOImpl();
        eventDAO = new EventDAOImpl();
    }

    @Before
    public void setUp() {
        //create test category, insert into db and  get generated id
        testCategory = TestHelper.createTestCategory();
        int categoryId = categoryDAO.addCategory(testCategory);
        testCategory.setId(categoryId);

        //create test event, insert into db and get generated id
        testEvent = TestHelper.createTestEvent();
        testEvent.setCategory(testCategory);
        int id = eventDAO.addEvent(testEvent);
        testEvent.setId(id);
    }

    @After
    public void tearDown() {
        //delete test records from db
        categoryDAO.deleteAllCategories();
        eventDAO.deleteAllEvents();

        //delete test object
        testCategory = null;
        testEvent = null;
    }

    @Test
    public void addEvent_Success() {
        //test record already inserted, read inserted data
        Event event = eventDAO.getEventById(testEvent.getId());

        assertNotNull(event);
        assertEqualEvent(event, testEvent);
    }

    @Test
    public void addEventWithRecurrencesAndInvitations_Success() {
        //create new event and insert into db
        Event newTestEvent = TestHelper.createTestEventWithRecurrencesAndInvitations();
        int id = eventDAO.addEvent(newTestEvent);
        newTestEvent.setCategory(testCategory);
        newTestEvent.setId(id);

        //read inserted event data
        Event event = eventDAO.getEventById(newTestEvent.getId());

        assertNotNull(event);
        assertEqualEvent(event, newTestEvent);
    }
/*
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
    }*/

    //helper methods
    private void assertEqualEvent(Event actualEvent, Event expectedEvent) {
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

    private void assertEqualEventWithRecurrencesAndInvitations(Event actualEvent, Event expectedEvent) {
        assertEqualEvent(actualEvent, expectedEvent);

        assertNotNull(actualEvent.getInvitations());
        assertNotNull(actualEvent.getEventRecurrences());
        assertEquals(actualEvent.getEventRecurrences().size(), expectedEvent.getEventRecurrences().size());
        assertEquals(actualEvent.getInvitations().size(), expectedEvent.getInvitations().size());
    }

}
