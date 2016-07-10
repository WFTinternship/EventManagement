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
private User testUser1, testUser2;
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

            insertTestUser(testUser);
            testUser.setId(getTestUserId(testUser.getUsername()));

            insertTestCategory(testCategory);
            testCategory.setId(getTestCategoryId(testCategory.getTitle()));

            insertTestEvent(testEvent);
            testEvent.setId(getTestEventId(testEvent.getTitle()));

            insertTestInvitation(testInvitation, testUser, testEvent);
            testInvitation.setEventId(testEvent.getId()).setUser(testUser);

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
            conn.setAutoCommit(false);
            if(testUser1 != null && testUser2 != null) {
                deleteTestUser(testUser1);
                deleteTestUser(testUser2);
                testUser1 = null;
                testUser2 = null;
            }
            deleteTestInvitations(testInvitation);
            deleteTestEvent(testEvent);
            deleteTestUser(testUser);
            deleteTestCategory(testCategory);
            testUser = null;
            testCategory = null;
            testEvent = null;
            testInvitation = null;
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    @Test
    public void testInsertInvitation() throws SQLException {
       deleteTestInvitations(testInvitation);
        invitationDAO.insertInvitation(testInvitation);
        EventInvitation actualInvitation = getTestInvitations().get(0);
        assertEquals(actualInvitation.getEventId(), testInvitation.getEventId());
        assertEquals(actualInvitation.getUser().getId(), testInvitation.getUser().getId());
        assertEquals(actualInvitation.getUserRole(), testInvitation.getUserRole());
        assertEquals(actualInvitation.getUserResponse(), "Undefined");
        assertEquals(actualInvitation.getAttendeesCount(), 1);
        assertFalse(actualInvitation.isRealParticipation());

    }

    @Test //----
    public void testInsertInvitationsList() throws SQLException {
        deleteTestInvitations(testInvitation);
        deleteTestUser(testUser);

        testUser1 = new User(testUser).setUsername("new_username1").setEmail("new_email1@test.com");
        testUser2 = new User(testUser).setUsername("new_username2").setEmail("new_email2@test.com");
        insertTestUser(testUser1);
        insertTestUser(testUser2);
        testUser1.setId(getTestUserId(testUser1.getUsername()));
        testUser2.setId(getTestUserId(testUser2.getUsername()));

        List<EventInvitation> testInvitationsList = TestUtil.setUpTestInvitationsList(testUser1, testUser2, testEvent);
        invitationDAO.insertInvitationsList(testInvitationsList) ;

        List<EventInvitation> actualInvitationsList = getTestInvitations();
        assertEquals(actualInvitationsList.size(), testInvitationsList.size());
        for (int i = 0; i < actualInvitationsList.size(); i++) {
            assertEquals(actualInvitationsList.get(i).getEventId(), testInvitationsList.get(i).getEventId());
            assertEquals(actualInvitationsList.get(i).getUser().getId(), testInvitationsList.get(i).getUser().getId());
            assertEquals(actualInvitationsList.get(i).getUserRole(), testInvitationsList.get(i).getUserRole());
            assertEquals(actualInvitationsList.get(i).getUserResponse(), "Undefined");
            assertEquals(actualInvitationsList.get(i).getAttendeesCount(), 1);
            assertFalse(actualInvitationsList.get(i).isRealParticipation());
        }
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
        EventInvitation actualInvitation = getTestInvitations().get(0);
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
         assertTrue(getTestInvitations().isEmpty());

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

    private void insertTestUser(User user) throws SQLException {
        String sqlStr = "INSERT INTO user "
                + "(first_name, last_name, username, password, "
                + "email, phone_number, avatar_path) VALUES "
                + "(?, ?, ?, ?, ?, ?, ?)";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setString(1, user.getFirstName());
        stmt.setString(2, user.getLastName());
        stmt.setString(3, user.getUsername());
        stmt.setString(4, user.getPassword());
        stmt.setString(5, user.getEmail());
        stmt.setString(6, user.getPhoneNumber());
        stmt.setString(7, user.getAvatarPath());
        stmt.executeUpdate();
    }

    private void insertTestEvent(Event event) throws SQLException {
        String insertEvent = "INSERT INTO event "
                + "(title, short_desc, full_desc, location, lat, lng, file_path, image_path, "
                + "category_id) VALUES "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        stmt = conn.prepareStatement(insertEvent);
        stmt.setString(1, event.getTitle());
        stmt.setString(2, event.getShortDesc());
        stmt.setString(3, event.getFullDesc());
        stmt.setString(4, event.getLocation());
        stmt.setFloat(5, event.getLat());
        stmt.setFloat(6, event.getLng());
        stmt.setString(7, event.getFilePath());
        stmt.setString(8, event.getImagePath());
        stmt.setInt(9, event.getCategory().getId());
        stmt.executeUpdate();
    }

    private void insertTestCategory(EventCategory category) throws SQLException {
        String sqlStr = "INSERT INTO event_category "
                + "(title, description) "
                + "VALUES (?, ?)";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setString(1, category.getTitle());
        stmt.setString(2, category.getDescription());
        stmt.executeUpdate();
    }

    private void insertTestInvitation(EventInvitation invitation, User user, Event event) throws SQLException {
        String sqlStr = "INSERT INTO event_invitation "
                + "(event_id, user_id, user_role) "
                + "VALUES (?, ?, ?)";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, event.getId());
        stmt.setInt(2, user.getId());
        stmt.setString(3, invitation.getUserRole());
        stmt.executeUpdate();
    }


    private void deleteTestInvitations(EventInvitation invitation) throws SQLException {
        String sqlStr = "DELETE FROM event_invitation WHERE event_id = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, invitation.getEventId());
        stmt.executeUpdate();
    }

    private void deleteTestUser(User user) throws SQLException {
        String sqlStr = "DELETE FROM user WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
        preparedStatement.setInt(1, user.getId());
        preparedStatement.executeUpdate();
    }

    private void deleteTestCategory(EventCategory category) throws SQLException {
        String sqlStr = "DELETE FROM event_category WHERE id = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, category.getId());
        stmt.executeUpdate();
    }

    private void deleteTestEvent(Event event) throws SQLException {
        String sqlStr = "DELETE FROM event WHERE id = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, event.getId());
        stmt.executeUpdate();
    }

    private List<EventInvitation> getTestInvitations() throws SQLException {
        String sqlStr = "SELECT * FROM event_invitation LEFT JOIN USER " +
                "ON user.id = event_invitation.user_id WHERE event_id  = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, testInvitation.getEventId());
        rs = stmt.executeQuery();
        List<EventInvitation>  invitationsList = new ArrayList<EventInvitation>();
        while (rs.next()) {
            EventInvitation  invitation = new EventInvitation();
            User actualUser = new User();
            actualUser.setId(rs.getInt("user.id"))
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

    private int getTestUserId(String username) throws SQLException {
        String sqlStr = "SELECT id FROM user WHERE username  = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setString(1, username);
        rs = stmt.executeQuery();
        int userId = 0;
        while (rs.next()) {
            userId = rs.getInt("id");
        }
        return userId;
    }

    private int getTestCategoryId(String categoryTitle) throws SQLException {
        String sqlStr = "SELECT * FROM event_category WHERE title  = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setString(1, categoryTitle);
        rs = stmt.executeQuery();
        int categoryId  = 0;
        while (rs.next()) {
            categoryId = rs.getInt("id");
        }
        return categoryId;
    }

    private int getTestEventId(String title) throws SQLException {
        String sqlStr = "SELECT id FROM event WHERE title  = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setString(1, title);
        rs = stmt.executeQuery();
        int eventId = 0;
        while (rs.next()) {
            eventId = rs.getInt("id");
        }
        return eventId;
    }
}
