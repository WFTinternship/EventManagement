package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.dao.EventDAO;
import com.workfront.internship.event_management.dao.EventDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.Invitation;
import com.workfront.internship.event_management.model.Recurrence;
import org.junit.*;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualEvents;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static junit.framework.TestCase.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class EventServiceUnitTest {

    private static EventService eventService;
    private EventDAO eventDAO;
    private Event testEvent;
    private List<Event> testEventList;

    @BeforeClass
    public static void setUpClass() {
        eventService = new EventServiceImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        eventService = null;
    }

    @Before
    public void setUp() {
        //create test Event object
        testEvent = createTestEvent();
        testEventList = new ArrayList<>();
        testEventList.add(testEvent);

        //creating mocks
        eventDAO = Mockito.mock(EventDAOImpl.class);
        RecurrenceService recurrenceService = Mockito.mock(RecurrenceServiceImpl.class);
        InvitationService invitationService = Mockito.mock(InvitationServiceImpl.class);

        //injecting mocks
        Whitebox.setInternalState(eventService, "eventDAO", eventDAO);
        Whitebox.setInternalState(eventService, "recurrenceService", recurrenceService);
        Whitebox.setInternalState(eventService, "invitationService", invitationService);

    }

    @After
    public void tearDown() {
        testEvent = null;
        eventDAO = null;
    }

    //Testing addEvent method
    @Test(expected = InvalidObjectException.class)
    public void addEvent_InvalidEvent() {
        testEvent.setTitle("");

        //method under test
        eventService.createEvent(testEvent);
    }

    @Test
    public void addEvent_Success() {
        testEvent.setId(VALID_ID);
        when(eventDAO.addEvent(testEvent)).thenReturn(VALID_ID);

        //method under test
        Event actualEvent = eventService.createEvent(testEvent);

        assertEqualEvents(actualEvent, testEvent);
    }

    @Test
    public void addEvent_WithRecurrences_Success() {
        Recurrence recurrence = createTestRecurrence();
        List<Recurrence> recurrenceList = new ArrayList<>();
        recurrenceList.add(recurrence);

        testEvent.setId(VALID_ID);
        testEvent.setEventRecurrences(recurrenceList);

        when(eventDAO.addEvent(testEvent)).thenReturn(VALID_ID);

        //method under test
        Event actualEvent = eventService.createEvent(testEvent);

        assertEqualEvents(actualEvent, testEvent);

        verify(eventDAO).addEventWithRecurrences(testEvent);
    }

    @Test
    public void addEvent_WithInvitations_Success() {
        Invitation invitation = createTestInvitation();
        List<Invitation> invitationList = new ArrayList<>();
        invitationList.add(invitation);

        testEvent.setId(VALID_ID);
        testEvent.setInvitations(invitationList);
        when(eventDAO.addEvent(testEvent)).thenReturn(VALID_ID);

        //mock invitation service class
        InvitationService invitationService = Mockito.mock(InvitationServiceImpl.class);
        Whitebox.setInternalState(eventService, "invitationService", invitationService);

        //method under test
        eventService.createEvent(testEvent);

        verify(invitationService).addInvitations(testEvent.getInvitations());
    }

    //Testing getEventById method
    @Test(expected = InvalidObjectException.class)
    public void getEventById_InvalidId() {
        //method under test
        eventService.getEventById(TestObjectCreator.INVALID_ID);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getEventById_NotFound() {
        when(eventDAO.getEventById(NON_EXISTING_ID)).thenReturn(null);

        //method under test
        eventService.getEventById(NON_EXISTING_ID);
    }

    @Test
    public void getEventById_Success() {
        testEvent.setId(TestObjectCreator.VALID_ID);
        when(eventDAO.getEventById(TestObjectCreator.VALID_ID)).thenReturn(testEvent);

        //method under test
        Event actualEvent = eventService.getEventById(TestObjectCreator.VALID_ID);
        assertEqualEvents(actualEvent, testEvent);
    }

    //Testing getEventByCategory method
    @Test(expected = InvalidObjectException.class)
    public void getEventByCategory_InvalidId() {
        //method under test
        eventService.getEventsByCategory(TestObjectCreator.INVALID_ID);
    }

    @Test
    public void getEventByCategory_Success() {
        testEvent.setId(TestObjectCreator.VALID_ID);
        when(eventDAO.getEventsByCategory(VALID_ID)).thenReturn(testEventList);

        //method under test
        List<Event> actualEventList = eventService.getEventsByCategory(TestObjectCreator.VALID_ID);
        assertNotNull(actualEventList);
        assertFalse(actualEventList.isEmpty());
        assertEquals(actualEventList.size(), 1);
        assertEqualEvents(actualEventList.get(0), testEvent);
    }

    //Testing getUserOrganizedEvents method
    @Test(expected = InvalidObjectException.class)
    public void getUserOrganizedEvents_InvalidId() {
        //method under test
        eventService.getUserOrganizedEvents(INVALID_ID);
    }

    @Test
    public void getUserOrganizedEvents_Success() throws ObjectNotFoundException, DAOException {
        testEvent.setId(TestObjectCreator.VALID_ID);
        when(eventDAO.getUserOrganizedEvents(VALID_ID)).thenReturn(testEventList);

        //method under test
        List<Event> actualEventList = eventService.getUserOrganizedEvents(TestObjectCreator.VALID_ID);
        assertNotNull(actualEventList);
        assertFalse(actualEventList.isEmpty());
        assertEquals(actualEventList.size(), 1);
        assertEqualEvents(actualEventList.get(0), testEvent);
    }

    //Testing getUserParticipatedEvents method
    @Test(expected = InvalidObjectException.class)
    public void getUserParticipatedEvents_InvalidId() {
        //method under test
        eventService.getUserParticipatedEvents(INVALID_ID);
    }

    @Test
    public void getUserParticipatedEvents_Success() {
        testEvent.setId(TestObjectCreator.VALID_ID);

        when(eventDAO.getUserParticipatedEvents(VALID_ID)).thenReturn(testEventList);

        //method under test
        List<Event> actualEventList = eventService.getUserParticipatedEvents(TestObjectCreator.VALID_ID);

        assertNotNull(actualEventList);
        assertFalse(actualEventList.isEmpty());
        assertEquals(actualEventList.size(), 1);
        assertEqualEvents(actualEventList.get(0), testEvent);
    }

    //Testing editEvent method
    @Test(expected = InvalidObjectException.class)
    public void editEvent_InvalidEvent() {
        testEvent.setCategory(null);

        //method under test
        eventService.editEvent(testEvent);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void editEvent_NotFound() {
        when(eventDAO.updateEvent(testEvent)).thenReturn(false);

        //method under test
        eventService.editEvent(testEvent);
    }

    @Test
    public void editEvent_Success() {
        when(eventDAO.updateEvent(testEvent)).thenReturn(true);

        //method under test
        testEvent.setId(VALID_ID);
        eventService.editEvent(testEvent);

        verify(eventDAO).updateEvent(testEvent);
    }

    //Testing deleteEvent method
    @Test(expected = InvalidObjectException.class)
    public void deleteEvent_InvalidId() {
        //method under test
        eventService.deleteEvent(INVALID_ID);
    }

    @Test
    public void deleteEvent_NotFound() {
        when(eventDAO.deleteEvent(NON_EXISTING_ID)).thenReturn(false);

        //method under test
        boolean success = eventService.deleteEvent(NON_EXISTING_ID);
        assertFalse(success);
    }

    @Test
    public void deleteEvent_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        eventService.deleteEvent(VALID_ID);

        verify(eventDAO).deleteEvent(VALID_ID);
    }

    //Testing getAllEvents method
    @Test
    public void getAllEvents_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        eventService.getAllEvents();

        verify(eventDAO).getAllEvents();
    }

    //Testing deleteAllEvents method
    @Test
    public void deleteAllEvents_Success() {
        //method under test
        eventService.deleteAllEvents();

        verify(eventDAO).deleteAllEvents();
    }
}
