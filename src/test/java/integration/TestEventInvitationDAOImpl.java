package integration;

/**
 * Created by hermine on 7/9/16.
 */
public class TestEventInvitationDAOImpl {

  /*  private static EventInvitationDAO invitationDAO;
    private EventInvitation testInvitation;
    private User testUser;
    private Event testEvent;
    private EventCategory testCategory;

    @BeforeClass
    public static void setUpClass() {
        invitationDAO = new EventInvitationDAOImpl();
    }

    @Before
    public void setUp() {

        testUser = integration.TestHelper.createRandomUser();
        testCategory = integration.TestHelper.createTestCategory();
        testEvent = integration.TestHelper.createTestEvent();
        testInvitation = integration.TestHelper.createTestInvitation();

        int userId = integration.TestHelper.insertTestUserToDB(testUser);
        testUser.setId(userId);

        int categoryId = integration.TestHelper.insertTestCategoryToDB(testCategory);
        testCategory.setId(categoryId);

        testEvent.setCategory(testCategory);
        int eventId = integration.TestHelper.insertTestEventToDB(testEvent);
        testEvent.setId(eventId);

        testInvitation.setUser(testUser);
        testInvitation.setEventId(testEvent.getId());
        int invId = integration.TestHelper.insertTestInvitationToDB(testInvitation);
        testInvitation.setId(invId);
    }

    @After
    public void tearDown() {
        //deleting test records from db
        integration.TestHelper.deleteTestInvitationFromDB(testInvitation.getId());
        integration.TestHelper.deleteTestEventFromDB(testEvent.getId());
        integration.TestHelper.deleteTestUserFromDB(testUser.getId());
        integration.TestHelper.deleteTestCategoryFromDB(testCategory.getId());

        testUser = null;
        testCategory = null;
        testEvent = null;
        testInvitation = null;
    }

    @Test
    public void testInsertInvitation(){
        integration.TestHelper.deleteTestInvitationFromDB(testInvitation.getId());
        int newInvId = invitationDAO.insertInvitation(testInvitation);
        EventInvitation actualInvitation = getTestInvitationFromDB(newInvId);

        try {
            assertEquals(actualInvitation.getEventId(), testInvitation.getEventId());
            assertEquals(actualInvitation.getUserRole(), testInvitation.getUserRole());
            assertEquals(actualInvitation.getUserResponse(), testInvitation.getUserResponse());
            assertEquals(actualInvitation.getAttendeesCount(), testInvitation.getAttendeesCount());
            assertEquals(actualInvitation.isParticipated(), testInvitation.isParticipated());
        } finally {
            integration.TestHelper.deleteTestInvitationFromDB(newInvId);
        }
    }

    @Test //---
    public void testInsertInvitationsList() {
    }

    @Test
    public void testGetInvitationsByEventId() {
        List<EventInvitation> invitations = invitationDAO.getInvitationsByEventId(testEvent.getId());

        assertEquals(invitations.size(), 1);
        EventInvitation actualInvitation = invitations.get(0);
        assertEquals(actualInvitation.getEventId(), testInvitation.getEventId());
        assertEquals(actualInvitation.getUser().getId(), testInvitation.getUser().getId());
        assertEquals(actualInvitation.getUserRole(), testInvitation.getUserRole());
        assertEquals(actualInvitation.getUserResponse(), testInvitation.getUserResponse());
        assertEquals(actualInvitation.getAttendeesCount(), testInvitation.getAttendeesCount());
        assertEquals(actualInvitation.isParticipated(), testInvitation.isParticipated());
    }

    @Test
    public void testGetInvitationById() {
        EventInvitation actualInvitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertEquals(actualInvitation.getEventId(), testInvitation.getEventId());
        assertEquals(actualInvitation.getUser().getId(), testInvitation.getUser().getId());
        assertEquals(actualInvitation.getUserRole(), testInvitation.getUserRole());
        assertEquals(actualInvitation.getUserResponse(), testInvitation.getUserResponse());
        assertEquals(actualInvitation.getAttendeesCount(), testInvitation.getAttendeesCount());
        assertEquals(actualInvitation.isParticipated(), testInvitation.isParticipated());
    }

    @Test
    public void testUpdateInvitation(){
        testInvitation.setUserResponse("Yes").setAttendeesCount(2).setParticipated(true);
        invitationDAO.updateInvitation(testInvitation);
        EventInvitation actualInvitation = getTestInvitationFromDB(testInvitation.getId());

        assertEquals(actualInvitation.getEventId(), testInvitation.getEventId());
        assertEquals(actualInvitation.getUserRole(), testInvitation.getUserRole());
        assertEquals(actualInvitation.getUserResponse(), testInvitation.getUserResponse());
        assertEquals(actualInvitation.getAttendeesCount(), testInvitation.getAttendeesCount());
        assertEquals(actualInvitation.isParticipated(), testInvitation.isParticipated());
    }

    @Test
    public void testDeleteInvitation() {
        invitationDAO.deleteInvitation(testInvitation.getId());
        assertNull(getTestInvitationFromDB(testInvitation.getId()));
    }

    @Test
    public void testDeleteInvitationsByEventId(){
        invitationDAO.deleteInvitationsByEventId(testInvitation.getEventId());
        assertNull(getTestInvitationFromDB(testInvitation.getId()));
    }
*/
}
