package com.workfront.internship.event_management.service;

import com.workfront.internship.event_management.TestObjectCreator;
import com.workfront.internship.event_management.dao.InvitationDAO;
import com.workfront.internship.event_management.dao.InvitationDAOImpl;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.service.InvalidObjectException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.exception.service.OperationFailedException;
import com.workfront.internship.event_management.model.Invitation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.ArrayList;
import java.util.List;

import static com.workfront.internship.event_management.AssertionHelper.assertEqualInvitations;
import static com.workfront.internship.event_management.TestObjectCreator.*;
import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/29/16.
 */
public class InvitationServiceUnitTest {

    private InvitationService invitationService;
    private InvitationDAO invitationDAO;
    private Invitation testInvitation;
    private List<Invitation> testInvitationList;

    @Before
    public void setUp() {
        testInvitation = TestObjectCreator.createTestInvitation();
        testInvitationList = new ArrayList<>();
        testInvitationList.add(testInvitation);

        invitationService = spy(new InvitationServiceImpl());
        invitationDAO = Mockito.mock(InvitationDAOImpl.class);
        Whitebox.setInternalState(invitationService, "invitationDAO", invitationDAO);
    }

    @After
    public void tearDown() {
        testInvitation = null;
        testInvitationList = null;
        invitationDAO = null;
        invitationService = null;
    }

    @Test(expected = InvalidObjectException.class)
    public void addInvitation_Invalid_Invitation() {
        testInvitation.setEventId(INVALID_ID);

        //method under test
        invitationService.addInvitation(testInvitation);
    }

    @Test
    public void addInvitation_Success() {

        when(invitationDAO.addInvitation(testInvitation)).thenReturn(VALID_ID);

        //method under test
        Invitation actualInvitation = invitationService.addInvitation(testInvitation);

        assertEqualInvitations(actualInvitation, testInvitation);
    }

    @Test(expected = OperationFailedException.class)
    public void addInvitation_Duplicate() {

        doThrow(DuplicateEntryException.class).when(invitationDAO).addInvitation(testInvitation);

        //method under test
        invitationService.addInvitation(testInvitation);
    }


    //Testing addInvitations method
    @Test(expected = InvalidObjectException.class)
    public void addInvitations_Empty_List() {
        //method under test
        invitationService.addInvitations(new ArrayList<Invitation>());
    }

    @Test
    public void addInvitations_Success() {
        //method under test
        invitationService.addInvitations(testInvitationList);

        verify(invitationDAO).addInvitations(testInvitationList);
    }

    @Test(expected = OperationFailedException.class)
    public void addInvitations_Duplicate() {

        doThrow(DuplicateEntryException.class).when(invitationDAO).addInvitations(testInvitationList);

        //method under test
        invitationService.addInvitations(testInvitationList);
    }

