package com.workfront.internship.event_management.service;

import com.sun.jnlp.FileOpenServiceImpl;
import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.dao.EventDAO;
import com.workfront.internship.event_management.dao.EventDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.Invitation;
import com.workfront.internship.event_management.model.Recurrence;
import org.junit.*;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualEvents;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static junit.framework.TestCase.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class EventServiceUnitTest {

    private static EventService eventService;
    private CategoryService categoryService;
    private InvitationService invitationService;
    private MediaService mediaService;
    private EventDAO eventDAO;
    private EmailService emailService;
    private FileService fileService;

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
        emailService = Mockito.mock(EmailServiceImpl.class);
        categoryService = Mockito.mock(CategoryServiceImpl.class);
        invitationService = Mockito.mock(InvitationServiceImpl.class);
        mediaService = Mockito.mock(MediaServiceImpl.class);
        fileService = Mockito.mock(FileService.class);

        //injecting mocks
        Whitebox.setInternalState(eventService, "eventDAO", eventDAO);
        Whitebox.setInternalState(eventService, "emailService", emailService);
        Whitebox.setInternalState(eventService, "categoryService", categoryService);
        Whitebox.setInternalState(eventService, "invitationService", invitationService);
        Whitebox.setInternalState(eventService, "mediaService", mediaService);
        Whitebox.setInternalState(eventService, "fileService", fileService);
    }

    @After
    public void tearDown() {
        testEvent = null;
        eventDAO = null;
    }

    //Testing addEvent method
    @Test(expected = InvalidObjectException.class)
    public void createEvent_InvalidEvent() {
        testEvent.setTitle("");

        //method under test
        eventService.createEvent(testEvent);
    }

    @Test(expected = OperationFailedException.class)
    public void createEvent_UnableToInsert() {
        when(eventDAO.addEvent(testEvent)).thenReturn(0);

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

        when(eventDAO.addEventWithRecurrences(testEvent)).thenReturn(VALID_ID);

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

        //method under test
        eventService.createEvent(testEvent);

        verify(invitationService).addInvitations(testEvent.getInvitations());
    }


    @Test(expected = OperationFailedException.class)
    public void addEvent_WithInvitations_FailInsert() {
        Invitation invitation = createTestInvitation();
        List<Invitation> invitationList = new ArrayList<>();
        invitationList.add(invitation);

        testEvent.setId(VALID_ID);
        testEvent.setInvitations(invitationList);
        when(eventDAO.addEvent(testEvent)).thenReturn(VALID_ID);
        doThrow(InvalidObjectException.class).when(invitationService).addInvitations(anyListOf(Invitation.class));

        //method under test
        eventService.createEvent(testEvent);
    }

    @Test(expected = OperationFailedException.class)
    public void addEvent_WithInvitations_FailSendMail() {
        Invitation invitation = createTestInvitation();
        List<Invitation> invitationList = new ArrayList<>();
        invitationList.add(invitation);

        testEvent.setId(VALID_ID);
        testEvent.setInvitations(invitationList);

        when(eventDAO.addEvent(testEvent)).thenReturn(VALID_ID);
        doThrow(MailSendException.class).when(emailService).sendInvitations(any(Event.class));

        //method under test
        eventService.createEvent(testEvent);
    }

    @Test
    public void addEvent_WithoutInvitations_Success() {
        testEvent.setId(VALID_ID);
        when(eventDAO.addEvent(testEvent)).thenReturn(VALID_ID);

        //mock invitation service class
        InvitationService invitationService = Mockito.mock(InvitationServiceImpl.class);
        Whitebox.setInternalState(eventService, "invitationService", invitationService);

        //method under test
        eventService.createEvent(testEvent);

        verify(invitationService, never()).addInvitations((List<Invitation>) anyObject());
        verify(emailService, never()).sendInvitations(any(Event.class));
    }

    //Testing getEventById method
    @Test(expected = InvalidObjectException.class)
    public void getEventById_Invalid_Id() {
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

    //Testing getFullEventById method
    @Test
    public void getFullEventById_Success() {
        testEvent.setId(VALID_ID);
        when(eventDAO.getEventById(VALID_ID)).thenReturn(testEvent);

        //method under test
        Event actualEvent = eventService.getFullEventById(TestObjectCreator.VALID_ID);
        assertEqualEvents(actualEvent, testEvent);

        verify(invitationService).getInvitationsByEvent(VALID_ID);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getFullEventById_Not_Found() {
        testEvent.setId(VALID_ID);
        when(eventDAO.getEventById(VALID_ID)).thenReturn(null);

        //method under test
        Event actualEvent = eventService.getFullEventById(TestObjectCreator.VALID_ID);
    }

    //Testing getEventByCategory method
    @Test(expected = InvalidObjectException.class)
    public void getAllEventsByCategory_InvalidId() {
        //method under test
        eventService.getAllEventsByCategory(INVALID_ID);

        verify(eventService, never()).getAllEventsByCategory(INVALID_ID);
    }

    @Test
    public void getAllEventsByCategory_Success() {
        testEvent.setId(TestObjectCreator.VALID_ID);
        when(eventDAO.getAllEventsByCategory(VALID_ID)).thenReturn(testEventList);

        //method under test
        List<Event> actualEventList = eventService.getAllEventsByCategory(TestObjectCreator.VALID_ID);
        assertNotNull(actualEventList);
        assertFalse(actualEventList.isEmpty());
        assertEquals(actualEventList.size(), 1);
        assertEqualEvents(actualEventList.get(0), testEvent);
    }

    @Test(expected = InvalidObjectException.class)
    public void getPublicEventsByCategory_InvalidId() {
        //method under test
        eventService.getPublicEventsByCategory(TestObjectCreator.INVALID_ID);

        verify(eventService, never()).getAllEventsByCategory(INVALID_ID);
    }

    @Test
    public void getPublicEventsByCategory_Success() {
        //method under test
        eventService.getPublicEventsByCategory(VALID_ID);

        verify(eventDAO).getPublicEventsByCategory(VALID_ID);
    }

    //Testing getUserOrganizedEvents method
    @Test(expected = InvalidObjectException.class)
    public void getUserOrganizedEvents_InvalidId() {
        //method under test
        eventService.getUserOrganizedEvents(INVALID_ID);
    }

    @Test(expected = InvalidObjectException.class)
    public void getUserInvitedEvents_InvalidId() {
        //method under test
        eventService.getUserInvitedEvents(INVALID_ID);
    }

    @Test
    public void getUserInvitedEvents_Success() {
        //method under test
        eventService.getUserInvitedEvents(VALID_ID);

        verify(eventDAO).getUserAllEvents(VALID_ID);
    }

    @Test
    public void getUserEventsByResponse_Success() {
        //method under test
        eventService.getUserEventsByResponse(VALID_ID, "Yes");

        verify(eventDAO).getUserEventsByResponse(VALID_ID, "Yes");
    }

    @Test (expected = InvalidObjectException.class)
    public void getUserEventsByResponse_InvalidUserId() {
        //method under test
        eventService.getUserEventsByResponse(INVALID_ID, "Yes");

        verify(eventDAO, never()).getUserEventsByResponse(anyInt(), anyString());
    }

    @Test (expected = InvalidObjectException.class)
    public void getUserEventsByResponse_EmptyResponse() {
        //method under test
        eventService.getUserEventsByResponse(VALID_ID, "");

        verify(eventDAO, never()).getUserEventsByResponse(anyInt(), anyString());
    }

    @Test
    public void getUserOrganizedEvents_Success() {
        testEvent.setId(VALID_ID);
        when(eventDAO.getUserOrganizedEvents(VALID_ID)).thenReturn(testEventList);

        //method under test
        List<Event> actualEventList = eventService.getUserOrganizedEvents(VALID_ID);
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

    @Test(expected = OperationFailedException.class)
    public void editEvent_NotFound() {
        testEvent.setId(VALID_ID).setImageName("new-name.jpg");
        when(eventDAO.updateEvent(testEvent)).thenReturn(false);

        when(eventDAO.getEventById(testEvent.getId())).thenReturn(testEvent);
        //method under test
        eventService.editEvent(testEvent);
    }

    @Test
    public void editEvent_EmptyOldImage() throws IOException {
        Event oldEvent = createTestEvent().setId(VALID_ID);
        oldEvent.setImageName("");
        when(eventDAO.getEventById(anyInt())).thenReturn(oldEvent);

        when(eventDAO.updateEvent(testEvent)).thenReturn(true);

        testEvent.setId(VALID_ID);

        //method under test
        eventService.editEvent(testEvent);
        verify(fileService, never()).deleteFile(anyString());
    }

    @Test
    public void editEvent_NoNewImage_DeleteOld() throws IOException {
        Event oldEvent = createTestEvent().setId(VALID_ID);
        oldEvent.setImageName("old-image.jpg");

        when(eventDAO.getEventById(anyInt())).thenReturn(oldEvent);
        when(eventDAO.updateEvent(testEvent)).thenReturn(true);

        testEvent.setId(VALID_ID).setImageName("");

        //method under test
        eventService.editEvent(testEvent);
        verify(fileService).deleteFile(anyString());
    }

    @Test(expected = OperationFailedException.class)
    public void editEvent_InvalidInvitations() {
        when(eventDAO.updateEvent(testEvent.setId(VALID_ID))).thenReturn(true);
        when(eventDAO.getEventById(testEvent.getId())).thenReturn(testEvent);

        doThrow(InvalidObjectException.class).when(invitationService).editInvitationList(any(Event.class));

        //method under test
        eventService.editEvent(testEvent);
    }

    @Test(expected = OperationFailedException.class)
    public void editEvent_InvitationEditError() {

        when(eventDAO.updateEvent(testEvent.setId(VALID_ID))).thenReturn(true);
        when(eventDAO.getEventById(testEvent.getId())).thenReturn(testEvent);

        doThrow(OperationFailedException.class).when(invitationService).editInvitationList(any(Event.class));

        //method under test
        eventService.editEvent(testEvent);
    }

    @Test(expected = OperationFailedException.class)
    public void editEvent_EmailSendError() {

        when(eventDAO.updateEvent(testEvent.setId(VALID_ID))).thenReturn(true);
        when(eventDAO.getEventById(testEvent.getId())).thenReturn(testEvent);

        doThrow(MailSendException.class).when(invitationService).editInvitationList(any(Event.class));

        //method under test
        eventService.editEvent(testEvent);
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

    @Test
    public void getAllUpcomingEvents_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        eventService.getAllUpcomingEvents();

        verify(eventDAO).getAllUpcomingEvents();
    }

    @Test
    public void getPublicUpcomingEvents_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        eventService.getPublicUpcomingEvents();

        verify(eventDAO).getPublicUpcomingEvents();
    }
    @Test
    public void getAllPastEvents_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        eventService.getAllPastEvents();

        verify(eventDAO).getAllPastEvents();
    }

    @Test
    public void getPublicPastEvents_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        eventService.getPublicPastEvents();

        verify(eventDAO).getPublicPastEvents();
    }

    @Test
    public void getPublicEvents_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        eventService.getPublicEvents();

        verify(eventDAO).getPublicEvents();
    }

    //Testing deleteAllEvents method
    @Test
    public void deleteAllEvents_Success() {
        //method under test
        eventService.deleteAllEvents();

        verify(eventDAO).deleteAllEvents();
    }

    @Test(expected = InvalidObjectException.class)
    public void getAllEventsByKeyword_EmptyKeyword() {
        //method under test
        eventService.getAllEventsByKeyword("");

        verify(eventDAO, never()).getAllEventsByKeyword(anyString());
    }

    @Test
    public void getAllEventsByKeyword_Success() {
        //method under test
        eventService.getAllEventsByKeyword(EXISTING_KEYWORD);

        verify(eventDAO).getAllEventsByKeyword(EXISTING_KEYWORD);
    }

    @Test(expected = InvalidObjectException.class)
    public void getPublicEventsByKeyword_EmptyKeyword() {
        //method under test
        eventService.getPublicEventsByKeyword("");

        verify(eventDAO, never()).getPublicEventsByKeyword(anyString());
    }

    @Test
    public void getPublicEventsByKeyword_Success() {
        //method under test
        eventService.getPublicEventsByKeyword(EXISTING_KEYWORD);
    }


}
