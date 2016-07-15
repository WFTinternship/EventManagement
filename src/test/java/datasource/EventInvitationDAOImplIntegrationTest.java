package datasource;

import com.workfront.internship.event_management.datasource.*;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventCategory;
import com.workfront.internship.event_management.model.EventInvitation;
import com.workfront.internship.event_management.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by hermine on 7/9/16.
 */

public class EventInvitationDAOImplIntegrationTest {

    private static UserDAO userDAO;
    private static EventCategoryDAO categoryDAO;
    private static EventDAO eventDAO;
    private static EventInvitationDAO invitationDAO;

    private User testUser;
    private EventCategory testCategory;
    private Event testEvent;
    private EventInvitation testInvitation;


    @BeforeClass
    public static void setUpClass() {
        userDAO = new UserDAOImpl();
        categoryDAO = new EventCategoryDAOImpl();
        eventDAO = new EventDAOImpl();
        invitationDAO = new EventInvitationDAOImpl();
    }

    @Before
    public void setUp() {
        createTestObjects();
        insertTestObjectsIntoDB();
    }

    @After
    public void tearDown() {
        deleteAllTestInsertionsFromDB();
        deleteTestObjects();
    }

    @Test
    public void addInvitation_Success() {
        //test invitation already inserted in setup, read record from db
        EventInvitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertNotNull(invitation);
        assertInvitations(invitation, testInvitation);
    }

    @Test(expected = RuntimeException.class)
    public void addInvitation_Dublicate_Entry() {
        //insert dublicate entry (the same eventId - userId pair)
        invitationDAO.addInvitation(testInvitation);
    }

    @Test
    public void getInvitationById_Found() {
        //test method
        EventInvitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertNotNull(invitation);
        assertInvitations(invitation, testInvitation);
    }

    @Test
    public void getInvitationById_Not_Found() {
        //test method
        EventInvitation invitation = invitationDAO.getInvitationById(TestHelper.NON_EXISTING_ID);

        assertNull(invitation);
    }

    @Test
    public void getInvitationsByEventId_Found() {
        //test method
        List<EventInvitation> invitationList = invitationDAO.getInvitationsByEventId(testEvent.getId());

        assertNotNull(invitationList);
        assertEquals(invitationList.size(), 1);
        assertInvitations(invitationList.get(0), testInvitation);
    }

    @Test
    public void getInvitationsByEventId_Not_Found() {
        //test method
        List<EventInvitation> invitationList = invitationDAO.getInvitationsByEventId(TestHelper.NON_EXISTING_ID);

        assertNotNull(invitationList);
        assertTrue(invitationList.isEmpty());
    }

    @Test
    public void getInvitationsByUserId_Found() {
        //test method
        List<EventInvitation> invitationList = invitationDAO.getInvitationsByUserId(testUser.getId());

        assertNotNull(invitationList);
        assertEquals(invitationList.size(), 1);
        assertInvitations(invitationList.get(0), testInvitation);
    }

    @Test
    public void getInvitationsByUserId_Not_Found() {
        //test method
        List<EventInvitation> invitationList = invitationDAO.getInvitationsByUserId(TestHelper.NON_EXISTING_ID);

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
        EventInvitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertNotNull(invitation);
        assertInvitations(invitation, testInvitation);
        assertTrue(updated);
    }

    @Test
    public void updateInvitation_Not_Found() {
        //create new invitation with no id
        EventInvitation newTestInvitation = TestHelper.createTestInvitation();

        //test method
        boolean updated = invitationDAO.updateInvitation(newTestInvitation);

        assertFalse(updated);
    }

    @Test
    public void deleteInvitation_Success() {
        //test method
        boolean deleted = invitationDAO.deleteInvitation(testInvitation.getId());

        EventInvitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertTrue(deleted);
        assertNull(invitation);
    }

