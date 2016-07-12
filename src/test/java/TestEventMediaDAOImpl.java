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
        testUser = TestHelper.createTestUser();
        testCategory = TestHelper.createTestCategory();
        testEvent = TestHelper.createTestEvent();
        testMedia = TestHelper.createTestMedia();
        try {
            conn = DataSourceManager.getInstance().getConnection();
            conn.setAutoCommit(false);
            testUser.setId(TestHelper.insertTestUser(testUser));
            testCategory.setId(TestHelper.insertTestCategory(testCategory));
           // testEvent.setId(TestHelper.insertTestEvent());
          //  testMedia.setId(TestHelper.insertTestMedia());
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
    public void tearDown(){
            TestHelper.deleteTestMedia(testEvent.getId());
            TestHelper.deleteTestEvent(testEvent.getId());
            TestHelper.deleteTestUser(testUser.getId());
            TestHelper.deleteTestCategory(testCategory.getId());
            testUser = null;
            testCategory = null;
            testEvent = null;
            testMedia = null;
            TestHelper.closeResources(rs, stmt, conn);
    }

    @Test
    public void testInsertMedia() throws SQLException {
        TestHelper.deleteTestMedia(testMedia.getId());
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
