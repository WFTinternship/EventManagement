import com.workfront.internship.event_management.datasource.DataSourceManager;
import com.workfront.internship.event_management.datasource.EventMediaDAO;
import com.workfront.internship.event_management.datasource.EventMediaDAOImpl;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventCategory;
import com.workfront.internship.event_management.model.EventMedia;
import com.workfront.internship.event_management.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by hermine on 7/9/16.
 */
public class TestEventMediaDAOImpl {
    private static EventMediaDAO mediaDAO;
    private EventMedia testMedia;
    private List<EventMedia> testMediaList;
    private User testUser;
    private Event testEvent;
    private EventCategory testCategory;
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;

    @BeforeClass
    public static void setUpClass(){
        mediaDAO = new EventMediaDAOImpl();
    }

    @Before
    public void setUp() {
        testUser = TestHelper.setUpTestUser();
        testCategory = TestHelper.setUpTestCategory();
        testEvent = TestHelper.setUpTestEvent(testCategory);
        testMedia = TestHelper.setUpTestMedia(testUser, testEvent);
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
            TestHelper.closeResources(rs, stmt, conn);
        }
    }

    @Test
    public void testInsertMedia() throws SQLException {
        deleteTestMedia();
        mediaDAO.insertMedia(testMedia);
        List<EventMedia> mediaList = getTestMedia();
        assertEquals(mediaList.size(), 1);
        EventMedia actualMedia = mediaList.get(0);
        assertEquals(actualMedia.getUploaderId(), testMedia.getUploaderId());
        assertEquals(actualMedia.getPath(), testMedia.getPath());
        assertEquals(actualMedia.getType(), testMedia.getType());
        assertEquals(actualMedia.getDescription(), testMedia.getDescription());
        assertNotNull(actualMedia.getUploadDate());
    }

    @Test
    public void testInsertMediaList() {

    }

    @Test
    public void testGetAllMedia() throws SQLException {
        List<EventMedia> expectedMedia = getAllMedia();
        List<EventMedia> actualMedia = mediaDAO.getAllMedia();
        assertEquals(expectedMedia.size(), actualMedia.size());
        for (int i = 0; i < actualMedia.size(); i++) {
            assertEquals(actualMedia.get(i).getEventId(), expectedMedia.get(i).getEventId());
            assertEquals(actualMedia.get(i).getUploaderId(), expectedMedia.get(i).getUploaderId());
            assertEquals(actualMedia.get(i).getPath(), expectedMedia.get(i).getPath());
            assertEquals(actualMedia.get(i).getType(), expectedMedia.get(i).getType());
            assertEquals(actualMedia.get(i).getDescription(), expectedMedia.get(i).getDescription());
            assertEquals(actualMedia.get(i).getUploadDate(),expectedMedia.get(i).getUploadDate() );
        }
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
    private void insertTestUser() throws SQLException {
        String sqlStr = "INSERT INTO user "
                + "(first_name, last_name, username, password, "
                + "email, phone_number, avatar_path) VALUES "
                + "(?, ?, ?, ?, ?, ?, ?)";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setString(1, testUser.getFirstName());
        stmt.setString(2, testUser.getLastName());
        stmt.setString(3, testUser.getUsername());
        stmt.setString(4, testUser.getPassword());
        stmt.setString(5, testUser.getEmail());
        stmt.setString(6, testUser.getPhoneNumber());
        stmt.setString(7, testUser.getAvatarPath());
        stmt.executeUpdate();
    }

    private void insertTestEvent() throws SQLException {
        String insertEvent = "INSERT INTO event "
                + "(title, short_desc, full_desc, location, lat, lng, file_path, image_path, "
                + "category_id, public_access, guests_allowed) VALUES "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?)";
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
    }

    private void insertTestCategory() throws SQLException {
        String sqlStr = "INSERT INTO event_category "
                + "(title, description) "
                + "VALUES (?, ?)";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setString(1, testCategory.getTitle());
        stmt.setString(2, testCategory.getDescription());
        stmt.executeUpdate();
    }

    private void insertTestMedia() throws SQLException {
        String sqlStr = "INSERT INTO event_media "
                + "(event_id, path, type, description, uploader_id) "
                + "VALUES (?, ?, ?, ?, ?)";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, testMedia.getEventId());
        stmt.setString(2, testMedia.getPath());
        stmt.setString(3, testMedia.getType());
        stmt.setString(4, testMedia.getDescription());
        stmt.setInt(5, testMedia.getUploaderId());
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



    private List<EventMedia> getTestMedia() throws SQLException {
        String sqlStr = "SELECT * FROM event_media where event_id = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, testEvent.getId());
        rs = stmt.executeQuery();
        List<EventMedia> mediaList = new ArrayList<EventMedia>();
        while (rs.next()) {
            EventMedia media = new EventMedia();
            media.setId(rs.getInt("id"))
                    .setEventId(rs.getInt("event_id"))
                    .setType(rs.getString("type"))
                    .setPath(rs.getString("path"))
                    .setDescription(rs.getString("description"))
                    .setUploaderId(rs.getInt("uploader_id"))
                    .setUploadDate(rs.getTimestamp("upload_date"));
            mediaList.add(media);
        }
        return mediaList;
    }

    private List<EventMedia> getAllMedia() throws SQLException {
        String sqlStr = "SELECT * FROM event_media";
        stmt = conn.prepareStatement(sqlStr);
        rs = stmt.executeQuery();
        List<EventMedia> mediaList = new ArrayList<EventMedia>();
        while (rs.next()) {
            EventMedia media = new EventMedia();
            media.setId(rs.getInt("id"))
                    .setEventId(rs.getInt("event_id"))
                    .setType(rs.getString("type"))
                    .setPath(rs.getString("path"))
                    .setDescription(rs.getString("description"))
                    .setUploaderId(rs.getInt("uploader_id"))
                    .setUploadDate(rs.getTimestamp("upload_date"));
            mediaList.add(media);
        }
        return mediaList;
    }
}
