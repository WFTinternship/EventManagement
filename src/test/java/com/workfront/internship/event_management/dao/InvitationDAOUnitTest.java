package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Invitation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/18/16.
 */
public class InvitationDAOUnitTest {

    private DataSource dataSource;
    private InvitationDAO invitationDAO;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

        dataSource = Mockito.mock(DataSource.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        invitationDAO = new InvitationDAOImpl();
        Whitebox.setInternalState(invitationDAO, "dataSource", dataSource);

    }


    @Test(expected = DAOException.class)
    public void addInvitation_dbError() throws DAOException, DuplicateEntryException {
        invitationDAO.addInvitation(new Invitation());
    }

    @Test(expected = DAOException.class)
    public void getInvitationById_dbError() throws DAOException, ObjectNotFoundException {
        invitationDAO.getInvitationById(1);
    }

    @Test(expected = DAOException.class)
    public void getAllInvitations_dbError() throws DAOException {
        invitationDAO.getAllInvitations();
    }

    @Test(expected = DAOException.class)
    public void getInvitationsByEventId_dbError() throws DAOException {
        invitationDAO.getInvitationsByEventId(1);
    }

    @Test(expected = DAOException.class)
    public void getInvitationsByUserId_dbError() throws DAOException {
        invitationDAO.getInvitationsByUserId(1);
    }


    @Test(expected = DAOException.class)
    public void updateInvitation_dbError() throws DuplicateEntryException, ObjectNotFoundException, DAOException {
        invitationDAO.updateInvitation(new Invitation());
    }

    @Test(expected = DAOException.class)
    public void deleteInvitation_dbError() throws DAOException, ObjectNotFoundException {
        invitationDAO.deleteInvitation(1);
    }

    @Test(expected = DAOException.class)
    public void deleteInvitationsByEventId_dbError() throws DAOException {
        invitationDAO.deleteInvitationsByEventId(1);
    }

    @Test(expected = DAOException.class)
    public void deleteInvitationsByUserId_dbError() throws DAOException {
        invitationDAO.deleteInvitationsByUserId(1);
    }

    @Test(expected = DAOException.class)
    public void deleteAllInvitations_dbError() throws DAOException {
        invitationDAO.deleteAllInvitations();
    }
}