    @Test
    public void deleteInvitation_Not_Found() {
        boolean deleted = invitationDAO.deleteInvitation(TestHelper.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteInvitationsByEventId_Success() {
        //test method
        boolean deleted = invitationDAO.deleteInvitationsByEventId(testEvent.getId());

        List<EventInvitation> invitationsList = invitationDAO.getInvitationsByEventId(testEvent.getId());

        assertTrue(deleted);
        assertNotNull(invitationsList);
        assertTrue(invitationsList.isEmpty());
    }

    @Test
    public void deleteInvitationsByEventId_Not_Found() {
        boolean deleted = invitationDAO.deleteInvitationsByEventId(TestHelper.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteInvitationsByUserId_Success() {
        //test method
        boolean deleted = invitationDAO.deleteInvitationsByUserId(testUser.getId());

        List<EventInvitation> invitationsList = invitationDAO.getInvitationsByUserId(testUser.getId());

        assertTrue(deleted);
        assertNotNull(invitationsList);
        assertTrue(invitationsList.isEmpty());
    }

    @Test
    public void deleteInvitationsByUserId_Not_Found() {
        //test method
        boolean deleted = invitationDAO.deleteInvitationsByUserId(TestHelper.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteAllInvitations_Success() {
        //test method
        boolean deleted = invitationDAO.deleteAllInvitations();

        EventInvitation invitation = invitationDAO.getInvitationById(testInvitation.getId());

        assertTrue(deleted);
        assertNull(invitation);
    }

    @Test
    public void deleteAllInvitations_Not_Found() {
        //delete inserted invitation from db
        invitationDAO.deleteInvitation(testInvitation.getId());

        //test method
        boolean deleted = invitationDAO.deleteAllInvitations();

        assertFalse(deleted);
    }


    //private methods

    private void createTestObjects() {
        //create test objects
        testUser = TestHelper.createTestUser();
        testCategory = TestHelper.createTestCategory();
        testEvent = TestHelper.createTestEvent();
        testInvitation = TestHelper.createTestInvitation();
    }

    private void insertTestObjectsIntoDB() {
        //insert user into db and get generated id
        int userId = userDAO.addUser(testUser);
        testUser.setId(userId);

        //insert category into db and get generated id
        int categoryId = categoryDAO.addCategory(testCategory);
        testCategory.setId(categoryId);

        //insert event into db and get generated id
        testEvent.setCategory(testCategory);
        int eventId = eventDAO.insertEvent(testEvent, testUser.getId());
        testEvent.setId(eventId);

        //insert invitation into db and get generated id
        testInvitation.setUser(testUser);
        testInvitation.setEventId(testEvent.getId());
        int invId = invitationDAO.addInvitation(testInvitation);
        testInvitation.setId(invId);
    }

    private void deleteAllTestInsertionsFromDB() {
        invitationDAO.deleteAllInvitations();
        eventDAO.deleteAllEvents();
        categoryDAO.deleteAllCategories();
        userDAO.getAllUsers();
    }

    private void deleteTestObjects() {
        testUser = null;
        testCategory = null;
        testEvent = null;
        testInvitation = null;
    }

    private void assertInvitations(EventInvitation expectedInvitation, EventInvitation actualInvitation) {
        assertEquals(actualInvitation.getId(), expectedInvitation.getId());
        assertEquals(actualInvitation.getEventId(), expectedInvitation.getEventId());
        assertEquals(actualInvitation.getUser().getId(), expectedInvitation.getUser().getId());
        assertEquals(actualInvitation.getUserRole(), expectedInvitation.getUserRole());
        assertEquals(actualInvitation.getUserResponse(), expectedInvitation.getUserResponse());
        assertEquals(actualInvitation.getAttendeesCount(), expectedInvitation.getAttendeesCount());
        assertEquals(actualInvitation.isParticipated(), expectedInvitation.isParticipated());
    }

    private void assertInvitationLists(List<EventInvitation> expectedList, List<EventInvitation> actualList) {
        assertEquals(actualList.size(), expectedList.size());
        for (int i = 0; i < actualList.size(); i++) {
            assertInvitations(actualList.get(i), expectedList.get(i));
        }
    }

    private List<EventInvitation> createTestInvitationsList() {
        //create second test invitation
        EventInvitation secondTestInvitation = TestHelper.createTestInvitation();

        //insert second category into db
        int invitationId = invitationDAO.addInvitation(secondTestInvitation);
        secondTestInvitation.setId(invitationId);

        //create test media list
        List<EventInvitation> testInvitationList = new ArrayList<>();
        testInvitationList.add(testInvitation);
        testInvitationList.add(secondTestInvitation);

        return testInvitationList;
    }


}
