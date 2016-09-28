package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.RecurrenceType;
import com.workfront.internship.event_management.spring.TestApplicationConfig;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualRecurrenceTypes;
import static com.workfront.internship.event_management.AssertionHelper.assertEqualRecurrenceTypesWithOptions;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static junit.framework.TestCase.*;

/**
 * Created by Hermine Turshujyan 7/11/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestApplicationConfig.class)
@ActiveProfiles("Test")
public class RecurrenceTypeDAOIntegrationTest {

    @Autowired
    private RecurrenceTypeDAO recurrenceTypeDAO;

    private RecurrenceType testRecurrenceType;

    @Before
    public void setUp() throws DuplicateEntryException, DAOException {
        //create test recurrence type
        testRecurrenceType = createTestRecurrenceTypeWithOptions();

        //insert test record into db and get generated id
        int recurrenceTypeId = recurrenceTypeDAO.addRecurrenceTypeWithOptions(testRecurrenceType);
        testRecurrenceType.setId(recurrenceTypeId);
    }

    @After
    public void tearDown() throws DAOException {
        //delete test record from db
        recurrenceTypeDAO.deleteAllRecurrenceTypes();

        //delete test recurrenceType object
        testRecurrenceType = null;
    }

    @Test
    public void addRecurrenceTypeWithOptions_Success() throws ObjectNotFoundException, DAOException {
        //test record already inserted in setup, read record by id
        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(testRecurrenceType.getId());

        assertNotNull(recurrenceType);
        assertEqualRecurrenceTypesWithOptions(recurrenceType, testRecurrenceType);
    }

    @Test
    public void addRecurrenceType_Success() throws DuplicateEntryException, DAOException, ObjectNotFoundException {
        //create recurrence type without options
        RecurrenceType newTestRecurrenceType = createTestRecurrenceType();
        int id = recurrenceTypeDAO.addRecurrenceType(newTestRecurrenceType);
        newTestRecurrenceType.setId(id);

        //read inserted record by id
        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(newTestRecurrenceType.getId());

        assertNotNull(recurrenceType);
        assertEqualRecurrenceTypes(recurrenceType, newTestRecurrenceType);
    }

    @Test(expected = DuplicateEntryException.class)
    public void addRecurrenceType_Duplicate_Entry() throws DuplicateEntryException, DAOException {
        //test recurrence type inserted in setup, insert duplicate entry
        recurrenceTypeDAO.addRecurrenceType(testRecurrenceType);
    }

    @Test
    public void getAllRecurrenceTypes_Found() throws DAOException {
        //test method
        List<RecurrenceType> recurrenceTypeList = recurrenceTypeDAO.getAllRecurrenceTypes();

        assertNotNull(recurrenceTypeList);
        assertFalse(recurrenceTypeList.isEmpty());
        assertEquals(recurrenceTypeList.size(), 1);

        assertEqualRecurrenceTypes(recurrenceTypeList.get(0), testRecurrenceType);
    }

    @Test
    public void getAllRecurrenceTypes_Empty_List() throws DAOException, ObjectNotFoundException {
        //delete inserted record from db
        recurrenceTypeDAO.deleteRecurrenceType(testRecurrenceType.getId());

        //test method
        List<RecurrenceType> recurrenceTypeList = recurrenceTypeDAO.getAllRecurrenceTypes();

        assertNotNull(recurrenceTypeList);
        assertTrue(recurrenceTypeList.isEmpty());
    }

    @Test
    public void getRecurrenceTypeById_Found() throws ObjectNotFoundException, DAOException {
        //test method (test record already inserted in seUp())
        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(testRecurrenceType.getId());

        assertNotNull(recurrenceType);
        assertEqualRecurrenceTypesWithOptions(recurrenceType, testRecurrenceType);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getRecurrenceTypeById_Not_Found() throws ObjectNotFoundException, DAOException {
        //test method
        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(NON_EXISTING_ID);
    }

    @Test
    public void updateRecurrenceType_Found() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        //update recurrence type info
        testRecurrenceType.setTitle("updated title")
                .setIntervalUnit("updated unit");

        //test method
        recurrenceTypeDAO.updateRecurrenceType(testRecurrenceType);

        //read updated record from db
        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(testRecurrenceType.getId());

        assertNotNull(recurrenceType);
        assertEqualRecurrenceTypes(recurrenceType, testRecurrenceType);
    }

    @Test
    public void updateRecurrenceType_Not_Found() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        //create new recurrence type with non-existing id
        RecurrenceType recurrenceType = createTestRecurrenceType();

        //test method
        boolean success = recurrenceTypeDAO.updateRecurrenceType(recurrenceType);
        assertFalse(success);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteRecurrenceType_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        recurrenceTypeDAO.deleteRecurrenceType(testRecurrenceType.getId());

        RecurrenceType recurrenceType = recurrenceTypeDAO.getRecurrenceTypeById(testRecurrenceType.getId());
    }

    @Test
    public void deleteRecurrenceType_Not_Found() throws DAOException, ObjectNotFoundException {
        //testing method
        boolean success = recurrenceTypeDAO.deleteRecurrenceType(NON_EXISTING_ID);
        assertFalse(success);
    }

    @Test
    public void deleteAllRecurrenceTypes_Found() throws DAOException {
        //testing method
        recurrenceTypeDAO.deleteAllRecurrenceTypes();

        List<RecurrenceType> recurrenceTypeList = recurrenceTypeDAO.getAllRecurrenceTypes();

        assertNotNull(recurrenceTypeList);
        assertTrue(recurrenceTypeList.isEmpty());
    }

    @Test
    public void deleteAllRecurrenceTypes_Not_Found() throws DAOException, ObjectNotFoundException {
        //delete inserted test record
        recurrenceTypeDAO.deleteRecurrenceType(testRecurrenceType.getId());

        //testing method
        recurrenceTypeDAO.deleteAllRecurrenceTypes();
    }
}
