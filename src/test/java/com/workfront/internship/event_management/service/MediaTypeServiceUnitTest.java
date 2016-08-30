package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.dao.MediaTypeDAO;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.MediaType;
import org.junit.*;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualMediaTypes;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class MediaTypeServiceUnitTest {

    private static MediaTypeService mediaTypeService;

    private MediaTypeDAO mediaTypeDAO;
    private MediaType testMediaType;

    @BeforeClass
    public static void setUpClass() {
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
    @Test(expected = InvalidObjectException.class)
    public void addMediaType_Invalid_MediaType() {
        testMediaType.setTitle("");

        //method under test
        mediaTypeService.addMediaType(testMediaType);
    }

    @Test(expected = OperationFailedException.class)
    public void addMediaType_Duplicate() {
        when(mediaTypeDAO.addMediaType(testMediaType)).thenThrow(DuplicateEntryException.class);

        //method under test
        mediaTypeService.addMediaType(testMediaType);
    }

    @Test
    public void addMediaType_Success() {
        testMediaType.setId(VALID_ID);
        when(mediaTypeDAO.addMediaType(testMediaType)).thenReturn(VALID_ID);

        //method under test
        MediaType actualMediaType = mediaTypeService.addMediaType(testMediaType);

        assertEqualMediaTypes(actualMediaType, testMediaType);
    }

    //Testing getMediaTypeById method
    @Test(expected = InvalidObjectException.class)
    public void getMediaTypeById_Invalid_Id() {
        //method under test
        mediaTypeService.getMediaTypeById(INVALID_ID);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getMediaTypeById_Not_Found() {
        when(mediaTypeDAO.getMediaTypeById(NON_EXISTING_ID)).thenReturn(null);

        //method under test
        mediaTypeService.getMediaTypeById(NON_EXISTING_ID);
    }

    @Test
    public void getMediaTypeById_Success() {
        testMediaType.setId(TestObjectCreator.VALID_ID);
        when(mediaTypeDAO.getMediaTypeById(TestObjectCreator.VALID_ID)).thenReturn(testMediaType);

        //method under test
        MediaType actualMediaType = mediaTypeService.getMediaTypeById(TestObjectCreator.VALID_ID);
        assertEqualMediaTypes(actualMediaType, testMediaType);
    }

    //Testing editMediaType method
    @Test(expected = InvalidObjectException.class)
    public void editMediaType_Invalid_MediaType() {
        testMediaType.setTitle("");

        //method under test
        mediaTypeService.editMediaType(testMediaType);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void editMediaType_Not_Found() {
        when(mediaTypeDAO.updateMediaType(testMediaType)).thenReturn(false);

        //method under test
        mediaTypeService.editMediaType(testMediaType);
    }

    @Test(expected = OperationFailedException.class)
    public void editMediaType_Duplicate() {
        doThrow(DuplicateEntryException.class).when(mediaTypeDAO).updateMediaType(testMediaType);

        //method under test
        mediaTypeService.editMediaType(testMediaType);
    }

    @Test
    public void editMediaType_Success() {
        when(mediaTypeDAO.updateMediaType(testMediaType)).thenReturn(true);

        //method under test
        boolean success = mediaTypeService.editMediaType(testMediaType);
        assertTrue(success);
    }

    //Testing deleteMediaType method
    @Test(expected = InvalidObjectException.class)
    public void deleteMediaType_Invalid_Id() {
        //method under test
        mediaTypeService.deleteMediaType(INVALID_ID);
    }

    @Test
    public void deleteMediaType_Not_Found() {
        when(mediaTypeDAO.deleteMediaType(NON_EXISTING_ID)).thenReturn(false);

        //method under test
        boolean success = mediaTypeService.deleteMediaType(NON_EXISTING_ID);
        assertFalse(success);
    }

    @Test
    public void deleteMediaType_Success() {
        //method under test
        mediaTypeService.deleteMediaType(VALID_ID);

        verify(mediaTypeDAO).deleteMediaType(VALID_ID);
    }

    //Testing getAllMediaTypes method
    @Test
    public void getAllMediaTypes_Success() {
        //method under test
        mediaTypeService.getAllMediaTypes();

        verify(mediaTypeDAO).getAllMediaTypes();
    }

    //Testing deleteAllMediaTypes method
    @Test
    public void deleteAllMediaTypes_Success() {
        //method under test
        mediaTypeService.deleteAllMediaTypes();

        verify(mediaTypeDAO).deleteAllMediaTypes();
    }
}
