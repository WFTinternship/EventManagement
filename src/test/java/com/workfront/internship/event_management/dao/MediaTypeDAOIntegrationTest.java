package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.MediaType;
import org.junit.*;

import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualMediaTypes;
import static junit.framework.TestCase.*;

/**
 * Created by Hermine Turshujyan 7/21/16.
 */
public class MediaTypeDAOIntegrationTest {

    private static MediaTypeDAO mediaTypeDAO;
    private MediaType testMediaType;

    @BeforeClass
    public static void setUpClass() throws DAOException {
        mediaTypeDAO = new MediaTypeDAOImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        mediaTypeDAO = null;
    }

    @Before
    public void setUp() throws DuplicateEntryException, DAOException {
        //create test media type
        testMediaType = TestObjectCreator.createTestMediaType();

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
    public void addMediaType_Success() throws DAOException, ObjectNotFoundException {
        //test media type already inserted in setup, read record by id
        MediaType mediaType = mediaTypeDAO.getMediaTypeById(testMediaType.getId());

        assertNotNull(mediaType);
        assertEqualMediaTypes(mediaType, testMediaType);
    }

    @Test(expected = DuplicateEntryException.class)
    public void addMediaType_Duplicate() throws DuplicateEntryException, DAOException {
        //method under test
        mediaTypeDAO.addMediaType(testMediaType);
    }

    @Test
    public void getAllMediaTypes_Found() throws DAOException {
        //method under test
        List<MediaType> mediaTypeList = mediaTypeDAO.getAllMediaTypes();

        assertNotNull(mediaTypeList);
        assertFalse(mediaTypeList.isEmpty());
        assertEqualMediaTypes(mediaTypeList.get(0), testMediaType);
    }

    @Test
    public void getAllMediaTypes_Empty_List() throws DAOException, ObjectNotFoundException {
        //delete inserted category from db
        mediaTypeDAO.deleteMediaType(testMediaType.getId());

        //method under test
        List<MediaType> mediaTypeList = mediaTypeDAO.getAllMediaTypes();

        assertNotNull(mediaTypeList);
        assertTrue(mediaTypeList.isEmpty());
    }

    @Test
    public void getMediaTypeById_Found() throws DAOException, ObjectNotFoundException {
        //method under test
        MediaType mediaType = mediaTypeDAO.getMediaTypeById(testMediaType.getId());

        assertNotNull(mediaType);
        assertEqualMediaTypes(mediaType, testMediaType);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getMediaTypeById_Not_Found() throws DAOException, ObjectNotFoundException {
        //test method
        MediaType mediaType = mediaTypeDAO.getMediaTypeById(TestObjectCreator.NON_EXISTING_ID);

        assertNull(mediaType);
    }

    @Test
    public void updateMediaType_Success() throws DuplicateEntryException, DAOException, ObjectNotFoundException {
        //create new media type with the same id
        testMediaType.setTitle("new changed title");

        //test method
        mediaTypeDAO.updateMediaType(testMediaType);

        //read updated record from db
        MediaType mediaType = mediaTypeDAO.getMediaTypeById(testMediaType.getId());

        assertNotNull(mediaType);
        assertEqualMediaTypes(mediaType, testMediaType);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void updateMediaType_Not_Found() throws DuplicateEntryException, DAOException, ObjectNotFoundException {
        //create new media type object with non-existing id
        MediaType mediaType = TestObjectCreator.createTestMediaType();

        //test method
        mediaTypeDAO.updateMediaType(mediaType);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteMediaType_Success() throws DAOException, ObjectNotFoundException {
        //testing method
        mediaTypeDAO.deleteMediaType(testMediaType.getId());

        mediaTypeDAO.getMediaTypeById(testMediaType.getId());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteMediaType_Not_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        mediaTypeDAO.deleteMediaType(TestObjectCreator.NON_EXISTING_ID);
    }

    @Test
    public void deleteAllMediaTypes_Success() throws DAOException {
        //testing method
        mediaTypeDAO.deleteAllMediaTypes();

        List<MediaType> mediaTypeList = mediaTypeDAO.getAllMediaTypes();

        assertNotNull(mediaTypeList);
        assertTrue(mediaTypeList.isEmpty());
    }

    @Test
    public void deleteAllMediaTypes_Not_Found() throws DAOException, ObjectNotFoundException {
        //delete inserted media type
        mediaTypeDAO.deleteMediaType(testMediaType.getId());

        //testing method
        mediaTypeDAO.deleteAllMediaTypes();
    }
}
