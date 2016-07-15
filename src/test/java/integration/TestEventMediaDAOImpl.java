package integration;

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

    public void getAllMedia_Found() {
        //create test media list and insert into db
        List<EventMedia> testMediaList = createTestMediaList();

        //testing method
        List<EventMedia> mediaList = mediaDAO.getAllMedia();

        assertMediaLists(mediaList, testMediaList);
    }

    @Test
    public void getAllMedia_Empty_List() {
        //delete inserted media from db
        mediaDAO.deleteMedia(testMedia.getId());

        //test method
        List<EventMedia> mediaList = mediaDAO.getAllMedia();

        assertTrue(mediaList.isEmpty());
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
        //create test media list and insert into db
        List<EventMedia> testMediaList = createTestMediaList();

        //testing method
        List<EventMedia> mediaList = mediaDAO.getMediaByEventId(testEvent.getId());

        assertMediaLists(mediaList, testMediaList);
    }

    @Test
    public void getMediaByEventId_Not_Found() {
        //testing method
        List<EventMedia> mediaList = mediaDAO.getMediaByEventId(TestHelper.NON_EXISTING_ID);

        assertTrue(mediaList.isEmpty());
    }


    @Test
    public void getMediaByType_Found() {
        //create test media list and insert into db
        List<EventMedia> testMediaList = createTestMediaList();

        //testing method
        List<EventMedia> mediaList = mediaDAO.getMediaByType(testMedia.getType());

        assertMediaLists(mediaList, testMediaList);
    }

    @Test
    public void getMediaByType_Not_Found() {
        //testing method
        List<EventMedia> mediaList = mediaDAO.getMediaByType(TestHelper.NON_EXISTING_MEDIA_TYPE);

        assertTrue(mediaList.isEmpty());
    }

    @Test
    public void getMediaByUploader_Found() {
        //create test media list and insert into db
        List<EventMedia> testMediaList = createTestMediaList();

        //testing method
        List<EventMedia> mediaList = mediaDAO.getMediaByUploaderId(testMedia.getUploaderId());

        assertMediaLists(mediaList, testMediaList);
    }

    @Test
    public void getMediaByUploader_Not_Found() {
        //testing method
        List<EventMedia> mediaList = mediaDAO.getMediaByUploaderId(TestHelper.NON_EXISTING_ID);

        assertTrue(mediaList.isEmpty());
    }

    @Test
    public void updateMediaDescription() {
        String description = "Updated description";
        //update expected media description
        testMedia.setDescription(description);

        //test method
        mediaDAO.updateMediaDescription(testMedia.getId(), description);

        //read updated record from db
        EventMedia media = mediaDAO.getMediaById(testMedia.getId());

        assertMedia(media, testMedia);
    }

    @Test
    public void deleteMedia() {
        //testing method
        mediaDAO.deleteMedia(testMedia.getId());

        EventMedia media = mediaDAO.getMediaById(testMedia.getId());
        assertNull(media);
    }

    @Test
    public void deleteAllMedia() {
        //testing method
        mediaDAO.deleteAllMedia();

        List<EventMedia> mediaList = mediaDAO.getAllMedia();
        assertTrue(mediaList.isEmpty());
    }

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
        int mediaId = mediaDAO.insertMedia(secondTestMedia);
        secondTestMedia.setId(mediaId);

        //create test media list
        List<EventMedia> testMediaList = new ArrayList<>();
        testMediaList.add(testMedia);
        testMediaList.add(secondTestMedia);

        return testMediaList;
    }
}
