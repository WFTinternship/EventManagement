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
import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualEvents;
import static com.workfront.internship.event_management.AssertionHelper.assertEqualInvitations;
import static com.workfront.internship.event_management.TestObjectCreator.createTestInvitation;
import static com.workfront.internship.event_management.TestObjectCreator.createTestUser;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.*;

/**
 * Created by Hermine Turshujyan 7/9/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class InvitationDAOIntegrationTest {

   @Autowired
    private UserDAO userDAO;
    @Autowired
    private CategoryDAO categoryDAO;
    @Autowired
    private EventDAO eventDAO;
    @Autowired
    private InvitationDAO invitationDAO;

    private User testUser;
    private Category testCategory;
    private Event testEvent;
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
    public void addInvitation_Success() {
        //test invitation already inserted in setup, read record from db
        Invitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertNotNull(invitation);
        assertEqualInvitations(invitation, testInvitation);
    }

    @Test(expected = DuplicateEntryException.class)
    public void addInvitation_Duplicate_Entry() throws DAOException, DuplicateEntryException {
        //insert duplicate entry (the same eventId - userId pair)
        invitationDAO.addInvitation(testInvitation);
    }

    @Test
    public void addInvitations_Success() {
        invitationDAO.deleteAllInvitations();

        //add new user for invitation
        User user = createTestUser();
        int userId = userDAO.addUser(user);
        user.setId(userId);

        //add invitations for event
        List<Invitation> testInvitationList = new ArrayList<>();
        Invitation invitation1 = createTestInvitation()
                .setEventId(testEvent.getId())
                .setUser(testUser);
        Invitation invitation2 = createTestInvitation()
                .setEventId(testEvent.getId())
                .setUser(user);

        testInvitationList.add(invitation1);
        testInvitationList.add(invitation2);

        //method under test
        invitationDAO.addInvitations(testInvitationList);
        List<Invitation> invitationList = invitationDAO.getInvitationsByEventId(testEvent.getId());

        assertNotNull(invitationList);
        assertFalse(invitationList.isEmpty());
        assertEquals(invitationList.size(), 2);
    }


    @Test
    public void getInvitationById_Found() throws DAOException, ObjectNotFoundException {
        //test method
        Invitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertNotNull(invitation);
        assertEqualInvitations(invitation, testInvitation);
    }

    @Test
    public void getInvitationById_Not_Found() throws DAOException, ObjectNotFoundException {
        //test method
        Invitation invitation = invitationDAO.getInvitationById(TestObjectCreator.NON_EXISTING_ID);

        assertNull(invitation);
    }

    @Test
    public void getInvitationsByEventId_Found() throws DAOException {
        //test method
        List<Invitation> invitationList = invitationDAO.getInvitationsByEventId(testEvent.getId());

        assertNotNull(invitationList);
        assertEquals(invitationList.size(), 1);
        assertEqualInvitations(invitationList.get(0), testInvitation);
    }

    @Test
    public void getInvitationsByEventId_Not_Found() throws DAOException {
        //test method
        List<Invitation> invitationList = invitationDAO.getInvitationsByEventId(TestObjectCreator.NON_EXISTING_ID);

        assertNotNull(invitationList);
        assertTrue(invitationList.isEmpty());
    }

    @Test
    public void getInvitationsByUserId_Found() throws DAOException {
        //test method
        List<Invitation> invitationList = invitationDAO.getInvitationsByUserId(testUser.getId());

        assertNotNull(invitationList);
        assertEquals(invitationList.size(), 1);
        assertEqualInvitations(invitationList.get(0), testInvitation);
    }

    @Test
    public void getInvitationsByUserId_Not_Found() throws DAOException {
        //test method
        List<Invitation> invitationList = invitationDAO.getInvitationsByUserId(TestObjectCreator.NON_EXISTING_ID);

        assertNotNull(invitationList);
        assertTrue(invitationList.isEmpty());
    }

    @Test
    public void updateInvitation_Success() throws DuplicateEntryException, ObjectNotFoundException, DAOException {
        //change test invitation object
        testInvitation.setAttendeesCount(10);

        //test method
        invitationDAO.updateInvitation(testInvitation);

        //read updated method from db
        Invitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertNotNull(invitation);
        assertEqualInvitations(invitation, testInvitation);
    }
    @Test
    public void updateInvitationResponse_Success() throws DuplicateEntryException, ObjectNotFoundException, DAOException {
        testInvitation.setUserResponse(new UserResponse(3, "Maybe"));
        //test method
        invitationDAO.updateInvitationResponse(testEvent.getId(), testUser.getId(), testInvitation.getUserResponse().getId());

        //read updated method from db
        Invitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertNotNull(invitation);
        assertEqualInvitations(invitation, testInvitation);
    }

    @Test
    public void updateInvitation_Not_Found() throws DuplicateEntryException, ObjectNotFoundException, DAOException {
        //create new invitation with no id
        Invitation newTestInvitation = createTestInvitation();

        //test method
        boolean success = invitationDAO.updateInvitation(newTestInvitation);
        assertFalse(success);
    }

    @Test
    public void deleteInvitation_Success() throws DAOException, ObjectNotFoundException {
        //test method
        boolean success = invitationDAO.deleteInvitation(testInvitation.getId());

        Invitation invitation = invitationDAO.getInvitationById(testInvitation.getId());
        assertTrue(success);
        assertNull(invitation);
    }

    @Test
    public void deleteInvitation_Not_Found() throws DAOException, ObjectNotFoundException {
        boolean success = invitationDAO.deleteInvitation(TestObjectCreator.NON_EXISTING_ID);

        assertFalse(success);
    }

    @Test
    public void deleteInvitationsByEventId_Success() throws DAOException {
        //test method
        invitationDAO.deleteInvitationsByEventId(testEvent.getId());

        List<Invitation> invitationsList = invitationDAO.getInvitationsByEventId(testEvent.getId());

        assertNotNull(invitationsList);
        assertTrue(invitationsList.isEmpty());
    }

    @Test
    public void deleteInvitationsByEventId_Not_Found() throws DAOException {
        invitationDAO.deleteInvitationsByEventId(TestObjectCreator.NON_EXISTING_ID);
    }

    @Test
    public void deleteInvitationsByUserId_Success() throws DAOException {
        //test method
        invitationDAO.deleteInvitationsByUserId(testUser.getId());

        List<Invitation> invitationsList = invitationDAO.getInvitationsByUserId(testUser.getId());

        assertNotNull(invitationsList);
        assertTrue(invitationsList.isEmpty());
    }

    @Test
    public void deleteInvitationsByUserId_Not_Found() throws DAOException {
        //test method
        invitationDAO.deleteInvitationsByUserId(TestObjectCreator.NON_EXISTING_ID);
    }

    @Test
    public void deleteAllInvitations_Success() throws DAOException {
        //test method
        invitationDAO.deleteAllInvitations();

        List<Invitation> invitationList = invitationDAO.getAllInvitations();

        assertNotNull(invitationList);
        assertTrue(invitationList.isEmpty());
    }

    //private methods

    private void insertTestObjectsIntoDB() throws DAOException, DuplicateEntryException {
        //insert user into db and get generated id
        int userId = userDAO.addUser(testUser);
        testUser.setId(userId);

        //insert category into db and get generated id
        int categoryId = categoryDAO.addCategory(testCategory);
        testCategory.setId(categoryId);

        //insert event into db and get generated id
        testEvent.setCategory(testCategory).setOrganizer(testUser);
        int eventId = eventDAO.addEvent(testEvent);
        testEvent.setId(eventId);

        //insert invitation into db and get generated id
        testInvitation.setUser(testUser);
        testInvitation.setEventId(testEvent.getId());
        int invId = invitationDAO.addInvitation(testInvitation);
        testInvitation.setId(invId);
    }

    private void createTestObjects() {
        testUser = TestObjectCreator.createTestUser();
        testCategory = TestObjectCreator.createTestCategory();
        testEvent = TestObjectCreator.createTestEvent();
        testInvitation = createTestInvitation();
    }

    private void deleteTestRecordsFromDB() throws DAOException {
        invitationDAO.deleteAllInvitations();
        eventDAO.deleteAllEvents();
        categoryDAO.deleteAllCategories();
        userDAO.getAllUsers();
    }

    private void deleteTestObjects() {
        testInvitation = null;
        testEvent = null;
        testCategory = null;
        testUser = null;
    }
}
