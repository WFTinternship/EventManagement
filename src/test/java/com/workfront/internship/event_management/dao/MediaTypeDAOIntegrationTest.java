package com.workfront.internship.event_management.dao;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
public class MediaTypeDAOIntegrationTest {
/*
    private static MediaTypeDAO mediaTypeDAO;
    private MediaType testMediaType;

    @BeforeClass
    public static void setUpClass() {
        mediaTypeDAO = new MediaTypeDAOImpl() {
        };
    }

    @AfterClass
    public static void tearDownClass() {
        mediaTypeDAO = null;
    }

    @Before
    public void setUp() {
        //create test media type
        testMediaType = TestHelper.createTestMediaType();

        //insert test user into db, get generated id
        int mediaTypeId = mediaTypeDAO.addMediaType(testMediaType);
        testMediaType.setId(mediaTypeId);
    }

    @After
    public void tearDown() throws DAOException {
        //delete test record from db
        mediaTypeDAO.deleteAllMediaTypes();

        //delete test media type object
        testMediaType = null;
    }

    @Test
    public void addMediaType_Success() {
        //test media type already inserted in setup, read record by id
        MediaType mediaType = mediaTypeDAO.getMediaTypeById(testMediaType.getId());

        assertNotNull(mediaType);
        assertEqualMediaTypes(mediaType, testMediaType);
    }

    @Test(expected = RuntimeException.class)
    public void addMediaType_Dublicate_Entry() {
        //test media type already inserted into db, insert dublicate category
        mediaTypeDAO.addMediaType(testMediaType);  //event_category.title field in db is unique
    }

    @Test
    public void getAllMediaTypes_Found() {

        List<MediaType> mediaTypeList = mediaTypeDAO.getAllMediaTypes();

        assertNotNull(mediaTypeList);
        assertFalse(mediaTypeList.isEmpty());
        assertEqualMediaTypes(mediaTypeList.get(0), testMediaType);
    }

    @Test
    public void getAllMediaTypes_Empty_List() throws DAOException {
        //delete inserted category from db
        mediaTypeDAO.deleteMediaType(testMediaType.getId());

        //test method
        List<MediaType> mediaTypeList = mediaTypeDAO.getAllMediaTypes();

        assertNotNull(mediaTypeList);
        assertTrue(mediaTypeList.isEmpty());
    }

    @Test
    public void getMediaTypeById_Found() {
        //test method
        MediaType mediaType = mediaTypeDAO.getMediaTypeById(testMediaType.getId());

        assertNotNull(mediaType);
        assertEqualMediaTypes(mediaType, testMediaType);
    }

    @Test
    public void getMediaTypeById_Not_Found() {
        //test method
        MediaType mediaType = mediaTypeDAO.getMediaTypeById(TestHelper.NON_EXISTING_ID);

        assertNull(mediaType);
    }

    @Test
    public void updateMediaType_Found() {
        //create new media type with the same id
        MediaType updatedMediaType = TestHelper.createTestMediaType();
        updatedMediaType.setId(testMediaType.getId());

        //test method
        mediaTypeDAO.updateMediaType(updatedMediaType);

        //read updated record from db
        MediaType mediaType = mediaTypeDAO.getMediaTypeById(updatedMediaType.getId());

        assertNotNull(mediaType);
        assertEqualMediaTypes(mediaType, updatedMediaType);
    }

    @Test
    public void updateMediaType_Not_Found() {
        //create new media type object with non-existing id
        MediaType mediaType = TestHelper.createTestMediaType();

        //test method
        boolean updated = mediaTypeDAO.updateMediaType(mediaType);

        assertFalse(updated);
    }

    @Test
    public void deleteMediaType_Found() throws DAOException {
        //testing method
        boolean deleted = mediaTypeDAO.deleteMediaType(testMediaType.getId());

        MediaType mediaType = mediaTypeDAO.getMediaTypeById(testMediaType.getId());

        assertTrue(deleted);
        assertNull(mediaType);
    }

    @Test
    public void deleteCategory_Not_Found() throws DAOException {
        //testing method
        boolean deleted = mediaTypeDAO.deleteMediaType(TestHelper.NON_EXISTING_ID);

        assertFalse(deleted);
    }

    @Test
    public void deleteAllCategories_Found() throws DAOException {
        //testing method
        boolean deleted = mediaTypeDAO.deleteAllMediaTypes();

        List<MediaType> mediaTypeList = mediaTypeDAO.getAllMediaTypes();

        assertNotNull(mediaTypeList);
        assertTrue(mediaTypeList.isEmpty());
        assertTrue(deleted);
    }

    @Test
    public void deleteAllMediaTypes_Not_Found() throws DAOException {
        //delete inserted media type
        mediaTypeDAO.deleteMediaType(testMediaType.getId());

        //testing method
        boolean deleted = mediaTypeDAO.deleteAllMediaTypes();

        assertFalse(deleted);
    }

    //helper methods
    private void assertEqualMediaTypes(MediaType expectedMediaType, MediaType actualMediaType) {
        assertEquals(expectedMediaType.getId(), actualMediaType.getId());
        assertEquals(expectedMediaType.getTitle(), actualMediaType.getTitle());
    }*/

}
