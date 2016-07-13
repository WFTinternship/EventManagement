/**
 * Created by hermine on 7/9/16.
 */
public class TestEventMediaDAOImpl {
  /*
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
*/

}
