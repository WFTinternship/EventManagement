package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.RecurrenceOption;
import com.workfront.internship.event_management.model.RecurrenceType;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualRecurrenceOptions;
import static junit.framework.TestCase.*;

/**
 * Created by Hermine Turshujyan 7/16/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class RecurrenceOptionDAOIntegrationTest {

    @Autowired
    private RecurrenceOptionDAO recurrenceOptionDAO;
    @Autowired
    private RecurrenceTypeDAO recurrenceTypeDAO;

    private RecurrenceOption testRecurrenceOption;
    private RecurrenceType testRecurrenceType;


    @Before
    public void setUp() throws DuplicateEntryException, DAOException {

        //create test recurrence type and option objects
        testRecurrenceType = TestObjectCreator.createTestRecurrenceType();
        testRecurrenceOption = TestObjectCreator.createTestRecurrenceOption();

        //insert into db
        int recurrenceTypeId = recurrenceTypeDAO.addRecurrenceType(testRecurrenceType);
        testRecurrenceType.setId(recurrenceTypeId);

        testRecurrenceOption.setRecurrenceTypeId(recurrenceTypeId);
        int optionId = recurrenceOptionDAO.addRecurrenceOption(testRecurrenceOption);
        testRecurrenceOption.setId(optionId);
    }

    @After
    public void tearDown() throws DAOException {
        //delete test records from db
        recurrenceTypeDAO.deleteAllRecurrenceTypes();
        recurrenceOptionDAO.deleteAllRecurrenceOptions();

        //delete test objects
        testRecurrenceOption = null;
        testRecurrenceType = null;
    }

    @Test
    public void addRecurrenceOption_Success() throws ObjectNotFoundException, DAOException {
        //test record already inserted in setup, read record from db
        RecurrenceOption recurrenceOption = recurrenceOptionDAO.getRecurrenceOptionById(testRecurrenceOption.getId());

        assertNotNull(recurrenceOption);
        assertEqualRecurrenceOptions(recurrenceOption, testRecurrenceOption);
    }

    @Test(expected = DuplicateEntryException.class)
    public void addRecurrenceOption_Duplicate_Entry() throws DuplicateEntryException, DAOException {

        //test record already inserted into db, insert dublicate entry
        recurrenceOptionDAO.addRecurrenceOption(testRecurrenceOption);
    }

    @Test
    public void getAllRecurrenceOptions_Found() throws DAOException {
        //test method
        List<RecurrenceOption> recurrenceOption = recurrenceOptionDAO.getAllRecurrenceOptions();

        assertNotNull(recurrenceOption);
        assertFalse(recurrenceOption.isEmpty());
        assertEquals(recurrenceOption.size(), 1);
        assertEqualRecurrenceOptions(recurrenceOption.get(0), testRecurrenceOption);
    }

    @Test
    public void getAllRecurrenceOptions_Empty_List() throws DAOException, ObjectNotFoundException {
        //delete inserted tet record
        recurrenceOptionDAO.deleteRecurrenceOption(testRecurrenceOption.getId());

        //test method
        List<RecurrenceOption> recurrenceOption = recurrenceOptionDAO.getAllRecurrenceOptions();

        assertNotNull(recurrenceOption);
        assertTrue(recurrenceOption.isEmpty());
    }

    @Test
    public void getRecurrenceOptionById_Found() throws ObjectNotFoundException, DAOException {
        //test method
        RecurrenceOption recurrenceOption = recurrenceOptionDAO.getRecurrenceOptionById(testRecurrenceOption.getId());

        assertNotNull(recurrenceOption);
        assertEqualRecurrenceOptions(recurrenceOption, testRecurrenceOption);
    }

    @Test
    public void getRecurrenceOptionById_Not_Found() throws ObjectNotFoundException, DAOException {
        //test method
        RecurrenceOption recurrenceOption = recurrenceOptionDAO.getRecurrenceOptionById(TestObjectCreator.NON_EXISTING_ID);
        assertNull(recurrenceOption);
    }

    @Test
    public void getRecurrenceOptionsByRecurrenceType_Found() throws DAOException {
        //test method
        List<RecurrenceOption> recurrenceOption = recurrenceOptionDAO.getRecurrenceOptionsByRecurrenceType(testRecurrenceType.getId());

        assertNotNull(recurrenceOption);
        assertFalse(recurrenceOption.isEmpty());
        assertEquals(recurrenceOption.size(), 1);
        assertEqualRecurrenceOptions(recurrenceOption.get(0), testRecurrenceOption);
    }

    @Test
    public void getRecurrenceOptionsByRecurrenceType_Empty_List() throws DAOException {
        //test method
        List<RecurrenceOption> recurrenceOption = recurrenceOptionDAO.getRecurrenceOptionsByRecurrenceType(TestObjectCreator.NON_EXISTING_ID);

        assertNotNull(recurrenceOption);
        assertTrue(recurrenceOption.isEmpty());
    }

    @Test
    public void updateRecurrenceOption_Success() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        //update test object
        testRecurrenceOption.setTitle("Changed test title");

        //test method
        recurrenceOptionDAO.updateRecurrenceOption(testRecurrenceOption);

        //read updated record from db
        RecurrenceOption updatedRecurrenceOption = recurrenceOptionDAO.getRecurrenceOptionById(testRecurrenceOption.getId());

        assertNotNull(updatedRecurrenceOption);
        assertEqualRecurrenceOptions(updatedRecurrenceOption, testRecurrenceOption);
    }

    @Test
    public void updateRepeatOption_Not_Found() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        //create new recurrence option with non-existing id
        RecurrenceOption recurrenceOption = TestObjectCreator.createTestRecurrenceOption();

        //test method
        boolean success = recurrenceOptionDAO.updateRecurrenceOption(recurrenceOption);
        assertFalse(success);
    }

    @Test
    public void deleteRecurrenceOption_Success() throws DAOException, ObjectNotFoundException {
        //testing method
        recurrenceOptionDAO.deleteRecurrenceOption(testRecurrenceOption.getId());

        RecurrenceOption recurrenceOption = recurrenceOptionDAO.getRecurrenceOptionById(testRecurrenceOption.getId());
        assertNull(recurrenceOption);
    }

    @Test
    public void deleteRecurrenceOption_Not_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        boolean success = recurrenceOptionDAO.deleteRecurrenceOption(TestObjectCreator.NON_EXISTING_ID);
        assertFalse(success);
    }

    @Test
    public void deleteRepeatOptionsByRecurrenceType_Success() throws DAOException, ObjectNotFoundException {
        //testing method
        recurrenceOptionDAO.deleteRecurrenceOptionsByRecurrenceType(testRecurrenceOption.getRecurrenceTypeId());

        List<RecurrenceOption> recurrenceOptionList = recurrenceOptionDAO.getRecurrenceOptionsByRecurrenceType(testRecurrenceOption.getRecurrenceTypeId());

        assertNotNull(recurrenceOptionList);
        assertTrue(recurrenceOptionList.isEmpty());
    }

    @Test
    public void deleteRepeatOptionsByRecurrenceType_Not_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        recurrenceOptionDAO.deleteRecurrenceOptionsByRecurrenceType(TestObjectCreator.NON_EXISTING_ID);
    }

    @Test
    public void deleteAllRecurrenceOptions_Success() throws DAOException {
        //testing method
        recurrenceOptionDAO.deleteAllRecurrenceOptions();

        List<RecurrenceOption> recurrenceOptionList = recurrenceOptionDAO.getAllRecurrenceOptions();

        assertNotNull(recurrenceOptionList);
        assertTrue(recurrenceOptionList.isEmpty());
    }

    @Test
    public void deleteAllRecurrenceOptions_Not_Found() throws DAOException, ObjectNotFoundException {
        //delete inserted record
        recurrenceOptionDAO.deleteRecurrenceOption(testRecurrenceOption.getId());

        //testing method
        recurrenceOptionDAO.deleteAllRecurrenceOptions();
    }
}
