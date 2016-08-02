package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Recurrence;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/18/16.
 */
public class RecurrenceDAOUnitTest {

    private DataSourceManager dataSourceManager;
    private RecurrenceDAO eventRecurrenceDAO;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

        dataSourceManager = Mockito.mock(DataSourceManager.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dataSourceManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        eventRecurrenceDAO = new RecurrenceDAOImpl(dataSourceManager);
    }

    @Test(expected = DAOException.class)
    public void addEventRecurrence_dbError() throws DAOException {
        eventRecurrenceDAO.addRecurrence(new Recurrence());
    }

    @Test(expected = DAOException.class)
    public void getEventRecurrenceById_dbError() throws DAOException, ObjectNotFoundException {
        eventRecurrenceDAO.getRecurrenceById(1);
    }

    @Test(expected = DAOException.class)
    public void getEventRecurrencesByEventId_dbError() throws DAOException {
        eventRecurrenceDAO.getRecurrencesByEventId(1);
    }

    @Test(expected = DAOException.class)
    public void getAllEventRecurrences_dbError() throws DAOException {
        eventRecurrenceDAO.getAllRecurrences();
    }

    @Test(expected = DAOException.class)
    public void updateEventRecurrence_dbError() throws DAOException, ObjectNotFoundException {
        eventRecurrenceDAO.updateRecurrence(new Recurrence());
    }

    @Test(expected = DAOException.class)
    public void deleteEventRecurrence_dbError() throws DAOException, ObjectNotFoundException {
        eventRecurrenceDAO.deleteRecurrence(1);
    }

    @Test(expected = DAOException.class)
    public void deleteAllEventRecurrences_dbError() throws DAOException {
        eventRecurrenceDAO.deleteAllRecurrences();
    }
}
