package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.RecurrenceOption;
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

import static com.workfront.internship.event_management.AssertionHelper.assertEqualRecurrenceOptions;
import static com.workfront.internship.event_management.TestObjectCreator.createTestRecurrenceOption;
import static com.workfront.internship.event_management.TestObjectCreator.createTestRecurrenceType;
import static junit.framework.TestCase.*;

/**
 * Created by Hermine Turshujyan 8/30/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class RecurrenceOptionIntegrationTest {

    @Autowired
    private RecurrenceOptionService recurrenceOptionService;
    @Autowired
    private RecurrenceTypeService recurrenceTypeService;

    private RecurrenceOption testRecurrenceOption;
    private RecurrenceType testRecurrenceType;

    @Before
    public void setUp() {

        //create test recurrence type and option objects
        testRecurrenceType = createTestRecurrenceType();
        testRecurrenceOption = createTestRecurrenceOption();

        //insert into db
        testRecurrenceType = recurrenceTypeService.addRecurrenceType(testRecurrenceType);

        testRecurrenceOption.setRecurrenceTypeId(testRecurrenceType.getId());
        testRecurrenceOption = recurrenceOptionService.addRecurrenceOption(testRecurrenceOption);
    }

    @After
    public void tearDown() throws DAOException {
        //delete test records from db
        recurrenceTypeService.deleteAllRecurrenceTypes();
        recurrenceOptionService.deleteAllRecurrenceOptions();

        //delete test objects
        testRecurrenceOption = null;
        testRecurrenceType = null;
    }

    @Test
    public void addRecurrenceOption() {
        //test record already inserted in setup, read record from db
        RecurrenceOption recurrenceOption = recurrenceOptionService.getRecurrenceOption(testRecurrenceOption.getId());

        assertNotNull(recurrenceOption);
        assertEqualRecurrenceOptions(recurrenceOption, testRecurrenceOption);
    }

    @Test
    public void getAllRecurrenceOptions() {
        //test method
        List<RecurrenceOption> recurrenceOption = recurrenceOptionService.getAllRecurrenceOptions();

        assertNotNull(recurrenceOption);
        assertFalse(recurrenceOption.isEmpty());
        assertEquals(recurrenceOption.size(), 1);
        assertEqualRecurrenceOptions(recurrenceOption.get(0), testRecurrenceOption);
    }

    @Test
    public void getRecurrenceOptionById() {
        //test method
        RecurrenceOption recurrenceOption = recurrenceOptionService.getRecurrenceOption(testRecurrenceOption.getId());

        assertNotNull(recurrenceOption);
        assertEqualRecurrenceOptions(recurrenceOption, testRecurrenceOption);
    }

    @Test
    public void getRecurrenceOptionsByRecurrenceType() {
        //test method
        List<RecurrenceOption> recurrenceOption = recurrenceOptionService.getRecurrenceOptionsByRecurrenceType(testRecurrenceType.getId());

        assertNotNull(recurrenceOption);
        assertFalse(recurrenceOption.isEmpty());
        assertEquals(recurrenceOption.size(), 1);
        assertEqualRecurrenceOptions(recurrenceOption.get(0), testRecurrenceOption);
    }

    @Test
    public void updateRecurrenceOption() {
        //update test object
        testRecurrenceOption.setTitle("Changed test title");

        //test method
        recurrenceOptionService.editRecurrenceOption(testRecurrenceOption);

        //read updated record from db
        RecurrenceOption updatedRecurrenceOption = recurrenceOptionService.getRecurrenceOption(testRecurrenceOption.getId());

        assertNotNull(updatedRecurrenceOption);
        assertEqualRecurrenceOptions(updatedRecurrenceOption, testRecurrenceOption);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteRecurrenceOption() throws DAOException, ObjectNotFoundException {
        //testing method
        boolean success = recurrenceOptionService.deleteRecurrenceOption(testRecurrenceOption.getId());
        assertTrue(success);

        recurrenceOptionService.getRecurrenceOption(testRecurrenceOption.getId());
    }


    @Test
    public void deleteRepeatOptionsByRecurrenceType() {
        //testing method
        recurrenceOptionService.deleteRecurrenceOptionsByRecurrenceType(testRecurrenceOption.getRecurrenceTypeId());

        List<RecurrenceOption> recurrenceOptionList = recurrenceOptionService.getRecurrenceOptionsByRecurrenceType(testRecurrenceOption.getRecurrenceTypeId());

        assertNotNull(recurrenceOptionList);
        assertTrue(recurrenceOptionList.isEmpty());
    }

    @Test
    public void deleteAllRecurrenceOptions() {
        //testing method
        recurrenceOptionService.deleteAllRecurrenceOptions();

        List<RecurrenceOption> recurrenceOptionList = recurrenceOptionService.getAllRecurrenceOptions();

        assertNotNull(recurrenceOptionList);
        assertTrue(recurrenceOptionList.isEmpty());
    }
}
