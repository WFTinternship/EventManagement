import com.workfront.internship.event_management.datasource.DataSourceManager;
import com.workfront.internship.event_management.datasource.EventInvitationDAO;
import com.workfront.internship.event_management.datasource.EventInvitationDAOImpl;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventCategory;
import com.workfront.internship.event_management.model.EventInvitation;
import com.workfront.internship.event_management.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by hermine on 7/9/16.
 */
public class TestEventInvitationDAOImpl {

    private EventInvitationDAO invitationDAO = new EventInvitationDAOImpl();
    private EventInvitation testInvitation;
    private User testUser;
    private Event testEvent;
    private EventCategory testCategory;
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;

    @Before
    public void setUp() {
        testUser = TestUtil.setUpTestUser();
        testCategory = TestUtil.setUpTestCategory();
        testEvent = TestUtil.setUpTestEvent(testCategory);
        testInvitation = TestUtil.setUpTestInvitation(testUser, testEvent);
        try {
            conn = DataSourceManager.getInstance().getConnection();
            conn.setAutoCommit(false);
            insertTestUser();
            insertTestCategory();
            insertTestEvent();
            insertTestInvitation();
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @After
    public void tearDown() {
        try {
            deleteTestInvitations();
            deleteTestEvent();
            deleteTestUser();
            deleteTestCategory();
            testUser = null;
            testCategory = null;
            testEvent = null;
            testInvitation = null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
    }


    @Test
    public void testInsertInvitation() throws SQLException {
        deleteTestInvitations();
        invitationDAO.insertInvitation(testInvitation);
        EventInvitation actualInvitation = getTestInvitationsFromDB().get(0);
        assertEquals(actualInvitation.getEventId(), testInvitation.getEventId());
        assertEquals(actualInvitation.getUser().getId(), testInvitation.getUser().getId());
        assertEquals(actualInvitation.getUserRole(), testInvitation.getUserRole());
        assertEquals(actualInvitation.getUserResponse(), "Undefined");
        assertEquals(actualInvitation.getAttendeesCount(), 1);
        assertFalse(actualInvitation.isRealParticipation());

    }

    @Test
    public void testInsertInvitationsList() throws SQLException {
        User user1 = new User(testUser);
        user1.setId(222222);
        User user2 = new User(testUser);
        user2.setId(333333);
        List<EventInvitation> testInvitationsList = TestUtil.setUpTestInvitationsList(user1, user2, testEvent);
        invitationDAO.insertInvitationsList(testInvitationsList);
        List<EventInvitation> actualInvitations = getTestInvitationsFromDB();
    }

    @Test
    public void testGetInvitationsByEventId() {
        List<EventInvitation> invitations = invitationDAO.getInvitationsByEventId(testEvent.getId());
        assertEquals(invitations.size(), 1);
        EventInvitation actualInvitation = invitations.get(0);
        assertEquals(actualInvitation.getEventId(), testInvitation.getEventId());
        assertEquals(actualInvitation.getUser().getId(), testInvitation.getUser().getId());
        assertEquals(actualInvitation.getUserRole(), testInvitation.getUserRole());
        assertEquals(actualInvitation.getUserResponse(), "Undefined");
        assertEquals(actualInvitation.getAttendeesCount(), 1);
        assertFalse(actualInvitation.isRealParticipation());
    }

    @Test
    public void testUpdateInvitation() throws SQLException {
        EventInvitation expectedInvitation = new EventInvitation(testInvitation);
        expectedInvitation.setUserResponse("Yes").setAttendeesCount(2).setRealParticipation(true);
        invitationDAO.updateInvitation(expectedInvitation);
        EventInvitation actualInvitation = getTestInvitationsFromDB().get(0);
        assertEquals(actualInvitation.getEventId(), expectedInvitation.getEventId());
        assertEquals(actualInvitation.getUser().getId(), expectedInvitation.getUser().getId());
        assertEquals(actualInvitation.getUserRole(), expectedInvitation.getUserRole());
        assertEquals(actualInvitation.getUserResponse(), expectedInvitation.getUserResponse());
        assertEquals(actualInvitation.getAttendeesCount(), expectedInvitation.getAttendeesCount());
        assertEquals(actualInvitation.isRealParticipation(), expectedInvitation.isRealParticipation());
    }

    @Test
    public void testDeleteInvitation() throws SQLException {
        invitationDAO.deleteInvitation(testInvitation.getEventId(), testInvitation.getUser().getId());
         assertTrue(getTestInvitationsFromDB().isEmpty());

    }

    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }
    }

    private void insertTestUser() throws SQLException {
        String sqlStr = "INSERT INTO user "
                + "(id, first_name, last_name, username, password, "
                + "email, phone_number, avatar_path) VALUES "
                + "(?, ?, ?, ?, ?, ?, ?, ?)";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, testUser.getId());
        stmt.setString(2, testUser.getFirstName());
        stmt.setString(3, testUser.getLastName());
        stmt.setString(4, testUser.getUsername());
        stmt.setString(5, testUser.getPassword());
        stmt.setString(6, testUser.getEmail());
        stmt.setString(7, testUser.getPhoneNumber());
        stmt.setString(8, testUser.getAvatarPath());
        stmt.executeUpdate();
    }

