package datasource;

import com.workfront.internship.event_management.datasource.*;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.EventCategory;
import com.workfront.internship.event_management.model.EventMedia;
import com.workfront.internship.event_management.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by hermine on 7/9/16.
 */
public class EventMediaDAOImplIntegrationTest {

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
        createTestObjects();
        insertTestObjectsIntoDB();
    }

    @After
    public void tearDown() {
        deleteAllTestInsertionsFromDB();
        deleteTestObjects();
    }

    @Test
    public void insertMedia_Success() {
        //test media already inserted in setup, read record by mediId
        EventMedia media = mediaDAO.getMediaById(testMedia.getId());

        assertNotNull(media);
        assertMedia(media, testMedia);
    }

    @Test(expected = RuntimeException.class)
    public void insertMedia_Dublicate_Entry() {
        mediaDAO.addMedia(testMedia);  //media.path field is unique
    }

    @Test
    public void getAllMedia_Found() {
        //create test media list and insert into db
        List<EventMedia> testMediaList = createTestMediaList();

        //testing method
        List<EventMedia> mediaList = mediaDAO.getAllMedia();

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertMediaLists(mediaList, testMediaList);
    }

    @Test
    public void getAllMedia_Empty_List() {
        //delete inserted media from db
        mediaDAO.deleteMedia(testMedia.getId());

        //test method
        List<EventMedia> mediaList = mediaDAO.getAllMedia();

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
    }

    @Test
    public void getMediaById_Found() {
        //testing method
        EventMedia media = mediaDAO.getMediaById(testMedia.getId());

        assertNotNull(media);
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
        //create test media list and insert into db
        List<EventMedia> testMediaList = createTestMediaList();

        //testing method
        List<EventMedia> mediaList = mediaDAO.getMediaByEventId(testEvent.getId());

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertMediaLists(mediaList, testMediaList);
    }

    @Test
    public void getMediaByEventId_Not_Found() {
        //testing method
        List<EventMedia> mediaList = mediaDAO.getMediaByEventId(TestHelper.NON_EXISTING_ID);

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
    }

    @Test
    public void getMediaByType_Found() {
        //create test media list and insert into db
        List<EventMedia> testMediaList = createTestMediaList();

        //testing method
        List<EventMedia> mediaList = mediaDAO.getMediaByType(testMedia.getType());

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertMediaLists(mediaList, testMediaList);
    }

    @Test
    public void getMediaByType_Not_Found() {
        //testing method
        List<EventMedia> mediaList = mediaDAO.getMediaByType(TestHelper.NON_EXISTING_MEDIA_TYPE);

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
    }

    @Test
    public void getMediaByUploader_Found() {
        //create test media list and insert into db
        List<EventMedia> testMediaList = createTestMediaList();

        //testing method
        List<EventMedia> mediaList = mediaDAO.getMediaByUploaderId(testMedia.getUploaderId());

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertMediaLists(mediaList, testMediaList);
    }

    @Test
    public void getMediaByUploader_Not_Found() {
        //testing method
        List<EventMedia> mediaList = mediaDAO.getMediaByUploaderId(TestHelper.NON_EXISTING_ID);

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
    }

    @Test
    public void updateMediaDescription_Found() {
        String description = "Updated description";
        //update expected media description
        testMedia.setDescription(description);

        //test method
        mediaDAO.updateMediaDescription(testMedia.getId(), description);

        //read updated record from db
        EventMedia media = mediaDAO.getMediaById(testMedia.getId());

        assertNotNull(media);
        assertMedia(media, testMedia);
    }

    @Test
    public void updateMediaDescription_Not_Found() {
        String description = "Updated description";

        //test method
        boolean updated = mediaDAO.updateMediaDescription(TestHelper.NON_EXISTING_ID, description);

        assertFalse(updated);
    }

    @Test
    public void deleteMedia_Found() {
        //testing method
        boolean deleted = mediaDAO.deleteMedia(testMedia.getId());

        EventMedia media = mediaDAO.getMediaById(testMedia.getId());

        assertTrue(deleted);
        assertNull(media);
    }

    @Test
    public void deleteMedia_Not_Found() {
        //testing method
        boolean deleted = mediaDAO.deleteMedia(TestHelper.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteAllMedia_Found() {
        //testing method
        boolean deleted = mediaDAO.deleteAllMedia();

        List<EventMedia> mediaList = mediaDAO.getAllMedia();

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
        assertTrue(deleted);
    }

    @Test
    public void deleteAllMedia_Not_Found() {
        //delete inserted test media
        mediaDAO.deleteMedia(testMedia.getId());

        //testing method
        boolean deleted = mediaDAO.deleteAllMedia();

        assertFalse(deleted);
    }


    //helper methods
    private void createTestObjects() {
        testUser = TestHelper.createTestUser();
        testCategory = TestHelper.createTestCategory();
        testEvent = TestHelper.createTestEvent();
        testMedia = TestHelper.createTestMedia();
    }

    private void insertTestObjectsIntoDB() {
        //insert user into db and get generated id
        int userId = userDAO.addUser(testUser);
        testUser.setId(userId);

        //insert category into db and get generated id
        int categoryId = categoryDAO.addCategory(testCategory);
        testCategory.setId(categoryId);

        //insert event into db and get generated id
        testEvent.setCategory(testCategory);
        int eventId = eventDAO.insertEvent(testEvent, testUser.getId());
        testEvent.setId(eventId);

        //insert media into db and get generated id
        testMedia.setUploaderId(testUser.getId());
        testMedia.setEventId(testEvent.getId());
        int mediaId = mediaDAO.addMedia(testMedia);
        testMedia.setId(mediaId);
    }

    private void deleteAllTestInsertionsFromDB() {
        mediaDAO.deleteAllMedia();
        eventDAO.deleteAllEvents();
        categoryDAO.deleteAllCategories();
        userDAO.getAllUsers();
    }

    private void deleteTestObjects() {
        testUser = null;
        testCategory = null;
        testEvent = null;
        testMedia = null;
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

    private void assertMediaLists(List<EventMedia> expectedMediaList, List<EventMedia> actualMediaList) {
        assertEquals(actualMediaList.size(), expectedMediaList.size());
        for(int i = 0; i < actualMediaList.size(); i++) {
            assertMedia(actualMediaList.get(i), expectedMediaList.get(i));
        }
    }

    private List<EventMedia> createTestMediaList(){
        //create second test media with the same uploaderId, eventId ant mediaType
        EventMedia secondTestMedia = TestHelper.createTestMedia();
        secondTestMedia.setEventId(testMedia.getEventId())
                        .setUploaderId(testUser.getId());

        //insert second media into db
        int mediaId = mediaDAO.addMedia(secondTestMedia);
        secondTestMedia.setId(mediaId);

        //create test media list
        List<EventMedia> testMediaList = new ArrayList<>();
        testMediaList.add(testMedia);
        testMediaList.add(secondTestMedia);

        return testMediaList;
    }
}
