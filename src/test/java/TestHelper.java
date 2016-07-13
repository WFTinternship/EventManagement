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
            if(rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }

        try {
            if(stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }

        try {
            if(conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }
    }
    public static void closeResources(Statement stmt, Connection conn) {
        closeResources(null, stmt, conn);
    }
    public static void closeResources(ResultSet rs, Statement stmt) {
        closeResources(rs, stmt, null);
    }
    public static void closeResources(Connection conn) {
        closeResources(null, conn);
    }
    public static void closeResources(Statement stmt) {
        closeResources(stmt, null);
    }
    public static void closeResources(ResultSet rs) {
        closeResources(rs, null);
    }

    //test object creation
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

    public static EventMedia createTestMedia() {
        EventMedia media = new EventMedia();
        media.setPath("/event/111/test_path.jpg")
                .setType("Image")
                .setDescription("Test description")
                .setUploadDate(new java.util.Date());
        return media;
    }

    public static List<EventMedia> createTestMediaList(EventMedia media1, EventMedia media2) {
        media1.setPath("media1 path");
        media2.setPath("media2 path");
        List<EventMedia> testMediaList = new ArrayList<EventMedia>();
        testMediaList.add(media1);
        testMediaList.add(media2);
        return testMediaList;
    }

    public static RecurrenceType createTestRecurrenceType() {
        List<String> repeatOn = new ArrayList<String>();
        repeatOn.add("Test reoeat on 1");
        repeatOn.add("Test reoeat on 2");

        RecurrenceType recType = new RecurrenceType();
        recType.setTitle("Test recurrence type").setIntervalUnit("test unit").setRepeatOnValues(repeatOn);
        return recType;
    }

    public static EventRecurrence createTestEventRecurrence() {
        EventRecurrence eventRecurrence = new EventRecurrence();
        eventRecurrence.setRepeatInterval(2).setRepeatOn("Test RepeatOn string").setRepeatEndDate(new java.util.Date());
        return eventRecurrence;
    }

    //inserting test objects to db
    public static int insertTestUserToDB(User testUser) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO user "
                    + "(first_name, last_name, username, password, "
                    + "email, phone_number, avatar_path, verified, registration_date) VALUES "
                    + "(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlStr, PreparedStatement.RETURN_GENERATED_KEYS);
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

            rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return id;

    }

    public static void deleteTestUserFromDB(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM user WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn);
        }
    }

    public static int insertTestCategoryToDB(EventCategory testCategory) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO event_category "
                    + "(title, description) VALUES (?, ?)";
            stmt = conn.prepareStatement(sqlStr, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, testCategory.getTitle());
            stmt.setString(2, testCategory.getDescription());
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return id;
    }

    public static void deleteTestCategoryFromDB(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM event_category WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn);
        }
    }

    public static int insertTestRecurrenceTypeToDB(RecurrenceType recType) {
        Connection conn = null;
        PreparedStatement stmtRecType = null;
        PreparedStatement stmtRepeatOn = null;
        ResultSet rs = null;
        int id = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            conn.setAutoCommit(false);
            String sqlStr = "INSERT INTO recurrence_type"
                    + "(title, interval_unit) VALUES (?, ?)";
            stmtRecType = conn.prepareStatement(sqlStr, PreparedStatement.RETURN_GENERATED_KEYS);
            stmtRecType.setString(1, recType.getTitle());
            stmtRecType.setString(2, recType.getIntervalUnit());
            stmtRecType.executeUpdate();

            rs = stmtRecType.getGeneratedKeys();
            if(rs.next()) {
                id = rs.getInt(1);
            }

            if(recType.getRepeatOnValues() != null) {
                String insertRepeatOnValues = "INSERT INTO repeat_on_value "
                        + "(recurrence_type_id, title) VALUES "
                        + "(?, ?)";
                stmtRepeatOn = conn.prepareStatement(insertRepeatOnValues);
                List<String> values = recType.getRepeatOnValues();
                for (String value : values) {
                    stmtRepeatOn.setInt(1, id);
                    stmtRepeatOn.setString(2, value);
                    stmtRepeatOn.addBatch();
                }
                stmtRepeatOn.executeBatch();
            }
            conn.commit();
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs);
            closeResources(stmtRecType);
            closeResources(stmtRepeatOn);
            closeResources(conn);
        }
        return id;
    }

    public static void deleteTestRecurrenceTypeFromDB(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM recurrence_type WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (IOException | SQLException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn);
        }
    }

    public static int insertTestEventToDB(Event testEvent) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String insertEvent = "INSERT INTO event "
                    + "(title, short_desc, full_desc, location, lat, lng, file_path, image_path, "
                    + "category_id, public_accessed, guests_allowed) VALUES "
                    + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertEvent, PreparedStatement.RETURN_GENERATED_KEYS);
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

            rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return id;
    }

    public static void deleteTestEventFromDB(int eventId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM event WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, eventId);
            stmt.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn);
        }
    }

    public static int insertTestMediaToDB(EventMedia testMedia) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO event_media "
                    + "(event_id, path, type, description, uploader_id, upload_date) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlStr, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, testMedia.getEventId());
            stmt.setString(2, testMedia.getPath());
            stmt.setString(3, testMedia.getType());
            stmt.setString(4, testMedia.getDescription());
            stmt.setInt(5, testMedia.getUploaderId());
            stmt.setTimestamp(6, new Timestamp(testMedia.getUploadDate().getTime()));
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return id;
    }

    public static void deleteTestMediaFromDB(int mediaId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM event_media WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, mediaId);
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn);
        }
    }

    public static int insertTestInvitationToDB(EventInvitation testInvitation) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "INSERT INTO event_invitation "
                    + "(event_id, user_id, user_role, user_response, attendees_count, participated) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(sqlStr, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, testInvitation.getEventId());
            stmt.setInt(2, testInvitation.getUser().getId());
            stmt.setString(3, testInvitation.getUserRole());
            stmt.setString(4, testInvitation.getUserResponse());
            stmt.setInt(5, testInvitation.getAttendeesCount());
            stmt.setBoolean(6, testInvitation.isParticipated());
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return id;
    }

    public static void deleteTestInvitationFromDB(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM event_invitation WHERE id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn);
        }
    }

    public static int insertTestEventRecurrenceToDB(EventRecurrence recurrence) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int id = 0;
        String sqlStr = "INSERT INTO event_recurrence "
                + "(event_id, recurrence_type_id, repeat_on, repeat_interval, repeat_end) "
                + "VALUES (?, ?, ?, ?, ?)";
        try {
            conn = DataSourceManager.getInstance().getConnection();
            stmt = conn.prepareStatement(sqlStr, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, recurrence.getEventId());
            stmt.setInt(2, recurrence.getRecurrenceType().getId());
            stmt.setString(3, recurrence.getRepeatOn());
            stmt.setInt(4, recurrence.getRepeatInterval());
            if(recurrence.getRepeatEndDate()!= null) {
                stmt.setTimestamp(5, new Timestamp(recurrence.getRepeatEndDate().getTime()));
            } else {
                stmt.setTimestamp(5, null);
            }
            stmt.executeUpdate();
            rs = stmt.getGeneratedKeys();
            if(rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
        return id;    }

    public static void deleteTestEventRecurrenceFromDB(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "DELETE FROM event_recurrence WHERE id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (IOException | SQLException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            closeResources(stmt, conn);
        }
    }}