    //Testing getInvitationById method
    @Test(expected = InvalidObjectException.class)
    public void getInvitation_Invalid_Id() {
        //method under test
        invitationService.getInvitationById(INVALID_ID);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void getInvitation_Not_Found() {
        when(invitationDAO.getInvitationById(NON_EXISTING_ID)).thenReturn(null);

        //method under test
        invitationService.getInvitationById(NON_EXISTING_ID);
    }

    @Test
    public void getInvitation_Success() {
        testInvitation.setId(VALID_ID);
        when(invitationDAO.getInvitationById(VALID_ID)).thenReturn(testInvitation);

        //method under test
        Invitation actualInvitation = invitationService.getInvitationById(VALID_ID);
        assertEqualInvitations(actualInvitation, testInvitation);
    }

    //Testing getInvitationsByEventId method
    @Test(expected = InvalidObjectException.class)
    public void getInvitationsByEvent_Invalid_Id() {
        //method under test
        invitationService.getInvitationsByEvent(INVALID_ID);
    }

    @Test
    public void getInvitationsByEvent_Success() {
        when(invitationDAO.getInvitationsByEventId(VALID_ID)).thenReturn(testInvitationList);

        //method under test
        List<Invitation> actualInvitationList = invitationService.getInvitationsByEvent(VALID_ID);
        assertNotNull(actualInvitationList);
        assertFalse(actualInvitationList.isEmpty());
        assertEquals(actualInvitationList.size(), 1);
        assertEqualInvitations(actualInvitationList.get(0), testInvitation);
    }

    //Testing getInvitationsByUserId method
    @Test(expected = InvalidObjectException.class)
    public void getInvitationsByUser_Invalid_Id() {
        //method under test
        invitationService.getInvitationsByUser(INVALID_ID);
    }

    @Test
    public void getInvitationsByUser_Success() {
        when(invitationDAO.getInvitationsByUserId(VALID_ID)).thenReturn(testInvitationList);

        //method under test
        List<Invitation> actualInvitationList = invitationService.getInvitationsByUser(VALID_ID);
        assertNotNull(actualInvitationList);
        assertFalse(actualInvitationList.isEmpty());
        assertEquals(actualInvitationList.size(), 1);
        assertEqualInvitations(actualInvitationList.get(0), testInvitation);
    }

    //Testing editInvitation method
    @Test(expected = InvalidObjectException.class)
    public void editInvitation_Invalid_Invitation() {
        testInvitation.setUser(null);

        //method under test
        invitationService.editInvitation(testInvitation);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void editInvitation_Not_Found() {
        when(invitationDAO.updateInvitation(testInvitation)).thenReturn(false);

        //method under test
        invitationService.editInvitation(testInvitation);
    }

    @Test(expected = OperationFailedException.class)
    public void editInvitation_Duplicate() {

        doThrow(DuplicateEntryException.class).when(invitationDAO).updateInvitation(testInvitation);

        //method under test
        invitationService.editInvitation(testInvitation);
    }

    @Test
    public void editInvitation_Success() {
        //method under test
        when(invitationDAO.updateInvitation(testInvitation)).thenReturn(true);

        boolean success = invitationService.editInvitation(testInvitation);

        assertTrue(success);
    }

    //testing editInvitationList method
    @Test
    public void editInvitations_Empty_List() {
        //method under test
        invitationService.editInvitationList(VALID_ID, new ArrayList<Invitation>());

        verify(invitationService).deleteInvitationsByEvent(VALID_ID);
    }

    @Test
    public void editInvitations_InsertList_EmptyDBList() {
        when(invitationService.getInvitationsByEvent(VALID_ID)).thenReturn(null);

        //method under test
        invitationService.editInvitationList(VALID_ID, testInvitationList);

        verify(invitationService).addInvitations(testInvitationList);
    }

    @Test
    public void editInvitations_Insert_NonEmptyDBList() {
        //db list does not contains testInvitation object, test method should add it to db
        List<Invitation> dbList = new ArrayList<>();
        Invitation dbInvitation = createTestInvitation().setId(100);
        dbList.add(dbInvitation);

        testInvitation.setId(VALID_ID);

        when(invitationService.getInvitationsByEvent(VALID_ID)).thenReturn(dbList);

        //method under test
        invitationService.editInvitationList(VALID_ID, testInvitationList);

        verify(invitationService).addInvitation(testInvitationList.get(0));
    }

    @Test
    public void editInvitationList_Update_NonEmptyDBList() {
        testInvitation.setId(VALID_ID);

        Invitation dbInvitation = new Invitation(testInvitation);
        dbInvitation.setAttendeesCount(100);

        List<Invitation> dbList = new ArrayList<>();
        dbList.add(dbInvitation);

        when(invitationService.getInvitationsByEvent(TestObjectCreator.VALID_ID)).thenReturn(dbList);
        doReturn(true).when(invitationService).editInvitation((Invitation) anyObject());

        //method under test
        invitationService.editInvitationList(VALID_ID, testInvitationList);

        verify(invitationService).editInvitation(testInvitation);
    }

    @Test
    public void editInvitationList_DeleteInvitation_FromDB() {
        //create db list, that contains invitation with the same id as testInvitation
        List<Invitation> dbList = new ArrayList<>();
        Invitation dbInvitation = createTestInvitation().setId(VALID_ID);
        dbList.add(dbInvitation);

        when(invitationService.getInvitationsByEvent(TestObjectCreator.VALID_ID)).thenReturn(dbList);

        //method under test
        invitationService.editInvitationList(VALID_ID, testInvitationList);

        verify(invitationService).deleteInvitation(dbInvitation.getId());
    }

    //Testing deleteInvitationsByEvent method
    @Test(expected = InvalidObjectException.class)
    public void deleteInvitationsByEvent_Invalid_Id() {
        //method under test
        invitationService.deleteInvitationsByEvent(INVALID_ID);
    }

    @Test
    public void deleteInvitationsByEvent_Success() {
        //method under test
        invitationService.deleteInvitationsByEvent(VALID_ID);

        verify(invitationDAO).deleteInvitationsByEventId(VALID_ID);
    }

    //Testing deleteInvitationsByEvent method
    @Test(expected = InvalidObjectException.class)
    public void deleteInvitationsByUser_Invalid_Id() {
        //method under test
        invitationService.deleteInvitationsByUser(INVALID_ID);
    }

    @Test
    public void deleteInvitationsByUserId_Success() {
        //method under test
        invitationService.deleteInvitationsByUser(VALID_ID);

        verify(invitationDAO).deleteInvitationsByUserId(VALID_ID);
    }


    //Testing deleteInvitation method
    @Test(expected = InvalidObjectException.class)
    public void deleteInvitation_Invalid_Id() {
        //method under test
        invitationService.deleteInvitation(INVALID_ID);
    }

    @Test
    public void deleteInvitation_Not_Found() {
        when(invitationDAO.deleteInvitation(NON_EXISTING_ID)).thenReturn(false);

        //method under test
        boolean success = invitationService.deleteInvitation(NON_EXISTING_ID);
        assertFalse(success);
    }

    @Test
    public void deleteInvitation_Success() {
        //method under test
        invitationService.deleteInvitation(VALID_ID);

        verify(invitationDAO).deleteInvitation(VALID_ID);
    }

    //Testing getAllInvitations method
    @Test
    public void getAllInvitations_Success() {
        //method under test
        invitationService.getAllInvitations();

        verify(invitationDAO).getAllInvitations();
    }

    //Testing deleteAllInvitations method
    @Test
    public void deleteAllInvitations_Success() {
        //method under test
        invitationService.deleteAllInvitations();

        verify(invitationDAO).deleteAllInvitations();
    }
}
