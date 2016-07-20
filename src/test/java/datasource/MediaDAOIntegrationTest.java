package datasource;

import com.workfront.internship.event_management.dao.*;
import com.workfront.internship.event_management.model.Event;
import com.workfront.internship.event_management.model.Category;
import com.workfront.internship.event_management.model.Media;
import com.workfront.internship.event_management.model.User;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by Hermine Turshujyan 7/9/16.
 */
public class MediaDAOIntegrationTest {

    private static UserDAO userDAO;
    private static CategoryDAO categoryDAO;
    private static EventDAO eventDAO;
    private static MediaDAO mediaDAO;

    private Media testMedia;
    private User testUser;
    private Event testEvent;
    private Category testCategory;


    @BeforeClass
    public static void setUpClass() {
        userDAO = new UserDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        eventDAO = new EventDAOImpl();
        mediaDAO = new MediaDAOImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        userDAO = null;
        categoryDAO = null;
        eventDAO = null;
        mediaDAO = null;
    }

    @Before
    public void setUp() {
        createTestObjects();
        insertTestObjectsIntoDB();
    }

    @After
    public void tearDown() {
        deleteTestRecordsFromDB();
        deleteTestObjects();
    }


    @Test
    public void insertMedia_Success() {
        //test media already inserted in setup, read record by mediId
        Media media = mediaDAO.getMediaById(testMedia.getId());

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
        List<Media> testMediaList = createTestMediaList();

        //testing method
        List<Media> mediaList = mediaDAO.getAllMedia();

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertMediaLists(mediaList, testMediaList);
    }

    @Test
    public void getAllMedia_Empty_List() {
        //delete inserted media from db
        mediaDAO.deleteMedia(testMedia.getId());

        //test method
        List<Media> mediaList = mediaDAO.getAllMedia();

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
    }

    @Test
    public void getMediaById_Found() {
        //testing method
        Media media = mediaDAO.getMediaById(testMedia.getId());

        assertNotNull(media);
        assertMedia(media, testMedia);
    }

    @Test
    public void getMediaById_Not_Found() {
        //testing method
        Media media = mediaDAO.getMediaById(TestHelper.NON_EXISTING_ID);

        assertNull(media);
    }

    @Test
    public void getMediaByEventId_Found() {
        //create test media list and insert into db
        List<Media> testMediaList = createTestMediaList();

        //testing method
        List<Media> mediaList = mediaDAO.getMediaByEventId(testEvent.getId());

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertMediaLists(mediaList, testMediaList);
    }

    @Test
    public void getMediaByEventId_Not_Found() {
        //testing method
        List<Media> mediaList = mediaDAO.getMediaByEventId(TestHelper.NON_EXISTING_ID);

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
    }

    @Test
    public void getMediaByType_Found() {
        //create test media list and insert into db
        List<Media> testMediaList = createTestMediaList();

        //testing method
        List<Media> mediaList = mediaDAO.getMediaByType(testMedia.getType());

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertMediaLists(mediaList, testMediaList);
    }

    @Test
    public void getMediaByType_Not_Found() {
        //testing method
        List<Media> mediaList = mediaDAO.getMediaByType(TestHelper.NON_EXISTING_MEDIA_TYPE);

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
    }

    @Test
    public void getMediaByUploader_Found() {
        //create test media list and insert into db
        List<Media> testMediaList = createTestMediaList();

        //testing method
        List<Media> mediaList = mediaDAO.getMediaByUploaderId(testMedia.getUploaderId());

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertMediaLists(mediaList, testMediaList);
    }

    @Test
    public void getMediaByUploader_Not_Found() {
        //testing method
        List<Media> mediaList = mediaDAO.getMediaByUploaderId(TestHelper.NON_EXISTING_ID);

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
        Media media = mediaDAO.getMediaById(testMedia.getId());

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

        Media media = mediaDAO.getMediaById(testMedia.getId());

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

        List<Media> mediaList = mediaDAO.getAllMedia();

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
        int eventId = eventDAO.addEvent(testEvent);
        testEvent.setId(eventId);

        //insert media into db and get generated id
        testMedia.setUploaderId(testUser.getId());
        testMedia.setEventId(testEvent.getId());
        int mediaId = mediaDAO.addMedia(testMedia);
        testMedia.setId(mediaId);
    }

    private void deleteTestObjects() {
        testMedia = null;
        testEvent = null;
        testCategory = null;
        testUser = null;
    }

    private void deleteTestRecordsFromDB() {
        mediaDAO.deleteAllMedia();
        eventDAO.deleteAllEvents();
        categoryDAO.deleteAllCategories();
        userDAO.getAllUsers();
    }

    private void assertMedia(Media expectedMedia, Media actualMedia) {
        assertEquals(actualMedia.getId(), expectedMedia.getId());
        assertEquals(actualMedia.getEventId(), expectedMedia.getEventId());
        assertEquals(actualMedia.getPath(), expectedMedia.getPath());
        assertEquals(actualMedia.getType(), expectedMedia.getType());
        assertEquals(actualMedia.getDescription(), expectedMedia.getDescription());
        assertEquals(actualMedia.getUploaderId(), expectedMedia.getUploaderId());
        assertNotNull(actualMedia.getUploadDate());
    }

    private void assertMediaLists(List<Media> expectedMediaList, List<Media> actualMediaList) {
        assertEquals(actualMediaList.size(), expectedMediaList.size());
        for (int i = 0; i < actualMediaList.size(); i++) {
            assertMedia(actualMediaList.get(i), expectedMediaList.get(i));
        }
    }

    private List<Media> createTestMediaList() {
        //create second test media with the same uploaderId, eventId ant mediaType
        Media secondTestMedia = TestHelper.createTestMedia();
        secondTestMedia.setEventId(testMedia.getEventId())
                .setUploaderId(testUser.getId());

        //insert second media into db
        int mediaId = mediaDAO.addMedia(secondTestMedia);
        secondTestMedia.setId(mediaId);

        //create test media list
        List<Media> testMediaList = new ArrayList<>();
        testMediaList.add(testMedia);
        testMediaList.add(secondTestMedia);

        return testMediaList;
    }
}
