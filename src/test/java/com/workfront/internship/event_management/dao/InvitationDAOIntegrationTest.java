package com.workfront.internship.event_management.dao;

/**
 * Created by Hermine Turshujyan 7/9/16.
 */

public class InvitationDAOIntegrationTest {
/*
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
    public void addInvitation_Success() {
        //test invitation already inserted in setup, read record from db
        Invitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertNotNull(invitation);
        assertEqualInvitations(invitation, testInvitation);
    }

    @Test(expected = RuntimeException.class)
    public void addInvitation_Dublicate_Entry() throws DAOException {
        //insert dublicate entry (the same eventId - userId pair)
        invitationDAO.addInvitation(testInvitation);
    }

    @Test
    public void getInvitationById_Found() {
        //test method
        Invitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertNotNull(invitation);
        assertEqualInvitations(invitation, testInvitation);
    }

    @Test
    public void getInvitationById_Not_Found() {
        //test method
        Invitation invitation = invitationDAO.getInvitationById(TestObjectCreator.NON_EXISTING_ID);

        assertNull(invitation);
    }

    @Test
    public void getInvitationsByEventId_Found() {
        //test method
        List<Invitation> invitationList = invitationDAO.getInvitationsByEventId(testEvent.getId());

        assertNotNull(invitationList);
        assertEquals(invitationList.size(), 1);
        assertEqualInvitations(invitationList.get(0), testInvitation);
    }

    @Test
    public void getInvitationsByEventId_Not_Found() {
        //test method
        List<Invitation> invitationList = invitationDAO.getInvitationsByEventId(TestObjectCreator.NON_EXISTING_ID);

        assertNotNull(invitationList);
        assertTrue(invitationList.isEmpty());
    }

    @Test
    public void getInvitationsByUserId_Found() {
        //test method
        List<Invitation> invitationList = invitationDAO.getInvitationsByUserId(testUser.getId());

        assertNotNull(invitationList);
        assertEquals(invitationList.size(), 1);
        assertEqualInvitations(invitationList.get(0), testInvitation);
    }

    @Test
    public void getInvitationsByUserId_Not_Found() {
        //test method
        List<Invitation> invitationList = invitationDAO.getInvitationsByUserId(TestObjectCreator.NON_EXISTING_ID);

        assertNotNull(invitationList);
        assertTrue(invitationList.isEmpty());
    }

    @Test
    public void updateInvitation_Success() {
        //change test invitation object
        testInvitation.setAttendeesCount(10)
                .setUserRole("Organizer");

        //test method
        boolean updated = invitationDAO.updateInvitation(testInvitation);

        //read updated method from db
        Invitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertNotNull(invitation);
        assertEqualInvitations(invitation, testInvitation);
        assertTrue(updated);
    }

    @Test
    public void updateInvitation_Not_Found() {
        //create new invitation with no id
        Invitation newTestInvitation = TestObjectCreator.createTestInvitation();

        //test method
        boolean updated = invitationDAO.updateInvitation(newTestInvitation);

        assertFalse(updated);
    }

    @Test
    public void deleteInvitation_Success() throws DAOException {
        //test method
        boolean deleted = invitationDAO.deleteInvitation(testInvitation.getId());

        Invitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertTrue(deleted);
        assertNull(invitation);
    }

    @Test
    public void deleteInvitation_Not_Found() throws DAOException {
        boolean deleted = invitationDAO.deleteInvitation(TestObjectCreator.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteInvitationsByEventId_Success() throws DAOException {
        //test method
        boolean deleted = invitationDAO.deleteInvitationsByEventId(testEvent.getId());

        List<Invitation> invitationsList = invitationDAO.getInvitationsByEventId(testEvent.getId());

        assertTrue(deleted);
        assertNotNull(invitationsList);
        assertTrue(invitationsList.isEmpty());
    }

    @Test
    public void deleteInvitationsByEventId_Not_Found() throws DAOException {
        boolean deleted = invitationDAO.deleteInvitationsByEventId(TestObjectCreator.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteInvitationsByUserId_Success() throws DAOException {
        //test method
        boolean deleted = invitationDAO.deleteInvitationsByUserId(testUser.getId());

        List<Invitation> invitationsList = invitationDAO.getInvitationsByUserId(testUser.getId());

        assertTrue(deleted);
        assertNotNull(invitationsList);
        assertTrue(invitationsList.isEmpty());
    }

    @Test
    public void deleteInvitationsByUserId_Not_Found() throws DAOException {
        //test method
        boolean deleted = invitationDAO.deleteInvitationsByUserId(TestObjectCreator.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteAllInvitations_Success() throws DAOException {
        //test method
        boolean deleted = invitationDAO.deleteAllInvitations();

        List<Invitation> invitationList = invitationDAO.getAllInvitations();

        assertTrue(deleted);
        assertNotNull(invitationList);
        assertTrue(invitationList.isEmpty());
    }

    @Test
    public void deleteAllInvitations_Not_Found() throws DAOException {
        //delete inserted invitation from db
        invitationDAO.deleteInvitation(testInvitation.getId());

        //test method
        boolean deleted = invitationDAO.deleteAllInvitations();

        assertFalse(deleted);
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
    }


    private void assertEqualInvitations(Invitation expectedInvitation, Invitation actualInvitation) {
        assertEquals(actualInvitation.getId(), expectedInvitation.getId());
        assertEquals(actualInvitation.getEventId(), expectedInvitation.getEventId());
        assertEquals(actualInvitation.getUser().getId(), expectedInvitation.getUser().getId());
        assertEquals(actualInvitation.getUserRole(), expectedInvitation.getUserRole());
        assertEquals(actualInvitation.getUserResponse(), expectedInvitation.getUserResponse());
        assertEquals(actualInvitation.getAttendeesCount(), expectedInvitation.getAttendeesCount());
        assertEquals(actualInvitation.isParticipated(), expectedInvitation.isParticipated());
    }

    private void assertEqualInvitationLists(List<Invitation> expectedList, List<Invitation> actualList) {
        assertEquals(actualList.size(), expectedList.size());
        for (int i = 0; i < actualList.size(); i++) {
            assertEqualInvitations(actualList.get(i), expectedList.get(i));
        }
    }

    private List<Invitation> createTestInvitationsList() throws DAOException {
        //create second test invitation
        Invitation secondTestInvitation = TestObjectCreator.createTestInvitation();

        //insert second category into db
        int invitationId = invitationDAO.addInvitation(secondTestInvitation);
        secondTestInvitation.setId(invitationId);

        //create test media list
        List<Invitation> testInvitationList = new ArrayList<>();
        testInvitationList.add(testInvitation);
        testInvitationList.add(secondTestInvitation);

        return testInvitationList;
    }*/


}
