package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.Invitation;
import com.workfront.internship.event_management.model.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Created by Hermine Turshujyan 7/9/16.
 */

public class InvitationDAOIntegrationTest {

    private static UserDAO userDAO;
    private static CategoryDAO categoryDAO;
    private static EventDAO eventDAO;
    private static InvitationDAO invitationDAO;

    private User testUser;
    private Category testCategory;
    private Event testEvent;
    private Invitation testInvitation;


    @BeforeClass
    public static void setUpClass() throws DAOException {
        userDAO = new UserDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        eventDAO = new EventDAOImpl();
        invitationDAO = new InvitationDAOImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        userDAO = null;
        categoryDAO = null;
        eventDAO = null;
        invitationDAO = null;
    }
/*
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
    public void addInvitation_Success() throws DAOException, ObjectNotFoundException {
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

    @Ignore
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

    @Ignore
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

    @Ignore
    @Test
    public void updateInvitation_Success() throws DuplicateEntryException, ObjectNotFoundException, DAOException {
        //change test invitation object
        testInvitation.setAttendeesCount(10)
                .setUserRole("Organizer");

        //test method
        invitationDAO.updateInvitation(testInvitation);

        //read updated method from db
        Invitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertNotNull(invitation);
        assertEqualInvitations(invitation, testInvitation);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void updateInvitation_Not_Found() throws DuplicateEntryException, ObjectNotFoundException, DAOException {
        //create new invitation with no id
        Invitation newTestInvitation = TestObjectCreator.createTestInvitation();

        //test method
        invitationDAO.updateInvitation(newTestInvitation);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteInvitation_Success() throws DAOException, ObjectNotFoundException {
        //test method
        invitationDAO.deleteInvitation(testInvitation.getId());

        Invitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertNull(invitation);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteInvitation_Not_Found() throws DAOException, ObjectNotFoundException {
        invitationDAO.deleteInvitation(TestObjectCreator.NON_EXISTING_ID);
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
        //insert user info into db and get generated id
        int userId = userDAO.addUser(testUser);
        testUser.setId(userId);

        //insert category info into db and get generated id
        int categoryId = categoryDAO.addCategory(testCategory);
        testCategory.setId(categoryId);

        //insert event into db and get generated id
        testEvent.setCategory(testCategory);
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
        testInvitation = TestObjectCreator.createTestInvitation();
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
    }*/
}
