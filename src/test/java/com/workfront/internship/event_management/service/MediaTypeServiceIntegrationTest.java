package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.MediaType;
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

import static com.workfront.internship.event_management.AssertionHelper.assertEqualMediaTypes;
import static com.workfront.internship.event_management.TestObjectCreator.createTestMediaType;
import static junit.framework.TestCase.*;

/**
 * Created by Hermine Turshujyan 8/30/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class MediaTypeServiceIntegrationTest {

    @Autowired
    private MediaTypeService mediaTypeService;

    private MediaType testMediaType;


    @Before
    public void setUp() {
        //create test media type
        testMediaType = createTestMediaType();

        //insert test user into db, get generated id
        testMediaType = mediaTypeService.addMediaType(testMediaType);
    }

    @After
    public void tearDown() {
        //delete test record from db
        mediaTypeService.deleteAllMediaTypes();

        //delete test media type object
        testMediaType = null;
    }

    @Test
    public void addMediaType() {
        //test media type already inserted in setup, read record by id
        MediaType mediaType = mediaTypeService.getMediaTypeById(testMediaType.getId());

        assertNotNull(mediaType);
        assertEqualMediaTypes(mediaType, testMediaType);
    }

    @Test
    public void getAllMediaTypes() {
        //method under test
        List<MediaType> mediaTypeList = mediaTypeService.getAllMediaTypes();

        assertNotNull(mediaTypeList);
        assertFalse(mediaTypeList.isEmpty());
        assertEqualMediaTypes(mediaTypeList.get(0), testMediaType);
    }

    @Test
    public void getMediaTypeById() {
        //method under test
        MediaType mediaType = mediaTypeService.getMediaTypeById(testMediaType.getId());

        assertNotNull(mediaType);
        assertEqualMediaTypes(mediaType, testMediaType);
    }

    @Test
    public void updateMediaType() {
        //create new media type with the same id
        testMediaType.setTitle("new changed title");

        //test method
        mediaTypeService.editMediaType(testMediaType);

        //read updated record from db
        MediaType mediaType = mediaTypeService.getMediaTypeById(testMediaType.getId());

        assertNotNull(mediaType);
        assertEqualMediaTypes(mediaType, testMediaType);
    }


    @Test(expected = ObjectNotFoundException.class)
    public void deleteMediaType() {
        //testing method
        boolean success = mediaTypeService.deleteMediaType(testMediaType.getId());
        assertTrue(success);

        mediaTypeService.getMediaTypeById(testMediaType.getId());
    }

    @Test
    public void deleteAllMediaTypes() {
        //testing method
        mediaTypeService.deleteAllMediaTypes();

        List<MediaType> mediaTypeList = mediaTypeService.getAllMediaTypes();

        assertNotNull(mediaTypeList);
        assertTrue(mediaTypeList.isEmpty());
    }
}
