package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.*;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualEvents;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static junit.framework.TestCase.*;

/**
 * Created by Hermine Turshujyan 7/15/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class EventDAOIntegrationTest {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private EventDAO eventDAO;
    @Autowired
    private CategoryDAO categoryDAO;
    @Autowired
    private RecurrenceTypeDAO recurrenceTypeDAO;
    @Autowired
    private RecurrenceOptionDAO recurrenceOptionDAO;
    @Autowired
    private RecurrenceDAO eventRecurrenceDAO;
    @Autowired
    private InvitationDAO invitationDAO;


    private Category testCategory;
    private Event testEvent;
    private User testUser1;
    private User testUser2;
    private RecurrenceType testRecurrenceType;
    private RecurrenceOption testRecurrenceOption;
    private Recurrence testEventRecurrence;

    @Before
    public void setUp() throws DAOException, DuplicateEntryException {

        //create test users, insert into db
        testUser1 = createTestUser();
        testUser2 = createTestUser();
        int id1 = userDAO.addUser(testUser1);
        testUser1.setId(id1);
        int id2 = userDAO.addUser(testUser2);
        testUser2.setId(id2);

        //create test category, insert into db
        testCategory = createTestCategory();
        int categoryId = categoryDAO.addCategory(testCategory);
        testCategory.setId(categoryId);

        //create test recurrence type, insert into db
        testRecurrenceType = TestObjectCreator.createTestRecurrenceType();
        int recurrenceTypeId = recurrenceTypeDAO.addRecurrenceType(testRecurrenceType);
        testRecurrenceType.setId(recurrenceTypeId);

        //create test recurrence option, insert into db
        testRecurrenceOption = TestObjectCreator.createTestRecurrenceOption();
        testRecurrenceOption.setRecurrenceTypeId(recurrenceTypeId);
        int recurrenceOptionId = recurrenceOptionDAO.addRecurrenceOption(testRecurrenceOption);
        testRecurrenceOption.setId(recurrenceOptionId);

        //create test event recurrence
        testEventRecurrence = createTestRecurrence();
        testEventRecurrence.setRecurrenceOption(testRecurrenceOption)
                .setRecurrenceType(testRecurrenceType);

        //create test event, insert into db
        List<Recurrence> testRecurrenceList = new ArrayList<>();
        testRecurrenceList.add(testEventRecurrence);

        testEvent = TestObjectCreator.createTestEvent();
        testEvent.setCategory(testCategory)
                .setOrganizer(testUser1)
                .setEventRecurrences(testRecurrenceList);
        int id = eventDAO.addEventWithRecurrences(testEvent);
        testEvent.setId(id);
    }

    @After
    public void tearDown() throws DAOException {
        deleteTestRecordsFromDB();
        deleteTestObjects();

    }

    @Test
    public void addEvent_Success() throws DAOException, ObjectNotFoundException {
        //insert event without recurrences
        testEvent.setEventRecurrences(null);
        int eventId = eventDAO.addEvent(testEvent);
        testEvent.setId(eventId);

        Event event = eventDAO.getEventById(testEvent.getId());

        assertNotNull(event);
        assertEqualEvents(event, testEvent);
    }

    @Test
    public void addEventWithRecurrences_Success() throws DAOException, ObjectNotFoundException {
        //test record already inserted, read inserted data
        Event event = eventDAO.getEventById(testEvent.getId());

        assertNotNull(event);
        assertEqualEvents(event, testEvent);
    }

    @Test
    public void getEventById_Found() throws DAOException, ObjectNotFoundException {
        //test method
        Event event = eventDAO.getEventById(testEvent.getId());

        assertNotNull(event);
        assertEqualEvents(event, testEvent);
    }

    @Test
    public void getEventById_Not_Found() throws DAOException, ObjectNotFoundException {
        //test method
        Event event = eventDAO.getEventById(NON_EXISTING_ID);

        assertNull(event);
    }

    @Test
    public void getAllEvents_Found() throws DAOException {
        //testing method
        List<Event> eventList = eventDAO.getAllEvents();

        assertNotNull(eventList);
        assertEquals(eventList.size(), 1);
        assertEqualEvents(eventList.get(0), testEvent);
    }

    @Test
    public void getAllEvents_EmptyList() throws DAOException, ObjectNotFoundException {
        //delete inserted test record
        eventDAO.deleteEvent(testEvent.getId());

        List<Event> eventList = eventDAO.getAllEvents();

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
    }

    @Test
    public void getAllEventsByCategory_Found() throws DAOException {
        //testing method
        List<Event> eventList = eventDAO.getAllEventsByCategory(testCategory.getId());

        assertNotNull(eventList);
        assertEquals(eventList.size(), 1);
        assertEqualEvents(eventList.get(0), testEvent);
    }


    @Test
    public void getAllEventsByCategoryId_Empty_List() throws DAOException {
        //delete inserted test record
        List<Event> eventList = eventDAO.getAllEventsByCategory(NON_EXISTING_ID);

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
    }

    @Test
    public void getUserOrganizedEvents_Found() throws DAOException {
        //delete inserted test record
        List<Event> eventList = eventDAO.getUserOrganizedEvents(testUser1.getId());

        assertNotNull(eventList);
        assertFalse(eventList.isEmpty());
        assertEqualEvents(testEvent, eventList.get(0));
    }

    @Test
    public void getUserOrganizedEvents_EmptyList() throws DAOException {
        //delete inserted test record
        List<Event> eventList = eventDAO.getUserOrganizedEvents(testUser2.getId());

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
    }

    @Test
    public void getAllPastEvents_EmptyList() throws DAOException {
        List<Event> eventList = eventDAO.getAllPastEvents();

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
    }

    @Test
    public void getAllPastEvents_Found() throws DAOException {
        testEvent.setEndDate(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        int eventId = eventDAO.addEvent(testEvent);
        testEvent.setId(eventId);

        List<Event> eventList = eventDAO.getAllPastEvents();

        assertNotNull(eventList);
        assertFalse(eventList.isEmpty());
        assertEqualEvents(testEvent, eventList.get(0));
    }

    @Test
    public void getPublicPastEvents_Found() throws DAOException {
        //add public past event
        testEvent.setEndDate(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        int eventId = eventDAO.addEvent(testEvent);
        testEvent.setId(eventId);


        //add private event
        Event privateTestEvent = createTestEvent().
                setCategory(testCategory).
                setOrganizer(testUser1).
                setPublicAccessed(false);

        int privateEventId = eventDAO.addEvent(privateTestEvent);
        privateTestEvent.setId(privateEventId);

        List<Event> eventList = eventDAO.getPublicPastEvents();

        assertNotNull(eventList);
        assertFalse(eventList.isEmpty());
        assertEqualEvents(testEvent, eventList.get(0));
    }

    @Test
    public void getPublicPastEvents_Not_Found() throws DAOException {
        //delete inserted test event
        eventDAO.deleteAllEvents();

        //add public upcoming event
        testEvent.setEndDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
        int eventId = eventDAO.addEvent(testEvent);
        testEvent.setId(eventId);

        //add private event
        Event privateTestEvent = createTestEvent().
                setCategory(testCategory).
                setOrganizer(testUser1).
                setPublicAccessed(false);

        int privateEventId = eventDAO.addEvent(privateTestEvent);
        privateTestEvent.setId(privateEventId);

        //method under test
        List<Event> eventList = eventDAO.getPublicPastEvents();

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
    }

    @Test
    public void getAllUpcomingEvents_EmptyList() throws DAOException {
        //delete upcomming event
        eventDAO.deleteAllEvents();

        testEvent.setStartDate(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        int eventId = eventDAO.addEvent(testEvent);
        testEvent.setId(eventId);

        List<Event> eventList = eventDAO.getAllUpcomingEvents();

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
    }

    @Test
    public void getAllUpcomingEvents_Found() throws DAOException {
        List<Event> eventList = eventDAO.getAllUpcomingEvents();

        assertNotNull(eventList);
        assertFalse(eventList.isEmpty());
        assertEqualEvents(testEvent, eventList.get(0));
    }

    @Test
    public void getPublicUpcomingEvents_EmptyList() throws DAOException {
        //delete all test event
        eventDAO.deleteAllEvents();

        //create private upcoming event
        testEvent.setStartDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .setPublicAccessed(false);
        int eventId = eventDAO.addEvent(testEvent);
        testEvent.setId(eventId);

        List<Event> eventList = eventDAO.getPublicUpcomingEvents();

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
    }

    @Test
    public void getPublicUpcomingEvents_Found() throws DAOException {
        //delete all test event
        eventDAO.deleteAllEvents();

        //create private upcoming event
        testEvent.setStartDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .setPublicAccessed(true);
        int eventId = eventDAO.addEvent(testEvent);
        testEvent.setId(eventId);

        //method under test
        List<Event> eventList = eventDAO.getPublicUpcomingEvents();

        assertNotNull(eventList);
        assertFalse(eventList.isEmpty());
        assertEqualEvents(testEvent, eventList.get(0));
    }


    @Test
    public void getUserParticipatedEvents_EmptyList() throws DAOException {
        List<Event> eventList = eventDAO.getUserParticipatedEvents(testUser1.getId());

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
    }

    @Test
    public void getUserParticipatedEvents_Found() throws DAOException {
        testEvent.setEndDate(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        int eventId = eventDAO.addEvent(testEvent);
        testEvent.setId(eventId);

        //Test event is already inserted, add invitation
        Invitation testInvitation = createTestInvitation().
                setUser(testUser1)
                .setEventId(testEvent.getId())
                .setUserResponse(new UserResponse(1, "Yes"));
        List<Invitation> invitations = new ArrayList<>();
        invitations.add(testInvitation);
        invitationDAO.addInvitations(invitations);

        List<Event> eventList = eventDAO.getUserParticipatedEvents(testUser1.getId());

        assertNotNull(eventList);
        assertFalse(eventList.isEmpty());
        assertEqualEvents(testEvent, eventList.get(0));
    }

    @Test
    public void getUserAllEvents_Empty_List() throws DAOException {
        List<Event> eventList = eventDAO.getUserAllEvents(testUser2.getId());

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
    }

    @Test
    public void getUserAllEvents_Found() throws DAOException {
        //create invitation for testUser
        Invitation testInvitation = createTestInvitation().
                setUser(testUser1)
                .setEventId(testEvent.getId())
                .setUserResponse(new UserResponse(1, "Yes"));
        List<Invitation> invitations = new ArrayList<>();
        invitations.add(testInvitation);
        invitationDAO.addInvitations(invitations);

        List<Event> eventList = eventDAO.getUserAllEvents(testUser1.getId());

        assertNotNull(eventList);
        assertFalse(eventList.isEmpty());
        assertEqualEvents(testEvent, eventList.get(0));
    }

    @Test
    public void getAllEventsByKeyword_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        List<Event> eventList = eventDAO.getAllEventsByKeyword(EXISTING_KEYWORD);

        assertNotNull(eventList);
        assertFalse(eventList.isEmpty());
        assertEqualEvents(testEvent, eventList.get(0));
    }

    @Test
    public void getPublicEventsByKeyword_EmptyList() throws DAOException, ObjectNotFoundException {
        //delete inserted test event
        eventDAO.deleteAllEvents();

        //create private upcoming event
        testEvent.setPublicAccessed(false);
        int eventId = eventDAO.addEvent(testEvent);
        testEvent.setId(eventId);

        //testing method
        List<Event> eventList = eventDAO.getPublicEventsByKeyword(EXISTING_KEYWORD);

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
    }

    @Test
    public void getPublicEventsByKeyword_Found() throws DAOException, ObjectNotFoundException {
        //testing method (public event in already inserted in setup)
        List<Event> eventList = eventDAO.getPublicEventsByKeyword(EXISTING_KEYWORD);

        assertNotNull(eventList);
        assertFalse(eventList.isEmpty());
        assertEqualEvents(testEvent, eventList.get(0));
    }

    @Test
    public void getAllEventsByKeyword_EmptyList() throws DAOException, ObjectNotFoundException {
        //testing method
        List<Event> eventList = eventDAO.getAllEventsByKeyword(NON_EXISTING_KEYWORD);

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
    }


    @Test
    public void deleteEvent_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        eventDAO.deleteEvent(testEvent.getId());

        Event event = eventDAO.getEventById(testEvent.getId());
        assertNull(event);
    }

    @Test
    public void deleteEvent_Not_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        boolean success = eventDAO.deleteEvent(NON_EXISTING_ID);

        assertFalse(success);
    }

    @Test
    public void deleteAllEvents_Found() throws DAOException {
        //testing method
        eventDAO.deleteAllEvents();

        List<Event> eventList = eventDAO.getAllEvents();

        assertNotNull(eventList);
        assertTrue(eventList.isEmpty());
    }

    @Test
    public void deleteAllEvent_Not_Found() throws DAOException, ObjectNotFoundException {
        //delete inserted event
        eventDAO.deleteEvent(testEvent.getId());

        //testing method
        eventDAO.deleteAllEvents();
    }

    //helper methods
    private void deleteTestObjects() {
        testEvent = null;
        testRecurrenceType = null;
        testCategory = null;
        testUser1 = null;
        testUser2 = null;
    }

    private void deleteTestRecordsFromDB() throws DAOException {
        eventDAO.deleteAllEvents();
        userDAO.deleteAllUsers();
        recurrenceTypeDAO.deleteAllRecurrenceTypes();
        categoryDAO.deleteAllCategories();

    }

}
