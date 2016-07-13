import com.workfront.internship.event_management.datasource.DataSourceManager;
import com.workfront.internship.event_management.datasource.EventInvitationDAO;
import com.workfront.internship.event_management.datasource.EventInvitationDAOImpl;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventCategory;
import com.workfront.internship.event_management.model.EventInvitation;
import com.workfront.internship.event_management.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by hermine on 7/9/16.
 */
public class TestEventInvitationDAOImpl {

    private static EventInvitationDAO invitationDAO;
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

        testUser = TestHelper.createTestUser();
        testCategory = TestHelper.createTestCategory();
        testEvent = TestHelper.createTestEvent();
        testInvitation = TestHelper.createTestInvitation();

        int userId = TestHelper.insertTestUser(testUser);
        testUser.setId(userId);

        int categoryId = TestHelper.insertTestCategory(testCategory);
        testCategory.setId(categoryId);

        testEvent.setCategory(testCategory);
        int eventId = TestHelper.insertTestEvent(testEvent);
        testEvent.setId(eventId);

        testInvitation.setUser(testUser);
        testInvitation.setEventId(testEvent.getId());
        int invId = TestHelper.insertTestInvitation(testInvitation);
        testInvitation.setId(invId);
    }

    @After
    public void tearDown() {
        TestHelper.deleteTestInvitation(testInvitation.getId());
        TestHelper.deleteTestEvent(testEvent.getId());
        TestHelper.deleteTestUser(testUser.getId());
        TestHelper.deleteTestCategory(testCategory.getId());
        testUser = null;
        testCategory = null;
        testEvent = null;
        testInvitation = null;
    }

    @Test
    public void testInsertInvitation() throws SQLException {
        TestHelper.deleteTestInvitation(testInvitation.getId());
        invitationDAO.insertInvitation(testInvitation);
        EventInvitation actualInvitation = getTestInvitationFromDB(testInvitation.getId() + 1);
        try {
            assertEquals(actualInvitation.getEventId(), testInvitation.getEventId());
            assertEquals(actualInvitation.getUserRole(), testInvitation.getUserRole());
            assertEquals(actualInvitation.getUserResponse(), testInvitation.getUserResponse());
            assertEquals(actualInvitation.getAttendeesCount(), testInvitation.getAttendeesCount());
            assertEquals(actualInvitation.isParticipated(), testInvitation.isParticipated());
        } finally {
            TestHelper.deleteTestInvitation(testInvitation.getId() + 1);
        }
    }

    @Test
    public void testInsertInvitationsList() throws SQLException {
//        TestHelper.deleteTestInvitation(testInvitation.getEventId(), testUser.getId());
//        //TestHelper.deleteTestUser(testUser.getId());
//
//        User testUser1 = TestHelper.createTestUser().setUsername("new_username1").setEmail("new_email1@test.com");
//        User testUser2 = TestHelper.createTestUser().setUsername("new_username2").setEmail("new_email2@test.com");
//        int userId1 = TestHelper.insertTestUser(testUser1);
//        int userId2 = TestHelper.insertTestUser(testUser2);
//        testUser1.setId(userId1);
//        testUser2.setId(userId2);
//
//        List<EventInvitation> testInvitationsList = TestHelper.createTestInvitationsList(testUser1, testUser2, testEvent);
//        invitationDAO.insertInvitations(testInvitationsList);
//
//        List<EventInvitation> actualInvitationsList = getTestInvitations();
//        try {
//            assertEquals(actualInvitationsList.size(), testInvitationsList.size());
//            for (int i = 0; i < actualInvitationsList.size(); i++) {
//                assertEquals(actualInvitationsList.get(i).getEventId(), testInvitationsList.get(i).getEventId());
//                assertEquals(actualInvitationsList.get(i).getUserRole(), testInvitationsList.get(i).getUserRole());
//            }
//        } finally {
//            TestHelper.deleteTestUser(testUser1.getId());
//            TestHelper.deleteTestUser(testUser2.getId());
//        }
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
    public void testUpdateInvitation() throws SQLException {
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
    public void testDeleteInvitation() throws SQLException {
        invitationDAO.deleteInvitation(testInvitation.getId());
        assertNull(getTestInvitationFromDB(testInvitation.getId()));
    }

    @Test
    public void testDeleteInvitationsByEventId() throws SQLException {
        invitationDAO.deleteInvitationsByEventId(testInvitation.getEventId());
        assertNull(getTestInvitationFromDB(testInvitation.getId()));
    }

    //helper methods
    private EventInvitation getTestInvitationFromDB(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        EventInvitation invitation = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();

            String sqlStr = "SELECT * FROM event_invitation WHERE id  = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                invitation = new EventInvitation();
                invitation.setId(rs.getInt("id"))
                        .setEventId(rs.getInt("event_id"))
                        .setUserRole(rs.getString("user_role"))
                        .setUserResponse(rs.getString("user_response"))
                        .setAttendeesCount(rs.getInt("attendees_count"))
                        .setParticipated(rs.getBoolean("participated"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            TestHelper.closeResources(rs, stmt, conn);
        }
        return invitation;
    }


}
