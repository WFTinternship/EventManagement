import com.workfront.internship.event_management.datasource.*;
import com.workfront.internship.event_management.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.List;

/**
 * Created by hermine on 7/9/16.
 */
public class TestEventMediaDAOImpl {
    private EventMediaDAO mediaDAO = new EventMediaDAOImpl();
    private EventMedia testMedia;
    private List<EventMedia> testMediaList;
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
        testMedia = TestUtil.setUpTestMedia(testUser, testEvent);
        try {
            conn = DataSourceManager.getInstance().getConnection();
            conn.setAutoCommit(false);
            insertTestUser();
            insertTestCategory();
            insertTestEvent();
            insertTestMedia();
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
            deleteTestMedia();
            deleteTestEvent();
            deleteTestUser();
            deleteTestCategory();
            testUser = null;
            testCategory = null;
            testEvent = null;
            testMedia = null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    @Test
    public void testInsertMedia() {

    }

    @Test
    public void testInsertMediaList() {

    }

    @Test
    public void testGetAllMedia() {

    }

    @Test
    public void testGetMediaByEvent() {

    }

    @Test
    public void testGetMediaByType() {

    }

    @Test
    public void testGetMediaByUploader() {

    }

    @Test
    public void testGetMediaByEventId() {

    }

    @Test
    public void testUpdateMediaDescription() {

    }
    @Test
    public void testDeleteMedia() {

    }

    //helper methods
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

    private void deleteTestMedia() throws SQLException {
        String sqlStr = "DELETE FROM event_media WHERE event_id = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, testEvent.getId());
        stmt.executeUpdate();
    }

    private void insertTestMedia() throws SQLException {
        String sqlStr = "INSERT INTO event_media "
                + "(id, event_id, path, type, description, uploader_id) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, testMedia.getId());
        stmt.setInt(2, testMedia.getEventId());
        stmt.setString(3, testMedia.getPath());
        stmt.setString(4, testMedia.getType());
        stmt.setString(5, testMedia.getDescription());
        stmt.setInt(6, testMedia.getUploaderId());
    }
}
