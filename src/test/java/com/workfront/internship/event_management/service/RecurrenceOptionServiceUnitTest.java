package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.dao.RecurrenceOptionDAO;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.RecurrenceOption;
import org.junit.*;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualRecurrenceOptions;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class RecurrenceOptionServiceUnitTest {

    private RecurrenceOptionService recurrenceOptionService;
    private RecurrenceOptionDAO recurrenceOptionDAO;
    private RecurrenceOption testRecurrenceOption;
    private List<RecurrenceOption> testRecurrenceOptionList;

    @Before
    public void setUp() {
        //create test user object
        testRecurrenceOption = TestObjectCreator.createTestRecurrenceOption();
        testRecurrenceOptionList = new ArrayList<>();
        testRecurrenceOptionList.add(testRecurrenceOption);

        recurrenceOptionService = spy(new RecurrenceOptionServiceImpl());
        recurrenceOptionDAO = Mockito.mock(RecurrenceOptionDAO.class);
        Whitebox.setInternalState(recurrenceOptionService, "recurrenceOptionDAO", recurrenceOptionDAO);
    }

    @After
    public void tearDown() {
        testRecurrenceOption = null;
        recurrenceOptionDAO = null;
        recurrenceOptionService = null;
    }

    //Testing addRecurrenceOption method
    @Test(expected = OperationFailedException.class)
    public void addRecurrenceOption_Invalid_Option() {
        testRecurrenceOption.setTitle("");

        //method under test
        recurrenceOptionService.addRecurrenceOption(testRecurrenceOption);
    }

    @Test(expected = OperationFailedException.class)
    public void addRecurrenceOption_Failed_Duplicate() throws DuplicateEntryException, DAOException {
        when(recurrenceOptionDAO.addRecurrenceOption(testRecurrenceOption)).thenThrow(DuplicateEntryException.class);

        //method under test
        recurrenceOptionService.addRecurrenceOption(testRecurrenceOption);
    }

    @Test(expected = OperationFailedException.class)
    public void addRecurrenceOption_Failed_DB_Error() throws DuplicateEntryException, DAOException {
        when(recurrenceOptionDAO.addRecurrenceOption(testRecurrenceOption)).thenThrow(DAOException.class);

        //method under test
        recurrenceOptionService.addRecurrenceOption(testRecurrenceOption);
    }

    @Test
    public void addRecurrenceOption_Success() throws DuplicateEntryException, DAOException {
        testRecurrenceOption.setId(VALID_ID);
        when(recurrenceOptionDAO.addRecurrenceOption(testRecurrenceOption)).thenReturn(VALID_ID);

        //method under test
        RecurrenceOption actualRecurrenceOption = recurrenceOptionService.addRecurrenceOption(testRecurrenceOption);

        assertEqualRecurrenceOptions(actualRecurrenceOption, testRecurrenceOption);
    }

    //Testing addRecurrenceOptions method
    @Test(expected = OperationFailedException.class)
    public void addRecurrenceOptions_Empty_Collection() {
        //method under test
        recurrenceOptionService.addRecurrenceOptions(new ArrayList<RecurrenceOption>());
    }

    @Test(expected = OperationFailedException.class)
    public void addRecurrenceOptions_Invalid_Option() {
        testRecurrenceOptionList.get(0).setTitle("");

        //method under test
        recurrenceOptionService.addRecurrenceOptions(testRecurrenceOptionList);
    }

    @Test(expected = OperationFailedException.class)
    public void addRecurrenceOptions_Failed_Duplicate() throws DuplicateEntryException, DAOException {
        doThrow(DuplicateEntryException.class).when(recurrenceOptionDAO).addRecurrenceOptions(testRecurrenceOptionList);

        //method under test
        recurrenceOptionService.addRecurrenceOptions(testRecurrenceOptionList);
    }

    @Test(expected = OperationFailedException.class)
    public void addRecurrenceOptions_Failed_DB_Error() throws DuplicateEntryException, DAOException {
        doThrow(DAOException.class).when(recurrenceOptionDAO).addRecurrenceOptions(testRecurrenceOptionList);

        //method under test
        recurrenceOptionService.addRecurrenceOptions(testRecurrenceOptionList);
    }

    @Test
    public void addRecurrenceOptions_Success() throws DuplicateEntryException, DAOException {
        //method under test
        recurrenceOptionService.addRecurrenceOptions(testRecurrenceOptionList);

        verify(recurrenceOptionDAO).addRecurrenceOptions(testRecurrenceOptionList);
    }

    //Testing getRecurrenceOptionById method
    @Test(expected = OperationFailedException.class)
    public void getRecurrenceOptionById_Invalid_Id() {
        //method under test
        recurrenceOptionService.getRecurrenceOption(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getRecurrenceOptionById_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceOptionDAO).getRecurrenceOptionById(TestObjectCreator.VALID_ID);

        //method under test
        recurrenceOptionService.getRecurrenceOption(TestObjectCreator.VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getRecurrenceOptionById_Not_Found() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(recurrenceOptionDAO).getRecurrenceOptionById(NON_EXISTING_ID);

        //method under test
        recurrenceOptionService.getRecurrenceOption(NON_EXISTING_ID);
    }

    @Test
    public void getRecurrenceOptionById_Success() throws ObjectNotFoundException, DAOException {
        testRecurrenceOption.setId(TestObjectCreator.VALID_ID);
        when(recurrenceOptionDAO.getRecurrenceOptionById(TestObjectCreator.VALID_ID)).thenReturn(testRecurrenceOption);

        //method under test
        RecurrenceOption actualRecurrenceOption = recurrenceOptionService.getRecurrenceOption(TestObjectCreator.VALID_ID);
        assertEqualRecurrenceOptions(actualRecurrenceOption, testRecurrenceOption);
    }

    //Testing getRecurrenceOptionByRecurrenceType method
    @Test(expected = OperationFailedException.class)
    public void getRecurrenceOptionsByRecurrenceType_Invalid_Id() {
        //method under test
        recurrenceOptionService.getRecurrenceOptionsByRecurrenceType(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getRecurrenceOptionsByRecurrenceType_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceOptionDAO).getRecurrenceOptionsByRecurrenceType(TestObjectCreator.VALID_ID);

        //method under test
        recurrenceOptionService.getRecurrenceOptionsByRecurrenceType(TestObjectCreator.VALID_ID);
    }

    @Test
    public void getRecurrenceOptionsByRecurrenceType_Success() throws ObjectNotFoundException, DAOException {
        when(recurrenceOptionDAO.getRecurrenceOptionsByRecurrenceType(TestObjectCreator.VALID_ID)).thenReturn(testRecurrenceOptionList);

        //method under test
        List<RecurrenceOption> actualRecurrenceOptionList = recurrenceOptionService.getRecurrenceOptionsByRecurrenceType(TestObjectCreator.VALID_ID);
        assertNotNull(actualRecurrenceOptionList);
        assertFalse(actualRecurrenceOptionList.isEmpty());
        assertEquals(actualRecurrenceOptionList.size(), 1);
        assertEqualRecurrenceOptions(actualRecurrenceOptionList.get(0), testRecurrenceOption);
    }

    //Testing editRecurrenceOption method
    @Test(expected = OperationFailedException.class)
    public void editRecurrenceOption_Invalid_Option() {
        testRecurrenceOption.setRecurrenceTypeId(0);

        //method under test
        recurrenceOptionService.editRecurrenceOption(testRecurrenceOption);
    }

    @Test(expected = OperationFailedException.class)
    public void editRecurrenceOption_Not_Found() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(ObjectNotFoundException.class).when(recurrenceOptionDAO).updateRecurrenceOption(testRecurrenceOption);

        //method under test
        recurrenceOptionService.editRecurrenceOption(testRecurrenceOption);
    }

    @Test(expected = OperationFailedException.class)
    public void editRecurrenceOption_Failed_Duplicate() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(DuplicateEntryException.class).when(recurrenceOptionDAO).updateRecurrenceOption(testRecurrenceOption);

        //method under test
        recurrenceOptionService.editRecurrenceOption(testRecurrenceOption);
    }

    @Test(expected = OperationFailedException.class)
    public void editRecurrenceOption_Failed_DB_Error() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(DAOException.class).when(recurrenceOptionDAO).updateRecurrenceOption(testRecurrenceOption);

        //method under test
        recurrenceOptionService.editRecurrenceOption(testRecurrenceOption);
    }

    @Test
    public void editRecurrenceOption_Success() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        //method under test
        recurrenceOptionService.editRecurrenceOption(testRecurrenceOption);

        verify(recurrenceOptionDAO).updateRecurrenceOption(testRecurrenceOption);
    }

    @Test
    public void editRecurrenceOptionList_Empty_List() throws DAOException, ObjectNotFoundException {
        //method under test
        recurrenceOptionService.editRecurrenceOptionList(VALID_ID, new ArrayList<RecurrenceOption>());

        verify(recurrenceOptionDAO).deleteRecurrenceOptionsByRecurrenceType(VALID_ID); //??????

    }

    @Test
    public void editRecurrenceOptionList_InsertOptions_EmptyDBList() throws DAOException, ObjectNotFoundException {
        when(recurrenceOptionService.getRecurrenceOptionsByRecurrenceType(TestObjectCreator.VALID_ID)).thenReturn(null);

        //method under test
        recurrenceOptionService.editRecurrenceOptionList(VALID_ID, testRecurrenceOptionList);

        // verify(recurrenceOptionDAO).deleteRecurrenceOptionsByRecurrenceType(VALID_ID);
        verify(recurrenceOptionService).addRecurrenceOptions(testRecurrenceOptionList);

    }

    @Test
    public void editRecurrenceOptionList_InsertOption_NonEmptyDBList() throws DAOException, ObjectNotFoundException {
        //db list does not contains testRecurrenceOption object, method should add it to db
        List<RecurrenceOption> dbList = new ArrayList<>();
        RecurrenceOption dbOption = createTestRecurrenceOption().setId(VALID_ID);
        dbList.add(dbOption);

        when(recurrenceOptionService.getRecurrenceOptionsByRecurrenceType(TestObjectCreator.VALID_ID)).thenReturn(dbList);

        //method under test
        recurrenceOptionService.editRecurrenceOptionList(VALID_ID, testRecurrenceOptionList);

        // verify(recurrenceOptionDAO).deleteRecurrenceOptionsByRecurrenceType(VALID_ID);
        verify(recurrenceOptionService).addRecurrenceOption(testRecurrenceOptionList.get(0));
    }

    @Test
    public void editRecurrenceOptionList_UpdateOption_NonEmptyDBList() throws DAOException, ObjectNotFoundException {
        //create db list, that contains recurrence option with the same id as testRecurrenceOption
        testRecurrenceOption.setId(VALID_ID);
        RecurrenceOption dbOption = new RecurrenceOption(testRecurrenceOption);
        dbOption.setTitle("changed title");

        List<RecurrenceOption> dbList = new ArrayList<>();
        dbList.add(dbOption);

        when(recurrenceOptionService.getRecurrenceOptionsByRecurrenceType(TestObjectCreator.VALID_ID)).thenReturn(dbList);

        //method under test
        recurrenceOptionService.editRecurrenceOptionList(VALID_ID, testRecurrenceOptionList);

        // verify(recurrenceOptionDAO).deleteRecurrenceOptionsByRecurrenceType(VALID_ID);
        verify(recurrenceOptionService).editRecurrenceOption(testRecurrenceOption);
    }

    @Test
    public void editRecurrenceOptionList_DeleteOption_FromDB() throws DAOException, ObjectNotFoundException {
        //create db list, that contains recurrence option with the same id as testRecurrenceOption
        List<RecurrenceOption> dbList = new ArrayList<>();
        RecurrenceOption dbOption = createTestRecurrenceOption().setId(VALID_ID);
        dbList.add(dbOption);

        when(recurrenceOptionService.getRecurrenceOptionsByRecurrenceType(TestObjectCreator.VALID_ID)).thenReturn(dbList);

        //method under test
        recurrenceOptionService.editRecurrenceOptionList(VALID_ID, testRecurrenceOptionList);

        // verify(recurrenceOptionDAO).deleteRecurrenceOptionsByRecurrenceType(VALID_ID);
        verify(recurrenceOptionService).deleteRecurrenceOption(dbOption.getId());
    }


    //Testing deleteRecurrenceOptionByRecurrenceType method
    @Test(expected = OperationFailedException.class)
    public void deleteRecurrenceOptionsByRecurrenceType_Invalid_Id() {
        //method under test
        recurrenceOptionService.deleteRecurrenceOptionsByRecurrenceType(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteRecurrenceOptionsByRecurrenceType_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceOptionDAO).deleteRecurrenceOptionsByRecurrenceType(VALID_ID);

        //method under test
        recurrenceOptionService.deleteRecurrenceOptionsByRecurrenceType(VALID_ID);
    }

    @Test
    public void deleteRecurrenceOptionsByRecurrenceType_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        recurrenceOptionService.deleteRecurrenceOptionsByRecurrenceType(VALID_ID);

        verify(recurrenceOptionDAO).deleteRecurrenceOptionsByRecurrenceType(VALID_ID);
    }

    //Testing deleteRecurrenceOption method
    @Test(expected = OperationFailedException.class)
    public void deleteRecurrenceOption_Invalid_Id() {
        //method under test
        recurrenceOptionService.deleteRecurrenceOption(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteRecurrenceOption_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceOptionDAO).deleteRecurrenceOption(VALID_ID);

        //method under test
        recurrenceOptionService.deleteRecurrenceOption(VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteRecurrenceOption_Not_Found() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(recurrenceOptionDAO).deleteRecurrenceOption(NON_EXISTING_ID);

        //method under test
        recurrenceOptionService.deleteRecurrenceOption(NON_EXISTING_ID);
    }

    @Test
    public void deleteRecurrenceOption_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        recurrenceOptionService.deleteRecurrenceOption(VALID_ID);

        verify(recurrenceOptionDAO).deleteRecurrenceOption(VALID_ID);
    }

    //Testing getAllRecurrenceOptions method
    @Test
    public void getAllRecurrenceOptions_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        recurrenceOptionService.getAllRecurrenceOptions();

        verify(recurrenceOptionDAO).getAllRecurrenceOptions();
    }

    @Test(expected = OperationFailedException.class)
    public void getAllRecurrenceOptions_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceOptionDAO).getAllRecurrenceOptions();

        //method under test
        recurrenceOptionService.getAllRecurrenceOptions();
    }

    //Testing deleteAllRecurrenceOptions method
    @Test
    public void deleteAllRecurrenceOptions_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        recurrenceOptionService.deleteAllRecurrenceOptions();

        verify(recurrenceOptionDAO).deleteAllRecurrenceOptions();
    }

    @Test(expected = OperationFailedException.class)
    public void deleteAllRecurrenceOptions_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceOptionDAO).deleteAllRecurrenceOptions();

        //method under test
        recurrenceOptionService.deleteAllRecurrenceOptions();
    }
}
