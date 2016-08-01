package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.*;
import org.junit.*;

import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualMedia;
import static com.workfront.internship.event_management.TestObjectCreator.NON_EXISTING_ID;
import static junit.framework.TestCase.*;

/**
 * Created by Hermine Turshujyan 7/9/16.
 */
public class MediaDAOIntegrationTest {

    private static UserDAO userDAO;
    private static CategoryDAO categoryDAO;
    private static EventDAO eventDAO;
    private static MediaDAO mediaDAO;
    private static MediaTypeDAO mediaTypeDAO;

    private MediaType testMediaType;
    private Media testMedia;
    private User testUser;
    private Event testEvent;
    private Category testCategory;


    @BeforeClass
    public static void setUpClass() throws DAOException {
        userDAO = new UserDAOImpl();
        categoryDAO = new CategoryDAOImpl();
        eventDAO = new EventDAOImpl();
        mediaTypeDAO = new MediaTypeDAOImpl();
        mediaDAO = new MediaDAOImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        userDAO = null;
        categoryDAO = null;
        eventDAO = null;
        mediaTypeDAO = null;
        mediaDAO = null;
    }

    @Before
    public void setUp() throws DuplicateEntryException, DAOException {
        createTestObjects();
        insertTestObjectsIntoDB();
    }

    @After
    public void tearDown() throws DAOException {
        deleteTestRecordsFromDB();
        deleteTestObjects();
    }


    @Test
    public void insertMedia_Success() throws DAOException, ObjectNotFoundException {
        //test media already inserted in setup, read record by mediaId
        Media media = mediaDAO.getMediaById(testMedia.getId());

        assertNotNull(media);
        assertEqualMedia(media, testMedia);
    }

    @Test(expected = DuplicateEntryException.class)
    public void insertMedia_Duplicate() throws DuplicateEntryException, DAOException {
        mediaDAO.addMedia(testMedia);  //media.path field is unique
    }

    @Test
    public void getAllMedia_Found() throws DAOException {
        //testing method
        List<Media> mediaList = mediaDAO.getAllMedia();

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertEqualMedia(mediaList.get(0), testMedia);
    }

    @Test
    public void getAllMedia_Empty_List() throws DAOException, ObjectNotFoundException {
        //delete inserted media from db
        mediaDAO.deleteMedia(testMedia.getId());

        //test method
        List<Media> mediaList = mediaDAO.getAllMedia();

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
    }

    @Test
    public void getMediaById_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        Media media = mediaDAO.getMediaById(testMedia.getId());

        assertNotNull(media);
        assertEqualMedia(media, testMedia);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getMediaById_Not_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        mediaDAO.getMediaById(NON_EXISTING_ID);
    }

    @Test
    public void getMediaByEventId_Found() throws DAOException {
        List<Media> mediaList = mediaDAO.getMediaByEventId(testEvent.getId());

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertEqualMedia(mediaList.get(0), testMedia);
    }

    @Test
    public void getMediaByEventId_Not_Found() throws DAOException {
        //testing method
        List<Media> mediaList = mediaDAO.getMediaByEventId(NON_EXISTING_ID);

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
    }

    @Test
    public void getMediaByUploader_Found() throws DAOException {
        List<Media> mediaList = mediaDAO.getMediaByUploaderId(testMedia.getUploaderId());

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertEqualMedia(mediaList.get(0), testMedia);
    }

    @Test
    public void getMediaByUploader_Not_Found() throws DAOException {
        //testing method
        List<Media> mediaList = mediaDAO.getMediaByUploaderId(TestObjectCreator.NON_EXISTING_ID);

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
    }

    @Test
    public void updateMediaDescription_Found() throws DAOException, ObjectNotFoundException {
        String description = "Updated description";
        //update expected media description
        testMedia.setDescription(description);

        //test method
        mediaDAO.updateMediaDescription(testMedia.getId(), description);

        //read updated record from db
        Media media = mediaDAO.getMediaById(testMedia.getId());

        assertNotNull(media);
        assertEqualMedia(media, testMedia);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void updateMediaDescription_Not_Found() throws ObjectNotFoundException, DAOException {
        String description = "Updated description";

        //test method
        mediaDAO.updateMediaDescription(TestObjectCreator.NON_EXISTING_ID, description);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteMedia_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        mediaDAO.deleteMedia(testMedia.getId());

        Media media = mediaDAO.getMediaById(testMedia.getId());

        assertNull(media);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteMedia_Not_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        mediaDAO.deleteMedia(TestObjectCreator.NON_EXISTING_ID);
    }

    @Test
    public void deleteAllMedia_Found() throws DAOException {
        //testing method
        mediaDAO.deleteAllMedia();

        List<Media> mediaList = mediaDAO.getAllMedia();

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
    }

    @Test
    public void deleteAllMedia_Not_Found() throws DAOException, ObjectNotFoundException {
        //delete inserted test media
        mediaDAO.deleteMedia(testMedia.getId());

        //testing method
        mediaDAO.deleteAllMedia();
    }

    //helper methods
    private void createTestObjects() {
        testUser = TestObjectCreator.createTestUser();
        testCategory = TestObjectCreator.createTestCategory();
        testEvent = TestObjectCreator.createTestEvent();
        testMedia = TestObjectCreator.createTestMedia();
        testMediaType = TestObjectCreator.createTestMediaType();
    }

    private void insertTestObjectsIntoDB() throws DuplicateEntryException, DAOException {
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

        //insert media type into db
        int mediaTypeId = mediaTypeDAO.addMediaType(testMediaType);
        testMediaType.setId(mediaTypeId);

        //insert media into db and get generated id
        testMedia.setUploaderId(testUser.getId());
        testMedia.setEventId(testEvent.getId());
        testMedia.setType(testMediaType);
        int mediaId = mediaDAO.addMedia(testMedia);
        testMedia.setId(mediaId);
    }

    private void deleteTestObjects() {
        testMedia = null;
        testEvent = null;
        testCategory = null;
        testUser = null;
    }

    private void deleteTestRecordsFromDB() throws DAOException {
        mediaDAO.deleteAllMedia();
        eventDAO.deleteAllEvents();
        categoryDAO.deleteAllCategories();
        userDAO.getAllUsers();
    }
}
