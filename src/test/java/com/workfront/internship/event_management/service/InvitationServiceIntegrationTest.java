package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.Invitation;
import com.workfront.internship.event_management.model.User;
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

import static com.workfront.internship.event_management.AssertionHelper.assertEqualInvitations;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.*;

/**
 * Created by Hermine Turshujyan 8/29/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class InvitationServiceIntegrationTest {

    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    InvitationService invitationService;

    private Event testEvent;
    private User testUser;
    private Category testCategory;
    private Invitation testInvitation;

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
    public void addInvitation() {
        //test invitation already inserted in setup, read record from db
        Invitation invitation = invitationService.getInvitationById(testInvitation.getId());

        assertNotNull(invitation);
        assertEqualInvitations(invitation, testInvitation);
    }

    @Test
    public void getInvitationById() throws DAOException, ObjectNotFoundException {
        //test method
        Invitation invitation = invitationService.getInvitationById(testInvitation.getId());

        assertNotNull(invitation);
        assertEqualInvitations(invitation, testInvitation);
    }

    @Test
    public void getInvitationsByEventId() {
        //test method
        List<Invitation> invitationList = invitationService.getInvitationsByEvent(testEvent.getId());

        assertNotNull(invitationList);
        assertEquals(invitationList.size(), 1);
        assertEqualInvitations(invitationList.get(0), testInvitation);
    }


    @Test
    public void getInvitationsByUserId() {
        //test method
        List<Invitation> invitationList = invitationService.getInvitationsByUser(testUser.getId());

        assertNotNull(invitationList);
        assertEquals(invitationList.size(), 1);
        assertEqualInvitations(invitationList.get(0), testInvitation);
    }


    @Test
    public void updateInvitation() {
        //change test invitation object
        testInvitation.setAttendeesCount(10)
                .setUserRole("Organizer");

        //test method
        invitationService.editInvitation(testInvitation);

        //read updated method from db
        Invitation invitation = invitationService.getInvitationById(testInvitation.getId());

        assertNotNull(invitation);
        assertEqualInvitations(invitation, testInvitation);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteInvitation() {
        //test method
        boolean success = invitationService.deleteInvitation(testInvitation.getId());
        assertTrue(success);

        Invitation invitation = invitationService.getInvitationById(testInvitation.getId());
        assertNull(invitation);
    }

    public void deleteInvitationsByEventId() {
        //test method
        invitationService.deleteInvitationsByEvent(testEvent.getId());

        List<Invitation> invitationsList = invitationService.getInvitationsByEvent(testEvent.getId());

        assertNotNull(invitationsList);
        assertTrue(invitationsList.isEmpty());
    }

    @Test
    public void deleteInvitationsByUserId() {
        //test method
        invitationService.deleteInvitationsByUser(testUser.getId());

        List<Invitation> invitationsList = invitationService.getInvitationsByUser(testUser.getId());

        assertNotNull(invitationsList);
        assertTrue(invitationsList.isEmpty());
    }

    @Test
    public void deleteAllInvitations() {
        //test method
        invitationService.deleteAllInvitations();

        List<Invitation> invitationList = invitationService.getAllInvitations();

        assertNotNull(invitationList);
        assertTrue(invitationList.isEmpty());
    }

    //private methods

    private void insertTestObjectsIntoDB() throws DAOException, DuplicateEntryException {
        //insert user info into db and get generated id
        testUser = userService.addAccount(testUser);

        //insert category info into db and get generated id
        testCategory = categoryService.addCategory(testCategory);

        //insert event into db and get generated id
        testEvent.setCategory(testCategory);
        testEvent = eventService.createEvent(testEvent);

        //insert invitation into db and get generated id
        testInvitation.setUser(testUser);
        testInvitation.setEventId(testEvent.getId());
        testInvitation = invitationService.addInvitation(testInvitation);
    }

    private void createTestObjects() {
        testUser = createTestUser();
        testCategory = createTestCategory();
        testEvent = createTestEvent();
        testInvitation = createTestInvitation();
    }

    private void deleteTestRecordsFromDB() throws DAOException {
        invitationService.deleteAllInvitations();
        eventService.deleteAllEvents();
        categoryService.deleteAllCategories();
        userService.getAllUsers();
    }

    private void deleteTestObjects() {
        testInvitation = null;
        testEvent = null;
        testCategory = null;
        testUser = null;
    }
}
