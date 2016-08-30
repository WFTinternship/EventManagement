package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.RecurrenceType;
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

import static com.workfront.internship.event_management.AssertionHelper.assertEqualRecurrenceTypes;
import static com.workfront.internship.event_management.AssertionHelper.assertEqualRecurrenceTypesWithOptions;
import static com.workfront.internship.event_management.TestObjectCreator.createTestRecurrenceType;
import static com.workfront.internship.event_management.TestObjectCreator.createTestRecurrenceTypeWithOptions;
import static junit.framework.TestCase.*;

/**
 * Created by Hermine Turshujyan 8/30/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class RecurrenceTypeIntegrationTest {

    @Autowired
    private RecurrenceTypeService recurrenceTypeService;

    private RecurrenceType testRecurrenceType;

    @Before
    public void setUp() {
        //create test recurrence type
        testRecurrenceType = createTestRecurrenceTypeWithOptions();

        //insert test record into db and get generated id
        testRecurrenceType = recurrenceTypeService.addRecurrenceType(testRecurrenceType);
    }

    @After
    public void tearDown() {
        //delete test record from db
        recurrenceTypeService.deleteAllRecurrenceTypes();

        //delete test recurrenceType object
        testRecurrenceType = null;
    }

    @Test
    public void addRecurrenceType() {
        //create recurrence type without options
        RecurrenceType newTestRecurrenceType = createTestRecurrenceType();
        newTestRecurrenceType = recurrenceTypeService.addRecurrenceType(newTestRecurrenceType);

        //read inserted record by id
        RecurrenceType recurrenceType = recurrenceTypeService.getRecurrenceTypeById(newTestRecurrenceType.getId());

        assertNotNull(recurrenceType);
        assertEqualRecurrenceTypes(recurrenceType, newTestRecurrenceType);
    }

    @Test
    public void getAllRecurrenceTypes() {
        //test method
        List<RecurrenceType> recurrenceTypeList = recurrenceTypeService.getAllRecurrenceTypes();

        assertNotNull(recurrenceTypeList);
        assertFalse(recurrenceTypeList.isEmpty());
        assertEquals(recurrenceTypeList.size(), 1);
        assertEqualRecurrenceTypes(recurrenceTypeList.get(0), testRecurrenceType);
    }


    @Test
    public void getRecurrenceTypeById() {
        //test method (test record already inserted in seUp())
        RecurrenceType recurrenceType = recurrenceTypeService.getRecurrenceTypeById(testRecurrenceType.getId());

        assertNotNull(recurrenceType);
        assertEqualRecurrenceTypesWithOptions(recurrenceType, testRecurrenceType);
    }

    @Test
    public void updateRecurrenceType() {
        //update recurrence type info
        testRecurrenceType.setTitle("updated title")
                .setIntervalUnit("updated unit");

        //test method
        recurrenceTypeService.editRecurrenceType(testRecurrenceType);

        //read updated record from db
        RecurrenceType recurrenceType = recurrenceTypeService.getRecurrenceTypeById(testRecurrenceType.getId());

        assertNotNull(recurrenceType);
        assertEqualRecurrenceTypes(recurrenceType, testRecurrenceType);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteRecurrenceType() {
        //testing method
        boolean success = recurrenceTypeService.deleteRecurrenceType(testRecurrenceType.getId());
        assertTrue(success);

        recurrenceTypeService.getRecurrenceTypeById(testRecurrenceType.getId());
    }

    @Test
    public void deleteAllRecurrenceTypes() {
        //testing method
        recurrenceTypeService.deleteAllRecurrenceTypes();

        List<RecurrenceType> recurrenceTypeList = recurrenceTypeService.getAllRecurrenceTypes();

        assertNotNull(recurrenceTypeList);
        assertTrue(recurrenceTypeList.isEmpty());
    }
}
