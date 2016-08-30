package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.*;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualMedia;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.*;

/**
 * Created by Hermine Turshujyan 8/29/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class MediaServiceIntegrationTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private MediaService mediaService;
    @Autowired
    private MediaTypeService mediaTypeService;

    private MediaType testMediaType;
    private Media testMedia;
    private User testUser;
    private Event testEvent;
    private Category testCategory;

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
    public void insertMedia() {
        //test media already inserted in setup, read record by mediaId
        Media media = mediaService.getMediaById(testMedia.getId());

        assertNotNull(media);
        assertEqualMedia(media, testMedia);
    }

    @Test
    public void getAllMedia() {
        //testing method
        List<Media> mediaList = mediaService.getAllMedia();

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertEqualMedia(mediaList.get(0), testMedia);
    }


    @Test
    public void getMediaByEventId() {
        List<Media> mediaList = mediaService.getMediaByEvent(testEvent.getId());

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertEqualMedia(mediaList.get(0), testMedia);
    }


    @Test
    public void getMediaByUploader() {
        List<Media> mediaList = mediaService.getMediaByUploader(testMedia.getUploaderId());

        assertNotNull(mediaList);
        assertFalse(mediaList.isEmpty());
        assertEqualMedia(mediaList.get(0), testMedia);
    }

    @Test
    public void updateMediaDescription() {
        //update expected media description
        String description = "Updated description";
        testMedia.setDescription(description);

        //test method
        mediaService.editMediaDescription(testMedia.getId(), description);

        //read updated record from db
        Media media = mediaService.getMediaById(testMedia.getId());

        assertNotNull(media);
        assertEqualMedia(media, testMedia);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteMedia() {
        //testing method
        mediaService.deleteMedia(testMedia.getId());

        Media media = mediaService.getMediaById(testMedia.getId());

        assertNull(media);
    }

    @Test
    public void deleteAllMedia() {
        //testing method
        mediaService.deleteAllMedia();

        List<Media> mediaList = mediaService.getAllMedia();

        assertNotNull(mediaList);
        assertTrue(mediaList.isEmpty());
    }

    //helper methods
    private void createTestObjects() {
        testUser = createTestUser();
        testCategory = createTestCategory();
        testEvent = createTestEvent();
        testMedia = createTestMedia();
        testMediaType = createTestMediaType();
    }

    private void insertTestObjectsIntoDB() {
        //insert user into db and get generated id
        testUser = userService.addAccount(testUser);

        //insert category into db and get generated id
        testCategory = categoryService.addCategory(testCategory);

        //insert event into db and get generated id
        testEvent.setCategory(testCategory);
        testEvent = eventService.createEvent(testEvent);

        //insert media type into db
        testMediaType = mediaTypeService.addMediaType(testMediaType);

        //insert media into db and get generated id
        testMedia.setUploaderId(testUser.getId());
        testMedia.setEventId(testEvent.getId());
        testMedia.setType(testMediaType);

        testMedia = mediaService.addMedia(testMedia);
    }

    private void deleteTestObjects() {
        testMedia = null;
        testEvent = null;
        testCategory = null;
        testUser = null;
    }

    private void deleteTestRecordsFromDB() {
        mediaService.deleteAllMedia();
        eventService.deleteAllEvents();
        categoryService.deleteAllCategories();
        userService.getAllUsers();
    }
}
