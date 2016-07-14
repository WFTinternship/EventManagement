import com.workfront.internship.event_management.datasource.*;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventCategory;
import com.workfront.internship.event_management.model.EventMedia;
import com.workfront.internship.event_management.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by hermine on 7/9/16.
 */
public class TestEventMediaDAOImpl {

    private static UserDAO userDAO;
    private static EventCategoryDAO categoryDAO;
    private static EventDAO eventDAO;
    private static EventMediaDAO mediaDAO;

    private EventMedia testMedia;
    private User testUser;
    private Event testEvent;
    private EventCategory testCategory;


    @BeforeClass
    public static void setUpClass() {
        userDAO = new UserDAOImpl();
        categoryDAO = new EventCategoryDAOImpl();
        eventDAO = new EventDAOImpl();
        mediaDAO = new EventMediaDAOImpl();
    }

    @Before
    public void setUp() {

        //create test objects
        testUser = TestHelper.createTestUser();
        testCategory = TestHelper.createTestCategory();
        testEvent = TestHelper.createTestEvent();
        testMedia = TestHelper.createTestMedia();

        //insert user into db and get generated id
        int userId = userDAO.insertUser(testUser);
        testUser.setId(userId);

        //insert category into db and get generated id
        int categoryId = categoryDAO.insertCategory(testCategory);
        testCategory.setId(categoryId);

        //insert event into db and get generated id
        testEvent.setCategory(testCategory);
        int eventId = eventDAO.insertEvent(testEvent, testUser.getId());
        testEvent.setId(eventId);

        //insert media into db and get generated id
        testMedia.setUploaderId(testUser.getId());
        testMedia.setEventId(testEvent.getId());
        int mediaId = mediaDAO.insertMedia(testMedia);
        testMedia.setId(mediaId);
    }

    @After
    public void tearDown() {
        //delete inserted records drom db
        deleteAllTestInsertions();

        testUser = null;
        testCategory = null;
        testEvent = null;
        testMedia = null;
    }

    @Test
    public void insertMedia_Success() {
        //test media already inserted in setup, read record by mediId
        EventMedia media = mediaDAO.getMediaById(testMedia.getId());

        assertMedia(media, testMedia);

    }

    @Test(expected = RuntimeException.class)
    public void insertMedia_Dublicate_Entry() {
        mediaDAO.insertMedia(testMedia);  //media.path field is unique
    }

    @Test
    public void getMediaById_Found() {
        //testing method
        EventMedia media = mediaDAO.getMediaById(testMedia.getId());

        assertMedia(media, testMedia);
    }

    @Test
    public void getMediaById_Not_Found() {
        //testing method
        EventMedia media = mediaDAO.getMediaById(TestHelper.NON_EXISTING_ID);

        assertNull(media);
    }

    @Test
    public void getMediaByEventId_Found() {
        //test media inserted in setup, insert another media
        EventMedia newTestMedia = TestHelper.createTestMedia();
        newTestMedia.setEventId(testMedia.getEventId())
                .setUploaderId(testUser.getId());
        int mediaId = mediaDAO.insertMedia(newTestMedia);
        newTestMedia.setId(mediaId);

        //testing method
        List<EventMedia> mediaList = mediaDAO.getMediaByEventId(testEvent.getId());

        assertEquals(mediaList.size(), 2);
        assertMedia(mediaList.get(0), testMedia);
        assertMedia(mediaList.get(1), newTestMedia);
    }

    @Test
    public void getMediaByEventId_Not_Found() {
        //testing method
        List<EventMedia> mediaList = mediaDAO.getMediaByEventId(TestHelper.NON_EXISTING_ID);

        assertTrue(mediaList.isEmpty());
    }




/*
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
    */

    //helper methods
    private void deleteAllTestInsertions() {
        mediaDAO.deleteAllMedia();
        eventDAO.deleteAllEvents();
        categoryDAO.deleteAllCategories();
        userDAO.getAllUsers();
    }

    private void assertMedia(EventMedia expectedMedia, EventMedia actualMedia) {
        assertEquals(actualMedia.getId(), expectedMedia.getId());
        assertEquals(actualMedia.getEventId(), expectedMedia.getEventId());
        assertEquals(actualMedia.getPath(), expectedMedia.getPath());
        assertEquals(actualMedia.getType(), expectedMedia.getType());
        assertEquals(actualMedia.getDescription(), expectedMedia.getDescription());
        assertEquals(actualMedia.getUploaderId(), expectedMedia.getUploaderId());
        assertNotNull(actualMedia.getUploadDate());
    }

    // private void createTestMedia
}
