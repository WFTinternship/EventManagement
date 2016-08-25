package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.RecurrenceTypeDAO;
import com.workfront.internship.event_management.dao.RecurrenceTypeDAOImpl;
import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Media;
import com.workfront.internship.event_management.model.RecurrenceType;
import org.junit.*;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualRecurrenceTypes;
import static com.workfront.internship.event_management.AssertionHelper.assertEqualRecurrenceTypesWithOptions;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class RecurrenceTypeServiceUnitTest {
    private static RecurrenceTypeService recurrenceTypeService;
    private RecurrenceTypeDAO recurrenceTypeDAO;
    private RecurrenceType testRecurrenceType;
    private List<Media> testMediaList;


    @BeforeClass
    public static void setUpClass() throws OperationFailedException {
        recurrenceTypeService = new RecurrenceTypeServiceImpl();
    }

    @AfterClass
    public static void tearDownClass() {
        recurrenceTypeService = null;
    }

    @Before
    public void setUp() {
        //create test media object
        testRecurrenceType = createTestRecurrenceType();

        recurrenceTypeDAO = Mockito.mock(RecurrenceTypeDAOImpl.class);
        Whitebox.setInternalState(recurrenceTypeService, "recurrenceTypeDAO", recurrenceTypeDAO);
    }

    @After
    public void tearDown() {
        testRecurrenceType = null;
        recurrenceTypeDAO = null;
    }

    //Testing addRecurrenceType method
    @Test(expected = OperationFailedException.class)
    public void addRecurrenceType_Invalid_Type() {
        testRecurrenceType.setTitle(null);

        //method under test
        recurrenceTypeService.addRecurrenceType(testRecurrenceType);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = OperationFailedException.class)
    public void addRecurrenceType_Duplicate() throws DuplicateEntryException, DAOException {
        when(recurrenceTypeDAO.addRecurrenceType(testRecurrenceType)).thenThrow(DuplicateEntryException.class);

        //method under test
        recurrenceTypeService.addRecurrenceType(testRecurrenceType);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = OperationFailedException.class)
    public void addRecurrenceType_DB_Error() throws DuplicateEntryException, DAOException {
        when(recurrenceTypeDAO.addRecurrenceType(testRecurrenceType)).thenThrow(DAOException.class);

        //method under test
        recurrenceTypeService.addRecurrenceType(testRecurrenceType);
    }

    @Test
    public void addRecurrenceType_Success() throws DuplicateEntryException, DAOException {
        testRecurrenceType.setId(VALID_ID);
        when(recurrenceTypeDAO.addRecurrenceType(testRecurrenceType)).thenReturn(VALID_ID);

        //method under test
        RecurrenceType actualRecurrenceType = recurrenceTypeService.addRecurrenceType(testRecurrenceType);

        assertEqualRecurrenceTypes(actualRecurrenceType, testRecurrenceType);
    }

    @Test
    public void addRecurrenceTypeWithOptions_Success() throws DuplicateEntryException, DAOException {
        testRecurrenceType = createTestRecurrenceTypeWithOptions();
        testRecurrenceType.setId(VALID_ID);
        when(recurrenceTypeDAO.addRecurrenceTypeWithOptions(testRecurrenceType)).thenReturn(VALID_ID);

        //method under test
        RecurrenceType actualRecurrenceType = recurrenceTypeService.addRecurrenceType(testRecurrenceType);

        assertEqualRecurrenceTypesWithOptions(actualRecurrenceType, testRecurrenceType);
    }

    //Testing getRecurrenceTypeById method
    @Test(expected = OperationFailedException.class)
    public void getRecurrenceTypeById_Invalid_Id() {
        //method under test
        recurrenceTypeService.getRecurrenceTypeById(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getRecurrenceTypeById_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceTypeDAO).getRecurrenceTypeById(VALID_ID);

        //method under test
        recurrenceTypeService.getRecurrenceTypeById(VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void getRecurrenceTypeById_Not_Found() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(recurrenceTypeDAO).getRecurrenceTypeById(NON_EXISTING_ID);

        //method under test
        recurrenceTypeService.getRecurrenceTypeById(NON_EXISTING_ID);
    }

    @Test
    public void getRecurrenceTypeById_Success() throws ObjectNotFoundException, DAOException {
        testRecurrenceType.setId(VALID_ID);
        when(recurrenceTypeDAO.getRecurrenceTypeById(VALID_ID)).thenReturn(testRecurrenceType);

        //method under test
        RecurrenceType actualRecurrenceType = recurrenceTypeService.getRecurrenceTypeById(VALID_ID);
        assertEqualRecurrenceTypes(actualRecurrenceType, testRecurrenceType);
    }

    //Testing editRecurrenceType method
    @Test(expected = OperationFailedException.class)
    public void editRecurrenceType_Invalid_Type() {
        testRecurrenceType.setTitle(null);

        //method under test
        recurrenceTypeService.editRecurrenceType(testRecurrenceType);
    }

    @Test(expected = OperationFailedException.class)
    public void editRecurrenceType_Not_Found() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(ObjectNotFoundException.class).when(recurrenceTypeDAO).updateRecurrenceType(testRecurrenceType);

        //method under test
        recurrenceTypeService.editRecurrenceType(testRecurrenceType);
    }

    @Test(expected = OperationFailedException.class)
    public void editRecurrenceType_DB_Error() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(DAOException.class).when(recurrenceTypeDAO).updateRecurrenceType(testRecurrenceType);

        //method under test
        recurrenceTypeService.editRecurrenceType(testRecurrenceType);
    }

    @Test(expected = OperationFailedException.class)
    public void editRecurrenceType_Duplicate() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        doThrow(DuplicateEntryException.class).when(recurrenceTypeDAO).updateRecurrenceType(testRecurrenceType);

        //method under test
        recurrenceTypeService.editRecurrenceType(testRecurrenceType);
    }

    @Test
    public void editRecurrenceType_Success() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        testRecurrenceType.setTitle("Updated title");
        testRecurrenceType.setId(VALID_ID);

        //method under test
        recurrenceTypeService.editRecurrenceType(testRecurrenceType);

        verify(recurrenceTypeDAO).updateRecurrenceType(testRecurrenceType);
    }

    //Testing deleteMedia method
    @Test(expected = OperationFailedException.class)
    public void deleteRecurrenceType_Invalid_Id() {
        //method under test
        recurrenceTypeService.deleteRecurrenceType(INVALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteRecurrenceType_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceTypeDAO).deleteRecurrenceType(VALID_ID);

        //method under test
        recurrenceTypeService.deleteRecurrenceType(VALID_ID);
    }

    @Test(expected = OperationFailedException.class)
    public void deleteRecurrenceType_Not_Found() throws ObjectNotFoundException, DAOException {
        doThrow(ObjectNotFoundException.class).when(recurrenceTypeDAO).deleteRecurrenceType(NON_EXISTING_ID);

        //method under test
        recurrenceTypeService.deleteRecurrenceType(NON_EXISTING_ID);
    }

    @Test
    public void deleteRecurrenceType_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        recurrenceTypeService.deleteRecurrenceType(VALID_ID);

        verify(recurrenceTypeDAO).deleteRecurrenceType(VALID_ID);
    }

    //Testing getAllRecurrenceTypes method
    @Test
    public void getAllRecurrenceTypes_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        recurrenceTypeService.getAllRecurrenceTypes();

        verify(recurrenceTypeDAO).getAllRecurrenceTypes();
    }

    @Test(expected = OperationFailedException.class)
    public void getAllRecurrenceTypes_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceTypeDAO).getAllRecurrenceTypes();

        //method under test
        recurrenceTypeService.getAllRecurrenceTypes();
    }

    //Testing deleteAllRecurrenceTypes method
    @Test
    public void deleteAllRecurrenceTypes_Success() throws ObjectNotFoundException, DAOException {
        //method under test
        recurrenceTypeService.deleteAllRecurrenceTypes();

        verify(recurrenceTypeDAO).deleteAllRecurrenceTypes();
    }

    @Test(expected = OperationFailedException.class)
    public void deleteAllRecurrenceTypes_DB_Error() throws ObjectNotFoundException, DAOException {
        doThrow(DAOException.class).when(recurrenceTypeDAO).deleteAllRecurrenceTypes();

        //method under test
        recurrenceTypeService.deleteAllRecurrenceTypes();
    }
}
