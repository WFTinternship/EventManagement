import com.workfront.internship.event_management.datasource.DataSourceManager;
import com.workfront.internship.event_management.datasource.EventMediaDAO;
import com.workfront.internship.event_management.datasource.EventMediaDAOImpl;
import com.workfront.internship.event_management.model.*;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by hermine on 7/9/16.
 */
public class TestEventMediaDAOImpl {
    private static EventMediaDAO mediaDAO;
    private EventMedia testMedia;
    private User testUser;
    private Event testEvent;
    private EventCategory testCategory;


    @BeforeClass
    public static void setUpClass() {
        mediaDAO = new EventMediaDAOImpl();
    }

    @Before
    public void setUp() {
        testUser = TestHelper.createTestUser();
        testCategory = TestHelper.createTestCategory();
        testEvent = TestHelper.createTestEvent();
        testMedia = TestHelper.createTestMedia();

        int userId = TestHelper.insertTestUserToDB(testUser);
        testUser.setId(userId);

        int categoryId = TestHelper.insertTestCategoryToDB(testCategory);
        testCategory.setId(categoryId);

        testEvent.setCategory(testCategory);
        int eventId = TestHelper.insertTestEventToDB(testEvent);
        testEvent.setId(eventId);

        testMedia.setUploaderId(testUser.getId());
        testMedia.setEventId(testEvent.getId());
        int mediaId = TestHelper.insertTestMediaToDB(testMedia);
        testMedia.setId(mediaId);
    }

    @After
    public void tearDown() {
        TestHelper.deleteTestMediaFromDB(testMedia.getId());
        TestHelper.deleteTestEventFromDB(testEvent.getId());
        TestHelper.deleteTestUserFromDB(testUser.getId());
        TestHelper.deleteTestCategoryFromDB(testCategory.getId());
        testUser = null;
        testCategory = null;
        testEvent = null;
        testMedia = null;
    }

    @Test
    public void testInsertMedia() {
        TestHelper.deleteTestMediaFromDB(testMedia.getId());
        int newMediaId = mediaDAO.insertMedia(testMedia);
        EventMedia actualMedia = getTestMediaFromDB(newMediaId);

        try {
            assertEquals(actualMedia.getUploaderId(), testMedia.getUploaderId());
            assertEquals(actualMedia.getPath(), testMedia.getPath());
            assertEquals(actualMedia.getType(), testMedia.getType());
            assertEquals(actualMedia.getDescription(), testMedia.getDescription());
        }finally {
            TestHelper.deleteTestMediaFromDB(newMediaId);
        }
    }

    @Test //---
    public void testInsertMediaList() {

    }

    @Test
    public void testGetAllMedia() {
        List<EventMedia> expectedMedia = getAllMediaFromDB();
        List<EventMedia> actualMedia = mediaDAO.getAllMedia();
        assertEquals(expectedMedia.size(), actualMedia.size());
        for (int i = 0; i < actualMedia.size(); i++) {
            assertEquals(actualMedia.get(i).getEventId(), expectedMedia.get(i).getEventId());
            assertEquals(actualMedia.get(i).getUploaderId(), expectedMedia.get(i).getUploaderId());
            assertEquals(actualMedia.get(i).getPath(), expectedMedia.get(i).getPath());
            assertEquals(actualMedia.get(i).getType(), expectedMedia.get(i).getType());
            assertEquals(actualMedia.get(i).getDescription(), expectedMedia.get(i).getDescription());
            assertEquals(actualMedia.get(i).getUploadDate(), expectedMedia.get(i).getUploadDate());
        }
    }

    @Test
    public void testGetMediaById() {
        EventMedia actualMedia = getTestMediaByField("id", testMedia.getId()).get(0);

        assertEquals(actualMedia.getUploaderId(), testMedia.getUploaderId());
        assertEquals(actualMedia.getPath(), testMedia.getPath());
        assertEquals(actualMedia.getType(), testMedia.getType());
        assertEquals(actualMedia.getDescription(), testMedia.getDescription());
    }