    private void insertTestEvent() throws SQLException {
        String insertEvent = "INSERT INTO event "
                + "(id, title, short_desc, full_desc, location, lat, lng, file_path, image_path, "
                + "category_id, public_access, guests_allowed) VALUES "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?)";
        stmt = conn.prepareStatement(insertEvent);
        stmt.setInt(1, testEvent.getId());
        stmt.setString(2, testEvent.getTitle());
        stmt.setString(3, testEvent.getShortDesc());
        stmt.setString(4, testEvent.getFullDesc());
        stmt.setString(5, testEvent.getLocation());
        stmt.setFloat(6, testEvent.getLat());
        stmt.setFloat(7, testEvent.getLng());
        stmt.setString(8, testEvent.getFilePath());
        stmt.setString(9, testEvent.getImagePath());
        stmt.setInt(10, testEvent.getCategory().getId());
        stmt.setBoolean(11, testEvent.isPublicAccess());
        stmt.setBoolean(12, testEvent.isGuestsAllowed());
        stmt.executeUpdate();
    }

    private void insertTestCategory() throws SQLException {
        String sqlStr = "INSERT INTO event_category "
                + "(id, title, description) "
                + "VALUES (?, ?, ?)";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, testCategory.getId());
        stmt.setString(2, testCategory.getTitle());
        stmt.setString(3, testCategory.getDescription());
        stmt.executeUpdate();
    }

    private void insertTestInvitation() throws SQLException {
        String sqlStr = "INSERT INTO event_invitation "
                + "(event_id, user_id, user_role) "
                + "VALUES (?, ?, ?)";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, testInvitation.getEventId());
        stmt.setInt(2, testInvitation.getUser().getId());
        stmt.setString(3, testInvitation.getUserRole());
        stmt.executeUpdate();
    }


    private void deleteTestInvitations() throws SQLException {
        String sqlStr = "DELETE FROM event_invitation WHERE event_id = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, testInvitation.getEventId());
        stmt.executeUpdate();
    }

    private void deleteTestUser() throws SQLException {
        String sqlStr = "DELETE FROM user WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
        preparedStatement.setInt(1, testUser.getId());
        preparedStatement.executeUpdate();
    }

    private void deleteTestCategory() throws SQLException {
        String sqlStr = "DELETE FROM event_category WHERE id = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, testCategory.getId());
        stmt.executeUpdate();
    }

    private void deleteTestEvent() throws SQLException {
        String sqlStr = "DELETE FROM event WHERE id = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, testEvent.getId());
        stmt.executeUpdate();
    }


    private List<EventInvitation> getTestInvitationsFromDB() throws SQLException {
        String sqlStr = "SELECT * FROM event_invitation LEFT JOIN USER " +
                "ON user.id = event_invitation.user_id WHERE event_id  = ? AND user_id = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, testInvitation.getEventId());
        stmt.setInt(2, testInvitation.getUser().getId());
        rs = stmt.executeQuery();
        List<EventInvitation>  invitationsList = new ArrayList<EventInvitation>();
        while (rs.next()) {
            EventInvitation  invitation = new EventInvitation();
            User actualUser = new User();
            actualUser.setId(rs.getInt("id"))
                    .setFirstName(rs.getString("first_name"))
                    .setLastName(rs.getString("last_name"))
                    .setUsername(rs.getString("username"))
                    .setPassword(rs.getString("password"))
                    .setEmail(rs.getString("email"))
                    .setAvatarPath(rs.getString("avatar_path"))
                    .setPhoneNumber(rs.getString("phone_number"))
                    .setVerified(rs.getBoolean("verified"))
                    .setRegistrationDate(rs.getTimestamp("registration_date"));
            invitation.setEventId(rs.getInt("event_id"))
                    .setUser(actualUser)
                    .setUserRole(rs.getString("user_role"))
                    .setUserResponse(rs.getString("user_response"))
                    .setAttendeesCount(rs.getInt("attendees_count"))
                    .setRealParticipation(rs.getBoolean("real_participation"));
            invitationsList.add(invitation);
        }
        return invitationsList;
    }
}
