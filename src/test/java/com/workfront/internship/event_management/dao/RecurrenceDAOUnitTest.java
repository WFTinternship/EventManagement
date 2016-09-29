package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Recurrence;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/18/16.
 */
public class RecurrenceDAOUnitTest {

    private static RecurrenceDAO eventRecurrenceDAO;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUpClass() throws Exception {

        DataSource dataSource = Mockito.mock(DataSource.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        eventRecurrenceDAO = new RecurrenceDAOImpl();
        Whitebox.setInternalState(eventRecurrenceDAO, "dataSource", dataSource);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        eventRecurrenceDAO = null;
    }

    @Test(expected = DAOException.class)
    public void addEventRecurrence_dbError() throws DAOException {
        eventRecurrenceDAO.addRecurrence(new Recurrence());
    }

    @Test(expected = DAOException.class)
    public void addEventRecurrences_dbError() throws DAOException {
        List<Recurrence> recurrenceList = new ArrayList<>();
        recurrenceList.add(new Recurrence());
        eventRecurrenceDAO.addRecurrences(recurrenceList);
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
