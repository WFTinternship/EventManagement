package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.RecurrenceType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by Hermine Turshujyan 7/18/16.
 */
public class RecurrenceTypeDAOUnitTest {

    private DataSourceManager dataSourceManager;
    private RecurrenceTypeDAO recurrenceTypeDAO;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

        dataSourceManager = Mockito.mock(DataSourceManager.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dataSourceManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        recurrenceTypeDAO = new RecurrenceTypeDAOImpl(dataSourceManager);
    }

    @Test(expected = DAOException.class)
    public void addRecurrenceType_dbError() throws DuplicateEntryException, DAOException {
        recurrenceTypeDAO.addRecurrenceType(new RecurrenceType());
    }

    @Test(expected = DAOException.class)
    public void addRecurrenceTypeWithOptions_dbError() throws DuplicateEntryException, DAOException {
        recurrenceTypeDAO.addRecurrenceTypeWithOptions(new RecurrenceType());
    }

    @Test(expected = DAOException.class)
    public void getAllRecurrenceTypes_dbError() throws DAOException {
        recurrenceTypeDAO.getAllRecurrenceTypes();
    }

    @Test(expected = DAOException.class)
    public void getRecurrenceTypeById_dbError() throws ObjectNotFoundException, DAOException {
        recurrenceTypeDAO.getRecurrenceTypeById(1);
    }

    @Test(expected = DAOException.class)
    public void editRecurrenceType() throws DAOException, ObjectNotFoundException, DuplicateEntryException {
        recurrenceTypeDAO.updateRecurrenceType(new RecurrenceType());
    }

    @Test(expected = DAOException.class)
    public void deleteRecurrenceType() throws DAOException, ObjectNotFoundException {
        recurrenceTypeDAO.deleteRecurrenceType(1);
    }

    @Test(expected = DAOException.class)
    public void deleteAllRecurrenceTypes() throws DAOException {
        recurrenceTypeDAO.deleteAllRecurrenceTypes();

    }
}
