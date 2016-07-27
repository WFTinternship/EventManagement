package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.DAOException;
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

    @Test(expected = RuntimeException.class)
    public void addRecurrenceType_dbError() {
        recurrenceTypeDAO.addRecurrenceType(new RecurrenceType());
    }

    @Test(expected = RuntimeException.class)
    public void getAllRecurrenceTypes_dbError() {
        recurrenceTypeDAO.getAllRecurrenceTypes();
    }

    @Test(expected = RuntimeException.class)
    public void getRecurrenceTypeById_dbError() {
        recurrenceTypeDAO.getRecurrenceTypeById(1);
    }

    @Test(expected = RuntimeException.class)
    public void updateRecurrenceType() {
        recurrenceTypeDAO.updateRecurrenceType(new RecurrenceType());
    }

    @Test(expected = RuntimeException.class)
    public void deleteRecurrenceType() throws DAOException {
        recurrenceTypeDAO.deleteRecurrenceType(1);
    }

    @Test(expected = RuntimeException.class)
    public void deleteAllRecurrenceTypes() throws DAOException {
        recurrenceTypeDAO.deleteAllRecurrenceTypes();

    }
}