    @Test
    public void testGetMediaByEventId() {
        EventMedia actualMedia = getTestMediaByField("event_id", testMedia.getEventId()).get(0);
        assertEquals(actualMedia.getUploaderId(), testMedia.getUploaderId());
        assertEquals(actualMedia.getPath(), testMedia.getPath());
        assertEquals(actualMedia.getType(), testMedia.getType());
        assertEquals(actualMedia.getDescription(), testMedia.getDescription());
    }

    @Test
    public void testGetMediaByType() {
        boolean found = false;
        List<EventMedia> actualMediaList = getTestMediaByField("type", testMedia.getType());
        for(EventMedia media: actualMediaList) {
            if (media.getId() == testMedia.getId()){
                found = true;
                break;
            }
        }
       assertTrue(found);
    }

    @Test
    public void testGetMediaByUploader() {
        EventMedia actualMedia = getTestMediaByField("uploader_id", testMedia.getUploaderId()).get(0);
        assertEquals(actualMedia.getUploaderId(), testMedia.getUploaderId());
        assertEquals(actualMedia.getPath(), testMedia.getPath());
        assertEquals(actualMedia.getType(), testMedia.getType());
        assertEquals(actualMedia.getDescription(), testMedia.getDescription());
    }

    @Test
    public void testUpdateMediaDescription() {
        String changedDesc = "changed description";
        mediaDAO.updateMediaDescription(testMedia.getId(), "changed description");
        EventMedia actualMedia = getTestMediaFromDB(testMedia.getId());
        assertEquals(actualMedia.getUploaderId(), testMedia.getUploaderId());
        assertEquals(actualMedia.getPath(), testMedia.getPath());
        assertEquals(actualMedia.getType(), testMedia.getType());
        assertEquals(actualMedia.getDescription(), changedDesc);
    }

    @Test
    public void testDeleteMedia() {
        mediaDAO.deleteMedia(testMedia.getId());
        assertNull(getTestMediaFromDB(testMedia.getId()));
    }

    //helper methods
    private EventMedia getTestMediaFromDB(int mediaId){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        EventMedia testMedia = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM event_media where id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, mediaId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                testMedia = new EventMedia();
                testMedia.setId(rs.getInt("id"))
                        .setEventId(rs.getInt("event_id"))
                        .setType(rs.getString("type"))
                        .setPath(rs.getString("path"))
                        .setDescription(rs.getString("description"))
                        .setUploaderId(rs.getInt("uploader_id"))
                        .setUploadDate(rs.getTimestamp("upload_date"));
            }
        } catch (SQLException | IOException | PropertyVetoException e) {
                e.printStackTrace();
        } finally {
            TestHelper.closeResources(rs, stmt, conn);
        }
        return testMedia;
    }

    private List<EventMedia> getTestMediaListFromDB() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventMedia> mediaList = new ArrayList<EventMedia>();
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM event_media where event_id = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, testEvent.getId());
            rs = stmt.executeQuery();
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
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            TestHelper.closeResources(rs, stmt, conn);
        }
        return mediaList;
    }

    private List<EventMedia> getAllMediaFromDB(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventMedia> mediaList = new ArrayList<EventMedia>();
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM event_media";
            stmt = conn.prepareStatement(sqlStr);
            rs = stmt.executeQuery();
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
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            TestHelper.closeResources(rs, stmt, conn);
        }
        return mediaList;
    }

    private List<EventMedia> getTestMediaByField(String columnName, Object columnValue) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventMedia> mediaList = new ArrayList<EventMedia>();
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM event_media where " + columnName + " = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setObject(1, columnValue);
            rs = stmt.executeQuery();
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
        } catch (SQLException | IOException | PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            TestHelper.closeResources(rs, stmt, conn);
        }
        return mediaList;
    }

}
