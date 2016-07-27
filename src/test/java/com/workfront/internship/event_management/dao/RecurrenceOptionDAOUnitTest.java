package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.model.RecurrenceOption;
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
public class RecurrenceOptionDAOUnitTest {

    private DataSourceManager dataSourceManager;
    private RecurrenceOptionDAO recurrenceOptionDAO;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

        dataSourceManager = Mockito.mock(DataSourceManager.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dataSourceManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        recurrenceOptionDAO = new RecurrenceOptionDAOImpl(dataSourceManager);
    }

    @Test(expected = RuntimeException.class)
    public void addRecurrenceOption_dbError() {
        recurrenceOptionDAO.addRecurrenceOption(new RecurrenceOption());
    }


    @Test(expected = RuntimeException.class)
    public void getAllRecurrenceOptions_dbError() {
        recurrenceOptionDAO.getAllRecurrenceOptions();
    }

    @Test(expected = RuntimeException.class)
    public void getRecurrenceOption_dbError() {
        recurrenceOptionDAO.getRecurrenceOption(1);
    }

    @Test(expected = RuntimeException.class)
    public void getRecurrenceOptionsByRecurrenceType_dbError() {
        recurrenceOptionDAO.getRecurrenceOptionsByRecurrenceType(1);
    }

    @Test(expected = RuntimeException.class)
    public void updateRecurrenceOption_dbError() {
        recurrenceOptionDAO.updateRecurrenceOption(new RecurrenceOption());
    }

    @Test(expected = RuntimeException.class)
    public void deleteRecurrenceOption_dbError() throws DAOException {
        recurrenceOptionDAO.deleteRecurrenceOption(1);
    }

    @Test(expected = RuntimeException.class)
    public void deleteRecurrenceOptionsByRecurrenceType_dbError() throws DAOException {
        recurrenceOptionDAO.deleteRecurrenceOptionsByRecurrenceType(1);
    }

    @Test(expected = RuntimeException.class)
    public void deleteAllRecurrenceOptions() throws DAOException {
        recurrenceOptionDAO.deleteAllRecurrenceOptions();
    }
}
