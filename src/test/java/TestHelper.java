import com.workfront.internship.event_management.datasource.DataSourceManager;
import com.workfront.internship.event_management.model.*;
import com.workfront.internship.event_management.model.datehelpers.RecurrenceType;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hermine on 7/10/16.
 */
public class TestHelper {

    public static void closeResources(ResultSet rs, Statement stmt, Connection conn) {
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

    //test object initializers
    public static User createTestUser() {
        User testUser = new User();
        java.util.Date currentDate = new java.util.Date();
        testUser.setFirstName("Test FirstName")
                .setLastName("Test LastName")
                .setUsername("Test Username")
                .setPassword("Test Password")
                .setEmail("test-test@test.com")
                .setPhoneNumber("1111111")
                .setAvatarPath("/users/test_user_av.jpg")
                .setVerified(false)
                .setRegistrationDate(currentDate);
        return testUser;
    }

    public static EventInvitation createTestInvitation() {
        EventInvitation invitation = new EventInvitation();
        invitation.setUserRole("Member")
                .setAttendeesCount(1)
                .setParticipated(false)
                .setUserResponse("Yes");
        return invitation;
    }

    public static List<EventInvitation> createTestInvitationsList(User testUser1, User testUser2, Event testEvent) {
        List<EventInvitation> invitationsList = new ArrayList<EventInvitation>();
        EventInvitation invitation1 = new EventInvitation();
        EventInvitation invitation2 = new EventInvitation();
        invitation1.setEventId(testEvent.getId()).setUser(testUser1).setUserRole("Member");
        invitation2.setEventId(testEvent.getId()).setUser(testUser2).setUserRole("Organizer");
        invitationsList.add(invitation1);
        invitationsList.add(invitation2);
        return invitationsList;
    }

    public static EventCategory createTestCategory() {
        EventCategory category = new EventCategory();
        category.setTitle("Test Category")
                .setDescription("Test Description");
        return category;
    }

    public static Event createTestEvent() {
        Event testEvent = new Event();
        testEvent.setTitle("Test title")
                .setShortDesc("Test short description")
                .setFullDesc("Test full description")
                .setLocation("Test location")
                .setLat(11111.1f)
                .setLng(11111.1f)
                .setFilePath("/events/test_event.doc")
                .setImagePath("events/test_event.jpg");
        return testEvent;
    }

    public static EventMedia createTestMedia() {
        EventMedia media = new EventMedia();
        media.setPath("/event/111/test_path.jpg")
                .setType("Image")
                .setDescription("Test description");
        return media;
    }

    public static RecurrenceType createTestRecurrenceType() {
        RecurrenceType recType = new RecurrenceType();
        recType.setTitle("Test recurrence type").setIntervalUnit("test unit");
        return recType;
    }

    //common methods
    public static int insertTestCategory(EventCategory testCategory) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int id = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO event_category "
                    + "(title, description) VALUES (?, ?)";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, testCategory.getTitle());
            stmt.setString(2, testCategory.getDescription());
            stmt.executeUpdate();

            stmt = conn.prepareStatement("SELECT LAST_INSERT_ID() as id");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, conn);
        }
        return id;
    }

    public static void deleteTestCategory(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM event_category WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    public static int insertTestUser(User testUser) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int id = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO user "
                    + "(first_name, last_name, username, password, "
                    + "email, phone_number, avatar_path, verified, registration_date) VALUES "
                    + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, testUser.getFirstName());
            stmt.setString(2, testUser.getLastName());
            stmt.setString(3, testUser.getUsername());
            stmt.setString(4, testUser.getPassword());
            stmt.setString(5, testUser.getEmail());
            stmt.setString(6, testUser.getPhoneNumber());
            stmt.setString(7, testUser.getAvatarPath());
            stmt.setBoolean(8, testUser.isVerified());
            stmt.setTimestamp(9, new Timestamp(testUser.getRegistrationDate().getTime()));
            stmt.executeUpdate();

            stmt = conn.prepareStatement("SELECT LAST_INSERT_ID() as id");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;

    }

    public static void deleteTestUser(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM user WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int insertTestRecurrenceType() {
        RecurrenceType recType = createTestRecurrenceType();
        Connection conn = null;
        PreparedStatement stmt = null;
        int id = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO recurrence_type"
                    + "(title, interval_unit) VALUES (?, ?)";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setString(1, recType.getTitle());
            stmt.setString(2, recType.getIntervalUnit());
            stmt.executeUpdate();
            stmt = conn.prepareStatement("SELECT LAST_INSERT_ID() as id");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, conn);
        }
        return id;
    }

    public static void deleteTestRecurrenceType(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM recurrence_type WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    public static int insertTestEvent(Event testEvent) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int id = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String insertEvent = "INSERT INTO event "
                    + "(title, short_desc, full_desc, location, lat, lng, file_path, image_path, "
                    + "category_id, public_accessed, guests_allowed) VALUES "
                    + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertEvent);
            stmt.setString(1, testEvent.getTitle());
            stmt.setString(2, testEvent.getShortDesc());
            stmt.setString(3, testEvent.getFullDesc());
            stmt.setString(4, testEvent.getLocation());
            stmt.setFloat(5, testEvent.getLat());
            stmt.setFloat(6, testEvent.getLng());
            stmt.setString(7, testEvent.getFilePath());
            stmt.setString(8, testEvent.getImagePath());
            stmt.setInt(9, testEvent.getCategory().getId());
            stmt.setBoolean(10, testEvent.isPublicAccessed());
            stmt.setBoolean(11, testEvent.isGuestsAllowed());
            stmt.executeUpdate();

            stmt = conn.prepareStatement("SELECT LAST_INSERT_ID() as id");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                id = rs.getInt("id");
            }
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, conn);
        }
        return id;
    }

    public static void deleteTestEvent(int eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM event WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, eventId);
            preparedStatement.executeUpdate();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    public static int insertTestMedia(EventMedia testMedia) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int id = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO event_media "
                    + "(event_id, path, type, description, uploader_id) "
                    + "VALUES (?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, testMedia.getEventId());
            stmt.setString(2, testMedia.getPath());
            stmt.setString(3, testMedia.getType());
            stmt.setString(4, testMedia.getDescription());
            stmt.setInt(5, testMedia.getUploaderId());
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, conn);
        }
        return id;
    }

    public static void deleteTestMedia(int mediaId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM event_media WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, mediaId);
            preparedStatement.executeUpdate();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, conn);
        }
    }

    public static int insertTestInvitation(EventInvitation testInvitation) {
        Connection conn = null;
        PreparedStatement stmt = null;
        int id = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO event_invitation "
                    + "(event_id, user_id, user_role, user_response, attendees_count, participated) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, testInvitation.getEventId());
            stmt.setInt(2, testInvitation.getUser().getId());
            stmt.setString(3, testInvitation.getUserRole());
            stmt.setString(4, testInvitation.getUserResponse());
            stmt.setInt(5, testInvitation.getAttendeesCount());
            stmt.setBoolean(6, testInvitation.isParticipated());
            stmt.executeUpdate();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, conn);
        }
        return id;
    }

    public static void deleteTestInvitation(int eventId, int userId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM event_invitation WHERE event_id = ? AND user_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(null, stmt, conn);
        }
    }
}
