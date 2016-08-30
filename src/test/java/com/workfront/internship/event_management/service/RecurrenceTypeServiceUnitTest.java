package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.dao.RecurrenceTypeDAO;
import com.workfront.internship.event_management.dao.RecurrenceTypeDAOImpl;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.RecurrenceType;
import org.junit.*;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualRecurrenceTypes;
import static com.workfront.internship.event_management.AssertionHelper.assertEqualRecurrenceTypesWithOptions;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class RecurrenceTypeServiceUnitTest {

    private static RecurrenceTypeService recurrenceTypeService;

    private RecurrenceTypeDAO recurrenceTypeDAO;
    private RecurrenceType testRecurrenceType;

    @BeforeClass
    public static void setUpClass() {
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
        RecurrenceOptionService recurrenceOptionService = Mockito.mock(RecurrenceOptionServiceImpl.class);

        Whitebox.setInternalState(recurrenceTypeService, "recurrenceTypeDAO", recurrenceTypeDAO);
        Whitebox.setInternalState(recurrenceTypeService, "recurrenceOptionService", recurrenceOptionService);
    }

    @After
    public void tearDown() {
        testRecurrenceType = null;
        recurrenceTypeDAO = null;
    }

    //Testing addRecurrenceType method
    @Test(expected = InvalidObjectException.class)
    public void addRecurrenceType_Invalid_Type() {
        testRecurrenceType.setTitle(null);

        //method under test
        recurrenceTypeService.addRecurrenceType(testRecurrenceType);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = OperationFailedException.class)
    public void addRecurrenceType_Duplicate() {
        when(recurrenceTypeDAO.addRecurrenceType(testRecurrenceType)).thenThrow(DuplicateEntryException.class);

        //method under test
        recurrenceTypeService.addRecurrenceType(testRecurrenceType);
    }

    @Test
    public void addRecurrenceType_Success() {
        testRecurrenceType.setId(VALID_ID);
        when(recurrenceTypeDAO.addRecurrenceType(testRecurrenceType)).thenReturn(VALID_ID);

        //method under test
        RecurrenceType actualRecurrenceType = recurrenceTypeService.addRecurrenceType(testRecurrenceType);

        assertEqualRecurrenceTypes(actualRecurrenceType, testRecurrenceType);
    }

    @Test
    public void addRecurrenceTypeWithOptions_Success() {
        testRecurrenceType = createTestRecurrenceTypeWithOptions();
        testRecurrenceType.setId(VALID_ID);
        when(recurrenceTypeDAO.addRecurrenceTypeWithOptions(testRecurrenceType)).thenReturn(VALID_ID);

        //method under test
        RecurrenceType actualRecurrenceType = recurrenceTypeService.addRecurrenceType(testRecurrenceType);

        assertEqualRecurrenceTypesWithOptions(actualRecurrenceType, testRecurrenceType);
    }

    //Testing getRecurrenceTypeById method
    @Test(expected = InvalidObjectException.class)
    public void getRecurrenceTypeById_Invalid_Id() {
        //method under test
        recurrenceTypeService.getRecurrenceTypeById(INVALID_ID);
    }


    @Test(expected = ObjectNotFoundException.class)
    public void getRecurrenceTypeById_Not_Found() {
        when(recurrenceTypeDAO.getRecurrenceTypeById(NON_EXISTING_ID)).thenReturn(null);

        //method under test
        recurrenceTypeService.getRecurrenceTypeById(NON_EXISTING_ID);
    }

    @Test
    public void getRecurrenceTypeById_Success() {
        testRecurrenceType.setId(VALID_ID);
        when(recurrenceTypeDAO.getRecurrenceTypeById(VALID_ID)).thenReturn(testRecurrenceType);

        //method under test
        RecurrenceType actualRecurrenceType = recurrenceTypeService.getRecurrenceTypeById(VALID_ID);
        assertEqualRecurrenceTypes(actualRecurrenceType, testRecurrenceType);
    }

    //Testing editRecurrenceType method
    @Test(expected = InvalidObjectException.class)
    public void editRecurrenceType_Invalid_Type() {
        testRecurrenceType.setTitle(null);

        //method under test
        recurrenceTypeService.editRecurrenceType(testRecurrenceType);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void editRecurrenceType_Not_Found() {
        when(recurrenceTypeDAO.updateRecurrenceType(testRecurrenceType)).thenReturn(false);

        //method under test
        recurrenceTypeService.editRecurrenceType(testRecurrenceType);
    }

    @Test(expected = OperationFailedException.class)
    public void editRecurrenceType_Duplicate() {
        doThrow(DuplicateEntryException.class).when(recurrenceTypeDAO).updateRecurrenceType(testRecurrenceType);

        //method under test
        recurrenceTypeService.editRecurrenceType(testRecurrenceType);
    }

    @Test
    public void editRecurrenceType_Success() {
        testRecurrenceType.setTitle("Updated title");
        testRecurrenceType.setId(VALID_ID);

        when(recurrenceTypeDAO.updateRecurrenceType(testRecurrenceType)).thenReturn(true);

        //method under test
        boolean success = recurrenceTypeService.editRecurrenceType(testRecurrenceType);
        assertTrue(success);
    }

    //Testing deleteMedia method
    @Test(expected = InvalidObjectException.class)
    public void deleteRecurrenceType_Invalid_Id() {
        //method under test
        recurrenceTypeService.deleteRecurrenceType(INVALID_ID);
    }


    @Test
    public void deleteRecurrenceType_Not_Found() {
        when(recurrenceTypeDAO.deleteRecurrenceType(NON_EXISTING_ID)).thenReturn(false);

        //method under test
        boolean success = recurrenceTypeService.deleteRecurrenceType(NON_EXISTING_ID);
        assertFalse(success);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void deleteRecurrenceType_Success() {
        when(recurrenceTypeDAO.deleteRecurrenceType(VALID_ID)).thenReturn(true);

        //method under test
        boolean success = recurrenceTypeService.deleteRecurrenceType(VALID_ID);
        assertTrue(success);

        recurrenceTypeService.getRecurrenceTypeById(VALID_ID);
    }

    //Testing getAllRecurrenceTypes method
    @Test
    public void getAllRecurrenceTypes_Success() {
        //method under test
        recurrenceTypeService.getAllRecurrenceTypes();

        verify(recurrenceTypeDAO).getAllRecurrenceTypes();
    }

    //Testing deleteAllRecurrenceTypes method
    @Test
    public void deleteAllRecurrenceTypes_Success() {
        //method under test
        recurrenceTypeService.deleteAllRecurrenceTypes();

        verify(recurrenceTypeDAO).deleteAllRecurrenceTypes();
    }

}
