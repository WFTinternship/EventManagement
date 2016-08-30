package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.MediaDAO;
import com.workfront.internship.event_management.dao.MediaDAOImpl;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        testMedia.setId(VALID_ID);
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
    @Test(expected = InvalidObjectException.class)
    public void addMedia_Invalid_Media() {
        testMedia.setPath(null);

        //method under test
        mediaService.addMedia(testMedia);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = OperationFailedException.class)
    public void addMedia_Duplicate() {
        when(mediaDAO.addMedia(testMedia)).thenThrow(DuplicateEntryException.class);

        //method under test
        mediaService.addMedia(testMedia);
    }

    @Test
    public void addMedia_Success() {
        testMedia.setId(VALID_ID);
        when(mediaDAO.addMedia(testMedia)).thenReturn(VALID_ID);

        //method under test
        Media actualMedia = mediaService.addMedia(testMedia);

        assertEqualMedia(actualMedia, testMedia);
    }

    //Testing getMediaById method
    @Test(expected = InvalidObjectException.class)
    public void getMediaById_Invalid_Id() {
        //method under test
        mediaService.getMediaById(INVALID_ID);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getMediaById_Not_Found() {
        when(mediaDAO.getMediaById(VALID_ID)).thenReturn(null);

        //method under test
        mediaService.getMediaById(NON_EXISTING_ID);
    }

    @Test
    public void getMediaById_Success() {
        testMedia.setId(VALID_ID);
        when(mediaDAO.getMediaById(VALID_ID)).thenReturn(testMedia);

        //method under test
        Media actualMedia = mediaService.getMediaById(VALID_ID);
        assertEqualMedia(actualMedia, testMedia);
    }

    //Testing getMediaByEvent method
    @Test(expected = InvalidObjectException.class)
    public void getMediaByEvent_Invalid_Id() {
        //method under test
        mediaService.getMediaByEvent(INVALID_ID);
    }

    @Test
    public void getMediaByEvent_Empty_List() {
        //method under test
        List<Media> actualMediaList = mediaService.getMediaByEvent(NON_EXISTING_ID);

        assertNotNull(actualMediaList);
        assertTrue(actualMediaList.isEmpty());
    }

    @Test
    public void getMediaByEvent_Success() {
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
    @Test(expected = InvalidObjectException.class)
    public void getMediaByUploader_Invalid_Id() {
        //method under test
        mediaService.getMediaByUploader(INVALID_ID);
    }

    @Test
    public void getMediaByUploader_Empty_List() {
        //method under test
        List<Media> actualMediaList = mediaService.getMediaByUploader(NON_EXISTING_ID);

        assertNotNull(actualMediaList);
        assertTrue(actualMediaList.isEmpty());
    }

    @Test
    public void getMediaByUploader_Success() {
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
    @Test(expected = InvalidObjectException.class)
    public void editMediaDescription_Invalid_Description() {
        testMedia.setDescription(null);

        //method under test
        mediaService.editMediaDescription(testMedia.getId(), testMedia.getDescription());
    }

    @Test(expected = InvalidObjectException.class)
    public void editMediaDescription_Invalid_Event_Id() {
        //method under test
        mediaService.editMediaDescription(0, testMedia.getDescription());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void editMediaDescription_Not_Found() {
        when(mediaDAO.updateMediaDescription(testMedia.getId(), testMedia.getDescription())).thenReturn(false);

        //method under test
        mediaService.editMediaDescription(testMedia.getId(), testMedia.getDescription());
    }

    @Test
    public void editMediaDescription_Success() {
        testMedia.setDescription("Updated description");

        when(mediaDAO.updateMediaDescription(testMedia.getId(), testMedia.getDescription())).thenReturn(true);

        //method under test
        boolean success = mediaService.editMediaDescription(VALID_ID, testMedia.getDescription());
        assertTrue(success);

        verify(mediaDAO).updateMediaDescription(VALID_ID, testMedia.getDescription());
    }

    //Testing deleteMedia method
    @Test(expected = InvalidObjectException.class)
    public void deleteMedia_Invalid_Id() {
        //method under test
        mediaService.deleteMedia(INVALID_ID);
    }

    @Test
    public void deleteMedia_Not_Found() {
        when(mediaDAO.deleteMedia(NON_EXISTING_ID)).thenReturn(false);

        //method under test
        boolean success = mediaService.deleteMedia(NON_EXISTING_ID);
        assertFalse(success);
    }

    @Test
    public void deleteMedia_Success() {
        when(mediaDAO.deleteMedia(VALID_ID)).thenReturn(true);

        //method under test
        boolean success = mediaService.deleteMedia(VALID_ID);
        assertTrue(success);

        verify(mediaDAO).deleteMedia(VALID_ID);
    }

    //Testing getAllMedia method
    @Test
    public void getAllMedia_Success() {
        //method under test
        mediaService.getAllMedia();

        verify(mediaDAO).getAllMedia();
    }

    //Testing deleteAllMedia method
    @Test
    public void deleteAllMedia_Success() {
        //method under test
        mediaService.deleteAllMedia();

        verify(mediaDAO).deleteAllMedia();
    }

}
