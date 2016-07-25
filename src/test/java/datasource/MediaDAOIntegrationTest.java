package datasource;

/**
 * Created by Hermine Turshujyan 7/9/16.
 */
public class MediaDAOIntegrationTest {
/*
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
        //testing method
        List<Media> mediaList = mediaDAO.getAllMedia();

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertMedia(mediaList.get(0), testMedia);
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
        List<Media> mediaList = mediaDAO.getMediaByEventId(testEvent.getId());

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertMedia(mediaList.get(0), testMedia);
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
        //testing method
        List<Media> mediaList = mediaDAO.getMediaByType(testMedia.getType().getId());

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertMedia(mediaList.get(0), testMedia);
    }

    @Test
    public void getMediaByType_Not_Found() {
        //testing method
        List<Media> mediaList = mediaDAO.getMediaByType(TestHelper.NON_EXISTING_ID);

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
    }

    @Test
    public void getMediaByUploader_Found() {
        List<Media> mediaList = mediaDAO.getMediaByUploaderId(testMedia.getUploaderId());

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertMedia(mediaList.get(0), testMedia);
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
    public void deleteAllMedia_Not_Found() throws DAOException {
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
        testMediaType = TestHelper.createTestMediaType();
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
        assertEquals(actualMedia.getType().getId(), expectedMedia.getType().getId());
        assertEquals(actualMedia.getType().getTitle(), expectedMedia.getType().getTitle());
        assertEquals(actualMedia.getDescription(), expectedMedia.getDescription());
        assertEquals(actualMedia.getUploaderId(), expectedMedia.getUploaderId());
        assertNotNull(actualMedia.getUploadDate());
    }*/
}
