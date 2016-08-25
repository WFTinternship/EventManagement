package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.dao.MediaTypeDAO;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.MediaType;
import org.junit.*;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualMediaTypes;
import static com.workfront.internship.event_management.TestObjectCreator.INVALID_ID;
import static com.workfront.internship.event_management.TestObjectCreator.NON_EXISTING_ID;
import static com.workfront.internship.event_management.TestObjectCreator.VALID_ID;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class MediaTypeServiceUnitTest {

    private static MediaTypeService mediaTypeService;
    private MediaTypeDAO mediaTypeDAO;
    private MediaType testMediaType;

    @BeforeClass
    public static void setUpClass() throws OperationFailedException {
        mediaTypeService = new MediaTypeServiceImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        mediaTypeService = null;
    }

    @Before
    public void setUp() {
        //create test user object
        testMediaType = TestObjectCreator.createTestMediaType();

        mediaTypeDAO = Mockito.mock(MediaTypeDAO.class);
        Whitebox.setInternalState(mediaTypeService, "mediaTypeDAO", mediaTypeDAO);
    }

    @After
    public void tearDown() {
        testMediaType = null;
        mediaTypeDAO = null;
    }

    //Testing addMediaType method
    @Test(expected = OperationFailedException.class)
    public void addMediaType_Invalid_MediaType() {
        testMediaType.setTitle("");

        //method under test
        mediaTypeService.addMediaType(testMediaType);
    }

    @Test(expected = OperationFailedException.class)
    public void addMediaType_Duplicate() throws DuplicateEntryException, DAOException {
        when(mediaTypeDAO.addMediaType(testMediaType)).thenThrow(DuplicateEntryException.class);

        //method under test
        mediaTypeService.addMediaType(testMediaType);
    }

    @Test(expected = OperationFailedException.class)
    public void addMediaType_DB_Error() throws DuplicateEntryException, DAOException {
        when(mediaTypeDAO.addMediaType(testMediaType)).thenThrow(DAOException.class);

        //method under test
        mediaTypeService.addMediaType(testMediaType);
    }

    @Test
    public void addMediaType_Success() throws DuplicateEntryException, DAOException {
        testMediaType.setId(VALID_ID);
        when(mediaTypeDAO.addMediaType(testMediaType)).thenReturn(VALID_ID);

        //method under test
        MediaType actualMediaType = mediaTypeService.addMediaType(testMediaType);

        assertEqualMediaTypes(actualMediaType, testMediaType);
    }

    //Testing getMediaTypeById method
    @Test(expected = OperationFailedException.class)
    public void getMediaTypeById_Invalid_Id() {
        //method under test
        mediaTypeService.getMediaTypeById(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getMediaTypeById_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(mediaTypeDAO).getMediaTypeById(VALID_ID);

        //method under test
        mediaTypeService.getMediaTypeById(TestObjectCreator.VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getMediaTypeById_Not_Found() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(mediaTypeDAO).getMediaTypeById(NON_EXISTING_ID);

        //method under test
        mediaTypeService.getMediaTypeById(NON_EXISTING_ID);
    }

    @Test
    public void getMediaTypeById_Success() throws ObjectNotFoundException, DAOException {
        testMediaType.setId(TestObjectCreator.VALID_ID);
        when(mediaTypeDAO.getMediaTypeById(TestObjectCreator.VALID_ID)).thenReturn(testMediaType);

        //method under test
        MediaType actualMediaType = mediaTypeService.getMediaTypeById(TestObjectCreator.VALID_ID);
        assertEqualMediaTypes(actualMediaType, testMediaType);
    }

    //Testing editMediaType method
    @Test(expected = OperationFailedException.class)
    public void editMediaType_Invalid_MediaType() {
        testMediaType.setTitle("");

        //method under test
        mediaTypeService.editMediaType(testMediaType);
    }

    @Test(expected = OperationFailedException.class)
    public void editMediaType_Not_Found() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(ObjectNotFoundException.class).when(mediaTypeDAO).updateMediaType(testMediaType);

        //method under test
        mediaTypeService.editMediaType(testMediaType);
    }

    @Test(expected = OperationFailedException.class)
    public void editMediaType_Duplicate() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(DuplicateEntryException.class).when(mediaTypeDAO).updateMediaType(testMediaType);

        //method under test
        mediaTypeService.editMediaType(testMediaType);
    }

    @Test(expected = OperationFailedException.class)
    public void editMediaType_DB_Error() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(DAOException.class).when(mediaTypeDAO).updateMediaType(testMediaType);

        //method under test
        mediaTypeService.editMediaType(testMediaType);
    }

    @Test
    public void editMediaType_Success() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        //method under test
        mediaTypeService.editMediaType(testMediaType);

        verify(mediaTypeDAO).updateMediaType(testMediaType);
    }

    //Testing deleteMediaType method
    @Test(expected = OperationFailedException.class)
    public void deleteMediaType_Invalid_Id() {
        //method under test
        mediaTypeService.deleteMediaType(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteMediaType_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(mediaTypeDAO).deleteMediaType(VALID_ID);

        //method under test
        mediaTypeService.deleteMediaType(VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteMediaType_Not_Found() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(mediaTypeDAO).deleteMediaType(NON_EXISTING_ID);

        //method under test
        mediaTypeService.deleteMediaType(NON_EXISTING_ID);
    }

    @Test
    public void deleteMediaType_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        mediaTypeService.deleteMediaType(VALID_ID);

        verify(mediaTypeDAO).deleteMediaType(VALID_ID);
    }

    //Testing getAllMediaTypes method
    @Test
    public void getAllMediaTypes_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        mediaTypeService.getAllMediaTypes();

        verify(mediaTypeDAO).getAllMediaTypes();
    }

    @Test(expected = OperationFailedException.class)
    public void getAllMediaTypes_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(mediaTypeDAO).getAllMediaTypes();

        //method under test
        mediaTypeService.getAllMediaTypes();
    }

    //Testing deleteAllMediaTypes method
    @Test
    public void deleteAllMediaTypes_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        mediaTypeService.deleteAllMediaTypes();

        verify(mediaTypeDAO).deleteAllMediaTypes();
    }

    @Test(expected = OperationFailedException.class)
    public void deleteAllMediaTypes_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(mediaTypeDAO).deleteAllMediaTypes();

        //method under test
        mediaTypeService.deleteAllMediaTypes();
    }
}
