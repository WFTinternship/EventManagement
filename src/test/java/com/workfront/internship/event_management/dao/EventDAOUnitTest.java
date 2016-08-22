package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Event;
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
public class EventDAOUnitTest {

    private DataSource dataSource;
    private static EventDAO eventDAO;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

        dataSource = Mockito.mock(DataSource.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        eventDAO = new EventDAOImpl();
        Whitebox.setInternalState(eventDAO, "dataSource", dataSource);

    }

    @Test(expected = DAOException.class)
    public void addEvent_dbError() throws DAOException {
        eventDAO.addEvent(new Event());
    }

    @Test(expected = DAOException.class)
    public void getAllEvents_dbError() throws DAOException {
        eventDAO.getAllEvents();
    }

    @Test(expected = DAOException.class)
    public void getEventById_dbError() throws DAOException, ObjectNotFoundException {
        eventDAO.getEventById(1);
    }

    @Test(expected = DAOException.class)
    public void getEventsByCategoryId_dbError() throws DAOException {
        eventDAO.getEventsByCategory(1);

    }

    @Test(expected = DAOException.class)
    public void updateEvent_dbError() throws DAOException, ObjectNotFoundException {
        eventDAO.updateEvent(new Event());
    }

    @Test(expected = DAOException.class)
    public void deleteEvent_dbError() throws DAOException, ObjectNotFoundException {
        eventDAO.deleteEvent(1);
    }

    @Test(expected = DAOException.class)
    public void deleteAllEvents_dbError() throws DAOException {
        eventDAO.deleteAllEvents();
    }
}
