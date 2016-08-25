package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.MediaDAO;
import com.workfront.internship.event_management.dao.MediaDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Media;
import org.junit.*;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualMedia;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class MediaServiceUnitTest {
    private static MediaService mediaService;
    private MediaDAO mediaDAO;
    private Media testMedia;
    private List<Media> testMediaList;


    @BeforeClass
    public static void setUpClass() throws OperationFailedException {
        mediaService = new MediaServiceImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        mediaService = null;
    }

    @Before
    public void setUp() {
        //create test media object
        testMedia = createTestMedia();
        testMediaList = new ArrayList<>();
        testMediaList.add(testMedia);

        mediaDAO = Mockito.mock(MediaDAOImpl.class);
        Whitebox.setInternalState(mediaService, "mediaDAO", mediaDAO);
    }

    @After
    public void tearDown() {
        testMedia = null;
        mediaDAO = null;
    }

    //Testing addMedia method
    @Test(expected = OperationFailedException.class)
    public void addMedia_Invalid_Media() {
        testMedia.setPath(null);

        //method under test
        mediaService.addMedia(testMedia);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = OperationFailedException.class)
    public void addMedia_Duplicate() throws DuplicateEntryException, DAOException {
        when(mediaDAO.addMedia(testMedia)).thenThrow(DuplicateEntryException.class);

        //method under test
        mediaService.addMedia(testMedia);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = OperationFailedException.class)
    public void addMedia_DB_Error() throws DuplicateEntryException, DAOException {
        when(mediaDAO.addMedia(testMedia)).thenThrow(DAOException.class);

        //method under test
        mediaService.addMedia(testMedia);
    }

    @Test
    public void addMedia_Success() throws DuplicateEntryException, DAOException {
        testMedia.setId(VALID_ID);
        when(mediaDAO.addMedia(testMedia)).thenReturn(VALID_ID);

        //method under test
        Media actualMedia = mediaService.addMedia(testMedia);

        assertEqualMedia(actualMedia, testMedia);
    }

    //Testing getMediaById method
    @Test(expected = OperationFailedException.class)
    public void getMediaById_Invalid_Id() {
        //method under test
        mediaService.getMediaById(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getMediaById_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(mediaDAO).getMediaById(VALID_ID);

        //method under test
        mediaService.getMediaById(VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getMediaById_Not_Found() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(mediaDAO).getMediaById(NON_EXISTING_ID);

        //method under test
        mediaService.getMediaById(NON_EXISTING_ID);
    }

    @Test
    public void getMediaById_Success() throws ObjectNotFoundException, DAOException {
        testMedia.setId(VALID_ID);
        when(mediaDAO.getMediaById(VALID_ID)).thenReturn(testMedia);

        //method under test
        Media actualMedia = mediaService.getMediaById(VALID_ID);
        assertEqualMedia(actualMedia, testMedia);
    }

    //Testing getMediaByEvent method
    @Test(expected = OperationFailedException.class)
    public void getMediaByEvent_Invalid_Id() {
        //method under test
        mediaService.getMediaByEvent(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getMediaByEvent_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(mediaDAO).getMediaByEventId(VALID_ID);

        //method under test
        mediaService.getMediaByEvent(VALID_ID);
    }

    @Test
    public void getMediaByEvent_Empty_List() throws DAOException {
        //method under test
        List<Media> actualMediaList = mediaService.getMediaByEvent(NON_EXISTING_ID);

        assertNotNull(actualMediaList);
        assertTrue(actualMediaList.isEmpty());
    }

    @Test
    public void getMediaByEvent_Success() throws ObjectNotFoundException, DAOException {
        testMedia.setId(VALID_ID);
        when(mediaDAO.getMediaByEventId(VALID_ID)).thenReturn(testMediaList);

        //method under test
        List<Media> actualMediaList = mediaService.getMediaByEvent(VALID_ID);

        assertNotNull(actualMediaList);
        assertFalse(actualMediaList.isEmpty());
        assertEquals(actualMediaList.size(), 1);
        assertEqualMedia(actualMediaList.get(0), testMedia);
    }

    //Testing getMediaByUploader method
    @Test(expected = OperationFailedException.class)
    public void getMediaByUploader_Invalid_Id() {
        //method under test
        mediaService.getMediaByUploader(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getMediaByUploader_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(mediaDAO).getMediaByUploaderId(VALID_ID);

        //method under test
        mediaService.getMediaByUploader(VALID_ID);
    }

    @Test
    public void getMediaByUploader_Empty_List() throws DAOException {
        //method under test
        List<Media> actualMediaList = mediaService.getMediaByUploader(NON_EXISTING_ID);

        assertNotNull(actualMediaList);
        assertTrue(actualMediaList.isEmpty());
    }

    @Test
    public void getMediaByUploader_Success() throws ObjectNotFoundException, DAOException {
        testMedia.setId(VALID_ID);
        when(mediaDAO.getMediaByUploaderId(VALID_ID)).thenReturn(testMediaList);

        //method under test
        List<Media> actualMediaList = mediaService.getMediaByUploader(VALID_ID);

        assertNotNull(actualMediaList);
        assertFalse(actualMediaList.isEmpty());
        assertEquals(actualMediaList.size(), 1);
        assertEqualMedia(actualMediaList.get(0), testMedia);
    }

    //Testing editMediaDescription method
    @Test(expected = OperationFailedException.class)
    public void editMediaDescription_Invalid_Description() {
        testMedia.setDescription(null);

        //method under test
        mediaService.editMediaDescription(testMedia.getId(), testMedia.getDescription());
    }

    @Test(expected = OperationFailedException.class)
    public void editMediaDescription_Invalid_EventId() {
        //method under test
        mediaService.editMediaDescription(0, testMedia.getDescription());
    }

    @Test(expected = OperationFailedException.class)
    public void editMediaDescription_Not_Found() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(ObjectNotFoundException.class).when(mediaDAO).updateMediaDescription(testMedia.getId(), testMedia.getDescription());

        //method under test
        mediaService.editMediaDescription(testMedia.getId(), testMedia.getDescription());
    }

    @Test(expected = OperationFailedException.class)
    public void editMediaDescription_DB_Error() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        testMedia.setDescription("Updated description");
        doThrow(DAOException.class).when(mediaDAO).updateMediaDescription(testMedia.getId(), testMedia.getDescription());

        //method under test
        mediaService.editMediaDescription(testMedia.getId(), testMedia.getDescription());
    }

    @Test
    public void editMediaDescription_Success() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        testMedia.setDescription("Updated description");

        //method under test
        mediaService.editMediaDescription(VALID_ID, testMedia.getDescription());

        verify(mediaDAO).updateMediaDescription(VALID_ID, testMedia.getDescription());
    }

    //Testing deleteMedia method
    @Test(expected = OperationFailedException.class)
    public void deleteMedia_Invalid_Id() {
        //method under test
        mediaService.deleteMedia(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteMedia_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(mediaDAO).deleteMedia(VALID_ID);

        //method under test
        mediaService.deleteMedia(VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteMedia_Not_Found() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(mediaDAO).deleteMedia(NON_EXISTING_ID);

        //method under test
        mediaService.deleteMedia(NON_EXISTING_ID);
    }

    @Test
    public void deleteMedia_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        mediaService.deleteMedia(VALID_ID);

        verify(mediaDAO).deleteMedia(VALID_ID);
    }

    //Testing getAllMedia method
    @Test
    public void getAllMedia_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        mediaService.getAllMedia();

        verify(mediaDAO).getAllMedia();
    }

    @Test(expected = OperationFailedException.class)
    public void getAllMedia_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(mediaDAO).getAllMedia();

        //method under test
        mediaService.getAllMedia();
    }

    //Testing deleteAllMedia method
    @Test
    public void deleteAllMedia_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        mediaService.deleteAllMedia();

        verify(mediaDAO).deleteAllMedia();
    }

    @Test(expected = OperationFailedException.class)
    public void deleteAllMedia_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(mediaDAO).deleteAllMedia();

        //method under test
        mediaService.deleteAllMedia();
    }
}
