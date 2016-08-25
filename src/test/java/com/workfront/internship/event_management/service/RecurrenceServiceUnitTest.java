package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.dao.RecurrenceDAO;
import com.workfront.internship.event_management.dao.RecurrenceDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Recurrence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualRecurrences;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class RecurrenceServiceUnitTest {

    private RecurrenceService recurrenceService;
    private RecurrenceDAO recurrenceDAO;
    private Recurrence testRecurrence;
    private List<Recurrence> testRecurrenceList;

    @Before
    public void setUp() {
        testRecurrence = TestObjectCreator.createTestRecurrence();
        testRecurrenceList = new ArrayList<>();
        testRecurrenceList.add(testRecurrence);

        recurrenceService = spy(new RecurrenceServiceImpl());
        recurrenceDAO = Mockito.mock(RecurrenceDAOImpl.class);
        Whitebox.setInternalState(recurrenceService, "recurrenceDAO", recurrenceDAO);
    }

    @After
    public void tearDown() {
        testRecurrence = null;
        testRecurrenceList = null;
        recurrenceDAO = null;
        recurrenceService = null;
    }

    //Testing addRecurrence method
    @Test(expected = OperationFailedException.class)
    public void addRecurrence_Invalid_Recurrence() {
        testRecurrence.setRecurrenceType(null);

        //method under test
        recurrenceService.addRecurrence(testRecurrence);
    }

    @Test(expected = OperationFailedException.class)
    public void addRecurrence_DB_Error() throws DuplicateEntryException, DAOException {
        when(recurrenceDAO.addRecurrence(testRecurrence)).thenThrow(DAOException.class);

        //method under test
        recurrenceService.addRecurrence(testRecurrence);
    }

    @Test
    public void addRecurrence_Success() throws DuplicateEntryException, DAOException {
        testRecurrence.setId(VALID_ID);
        when(recurrenceDAO.addRecurrence(testRecurrence)).thenReturn(VALID_ID);

        //method under test
        Recurrence actualRecurrence = recurrenceService.addRecurrence(testRecurrence);

        assertEqualRecurrences(actualRecurrence, testRecurrence);
    }

    //Testing addRecurrences method
    @Test(expected = OperationFailedException.class)
    public void addRecurrences_Empty_List() {
        //method under test
        recurrenceService.addRecurrences(new ArrayList<Recurrence>());
    }

    @Test(expected = OperationFailedException.class)
    public void addRecurrences_Failed_DB_Error() throws DuplicateEntryException, DAOException {
        doThrow(DAOException.class).when(recurrenceDAO).addRecurrences(testRecurrenceList);

        //method under test
        recurrenceService.addRecurrences(testRecurrenceList);
    }

    @Test
    public void addRecurrences_Success() throws DuplicateEntryException, DAOException {
        //method under test
        recurrenceService.addRecurrences(testRecurrenceList);

        verify(recurrenceDAO).addRecurrences(testRecurrenceList);
    }

    //Testing getRecurrenceById method
    @Test(expected = OperationFailedException.class)
    public void getRecurrenceById_Invalid_Id() {
        //method under test
        recurrenceService.getRecurrenceById(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getRecurrenceById_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceDAO).getRecurrenceById(TestObjectCreator.VALID_ID);

        //method under test
        recurrenceService.getRecurrenceById(TestObjectCreator.VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getRecurrenceById_Not_Found() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(recurrenceDAO).getRecurrenceById(NON_EXISTING_ID);

        //method under test
        recurrenceService.getRecurrenceById(NON_EXISTING_ID);
    }

    @Test
    public void getRecurrenceById_Success() throws ObjectNotFoundException, DAOException {
        testRecurrence.setId(TestObjectCreator.VALID_ID);
        when(recurrenceDAO.getRecurrenceById(TestObjectCreator.VALID_ID)).thenReturn(testRecurrence);

        //method under test
        Recurrence actualRecurrence = recurrenceService.getRecurrenceById(TestObjectCreator.VALID_ID);
        assertEqualRecurrences(actualRecurrence, testRecurrence);
    }

    //Testing getRecurrenceByEventId method
    @Test(expected = OperationFailedException.class)
    public void getRecurrencesByEventId_Invalid_Id() {
        //method under test
        recurrenceService.getRecurrencesByEventId(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getRecurrencesByEventId_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceDAO).getRecurrencesByEventId(TestObjectCreator.VALID_ID);

        //method under test
        recurrenceService.getRecurrencesByEventId(TestObjectCreator.VALID_ID);
    }

    @Test
    public void getRecurrencesByEventId_Success() throws ObjectNotFoundException, DAOException {
        when(recurrenceDAO.getRecurrencesByEventId(TestObjectCreator.VALID_ID)).thenReturn(testRecurrenceList);

        //method under test
        List<Recurrence> actualRecurrenceList = recurrenceService.getRecurrencesByEventId(TestObjectCreator.VALID_ID);
        assertNotNull(actualRecurrenceList);
        assertFalse(actualRecurrenceList.isEmpty());
        assertEquals(actualRecurrenceList.size(), 1);
        assertEqualRecurrences(actualRecurrenceList.get(0), testRecurrence);
    }

    //Testing editRecurrence method
    @Test(expected = OperationFailedException.class)
    public void editRecurrence_Invalid_Option() {
        testRecurrence.setRecurrenceType(null);

        //method under test
        recurrenceService.editRecurrence(testRecurrence);
    }

    @Test(expected = OperationFailedException.class)
    public void editRecurrence_Not_Found() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(ObjectNotFoundException.class).when(recurrenceDAO).updateRecurrence(testRecurrence);

        //method under test
        recurrenceService.editRecurrence(testRecurrence);
    }

    @Test(expected = OperationFailedException.class)
    public void editRecurrence_DB_Error() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(DAOException.class).when(recurrenceDAO).updateRecurrence(testRecurrence);

        //method under test
        recurrenceService.editRecurrence(testRecurrence);
    }

    @Test
    public void editRecurrence_Success() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        //method under test
        recurrenceService.editRecurrence(testRecurrence);

        verify(recurrenceDAO).updateRecurrence(testRecurrence);
    }

    @Test
    public void editRecurrenceList_Empty_List() throws DAOException, ObjectNotFoundException {
        //method under test
        recurrenceService.editRecurrenceList(VALID_ID, new ArrayList<Recurrence>());

        verify(recurrenceDAO).deleteRecurrencesByEventId(VALID_ID); //??????
    }

    @Test
    public void editRecurrenceList_InsertRecurrences_EmptyDBList() throws DAOException, ObjectNotFoundException {
        when(recurrenceService.getRecurrencesByEventId(VALID_ID)).thenReturn(null);

        //method under test
        recurrenceService.editRecurrenceList(VALID_ID, testRecurrenceList);

        verify(recurrenceService).addRecurrences(testRecurrenceList);
    }

    @Test
    public void editRecurrenceList_InsertRecurrence_NonEmptyDBList() throws DAOException, ObjectNotFoundException {
        //db list does not contains testRecurrenceOption object, method should add it to db
        List<Recurrence> dbList = new ArrayList<>();
        Recurrence dbRecurrence = createTestRecurrence().setId(100);
        dbList.add(dbRecurrence);

        testRecurrence.setId(VALID_ID);

        when(recurrenceService.getRecurrencesByEventId(TestObjectCreator.VALID_ID)).thenReturn(dbList);

        //method under test
        recurrenceService.editRecurrenceList(VALID_ID, testRecurrenceList);

        // verify(recurrenceOptionDAO).deleteRecurrenceOptionsByRecurrenceType(VALID_ID);
        verify(recurrenceService).addRecurrence(testRecurrenceList.get(0));
    }

    @Test
    public void editRecurrenceList_UpdateRecurrence_NonEmptyDBList() throws DAOException, ObjectNotFoundException {
        //create db list, that contains recurrence option with the same id as testRecurrenceOption
        testRecurrence.setId(VALID_ID);
        Recurrence dbRecurrence = new Recurrence(testRecurrence);
        dbRecurrence.setRepeatInterval(100);

        List<Recurrence> dbList = new ArrayList<>();
        dbList.add(dbRecurrence);

        when(recurrenceService.getRecurrencesByEventId(TestObjectCreator.VALID_ID)).thenReturn(dbList);

        //method under test
        recurrenceService.editRecurrenceList(VALID_ID, testRecurrenceList);

        // verify(recurrenceOptionDAO).deleteRecurrenceOptionsByRecurrenceType(VALID_ID);
        verify(recurrenceService).editRecurrence(testRecurrence);
    }

    @Test
    public void editRecurrenceList_DeleteRecurrence_FromDB() throws DAOException, ObjectNotFoundException {
        //create db list, that contains recurrence option with the same id as testRecurrenceOption
        List<Recurrence> dbList = new ArrayList<>();
        Recurrence dbRecurrence = createTestRecurrence().setId(VALID_ID);
        dbList.add(dbRecurrence);

        when(recurrenceService.getRecurrencesByEventId(TestObjectCreator.VALID_ID)).thenReturn(dbList);

        //method under test
        recurrenceService.editRecurrenceList(VALID_ID, testRecurrenceList);

        verify(recurrenceService).deleteRecurrence(dbRecurrence.getId());
    }


    //Testing deleteRecurrencesByEventId method
    @Test(expected = OperationFailedException.class)
    public void deleteRecurrencesByEventId_Invalid_Id() {
        //method under test
        recurrenceService.deleteRecurrencesByEventId(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteRecurrencesByEventId_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceDAO).deleteRecurrencesByEventId(VALID_ID);

        //method under test
        recurrenceService.deleteRecurrencesByEventId(VALID_ID);
    }

    @Test
    public void deleteRecurrencesByEventId_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        recurrenceService.deleteRecurrencesByEventId(VALID_ID);

        verify(recurrenceDAO).deleteRecurrencesByEventId(VALID_ID);
    }

    //Testing deleteRecurrence method
    @Test(expected = OperationFailedException.class)
    public void deleteRecurrenceOption_Invalid_Id() {
        //method under test
        recurrenceService.deleteRecurrence(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteRecurrenceOption_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceDAO).deleteRecurrence(VALID_ID);

        //method under test
        recurrenceService.deleteRecurrence(VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteRecurrenceOption_Not_Found() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(recurrenceDAO).deleteRecurrence(NON_EXISTING_ID);

        //method under test
        recurrenceService.deleteRecurrence(NON_EXISTING_ID);
    }

    @Test
    public void deleteRecurrenceOption_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        recurrenceService.deleteRecurrence(VALID_ID);

        verify(recurrenceDAO).deleteRecurrence(VALID_ID);
    }

    //Testing getAllRecurrences method
    @Test
    public void getAllRecurrences_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        recurrenceService.getAllRecurrences();

        verify(recurrenceDAO).getAllRecurrences();
    }

    @Test(expected = OperationFailedException.class)
    public void getAllRecurrences_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceDAO).getAllRecurrences();

        //method under test
        recurrenceService.getAllRecurrences();
    }

    //Testing deleteAllRecurrences method
    @Test
    public void deleteAllEventRecurrences_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        recurrenceService.deleteAllRecurrences();

        verify(recurrenceDAO).deleteAllRecurrences();
    }

    @Test(expected = OperationFailedException.class)
    public void deleteAllRecurrences_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceDAO).deleteAllRecurrences();

        //method under test
        recurrenceService.deleteAllRecurrences();
    }
}
