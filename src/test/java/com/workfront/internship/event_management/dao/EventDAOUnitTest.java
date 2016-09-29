package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.service.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Event;
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

import static com.workfront.internship.event_management.TestObjectCreator.EXISTING_KEYWORD;
import static com.workfront.internship.event_management.TestObjectCreator.VALID_ID;
import static com.workfront.internship.event_management.TestObjectCreator.VALID_RESPONSE_STRING;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/18/16.
 */
public class EventDAOUnitTest {

    private static EventDAO eventDAO;
    private static RecurrenceDAO recurrenceDAO;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUpClass() throws Exception {

        DataSource dataSource = Mockito.mock(DataSource.class);
        Connection connection = Mockito.mock(Connection.class);
        recurrenceDAO = Mockito.mock(RecurrenceDAOImpl.class);


        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        eventDAO = new EventDAOImpl();
        Whitebox.setInternalState(eventDAO, "dataSource", dataSource);
        Whitebox.setInternalState(eventDAO, "recurrenceDAO", recurrenceDAO);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        eventDAO = null;
        recurrenceDAO = null;
    }

    @Test(expected = DAOException.class)
    public void addEvent_dbError() throws DAOException {
        eventDAO.addEvent(new Event());
    }

    @Test(expected = DAOException.class)
    public void addEventWithRecurrences_dbError() throws DAOException {
        eventDAO.addEventWithRecurrences(new Event());
    }

    @Test(expected = DAOException.class)
    public void getAllEvents_dbError() throws DAOException {
        eventDAO.getAllEvents();
    }

    @Test(expected = DAOException.class)
    public void getPublicEvents_dbError() throws DAOException {
        eventDAO.getAllEvents();
    }

    @Test(expected = DAOException.class)
    public void getEventById_dbError() throws DAOException, ObjectNotFoundException {
        eventDAO.getEventById(VALID_ID);
    }

    @Test(expected = DAOException.class)
    public void getEventsByCategoryId_dbError() throws DAOException {
        eventDAO.getAllEventsByCategory(VALID_ID);
    }

    @Test(expected = DAOException.class)
    public void getPublicEventsByCategoryId_dbError() throws DAOException {
        eventDAO.getPublicEventsByCategory(VALID_ID);
    }

    @Test(expected = DAOException.class)
    public void getAllPastEvents_dbError() throws DAOException {
        eventDAO.getAllPastEvents();
    }

    @Test(expected = DAOException.class)
    public void getAllUpcomingEvents_dbError() throws DAOException {
        eventDAO.getAllUpcomingEvents();
    }

    @Test(expected = DAOException.class)
    public void getPublicPastEvents_dbError() throws DAOException {
        eventDAO.getPublicPastEvents();
    }

    @Test(expected = DAOException.class)
    public void getPublicUpcomingEvents_dbError() throws DAOException {
        eventDAO.getPublicUpcomingEvents();
    }

    @Test(expected = DAOException.class)
    public void getUserAllEvents_dbError() throws DAOException {
        eventDAO.getUserAllEvents(VALID_ID);
    }

    @Test(expected = DAOException.class)
    public void getUserOrganizedEventsetUserAllEvents_dbError() throws DAOException {
        eventDAO.getUserOrganizedEvents(VALID_ID);
    }

    @Test(expected = DAOException.class)
    public void getUserEventsByResponse_dbError() throws DAOException {
        eventDAO.getUserEventsByResponse(VALID_ID, VALID_RESPONSE_STRING);
    }

    @Test(expected = DAOException.class)
    public void getUserParticipatedEvents_dbError() throws DAOException {
        eventDAO.getUserParticipatedEvents(VALID_ID);
    }

    @Test(expected = DAOException.class)
    public void getAllEventsByKeyword_dbError() throws DAOException {
        eventDAO.getAllEventsByKeyword(EXISTING_KEYWORD);
    }

    @Test(expected = DAOException.class)
    public void getPublicEventsByKeyword_dbError() throws DAOException {
        eventDAO.getPublicEventsByKeyword(EXISTING_KEYWORD);
    }

    @Test(expected = DAOException.class)
    public void updateEvent_dbError() throws DAOException, ObjectNotFoundException {
        eventDAO.updateEvent(new Event());
    }

    @Test(expected = DAOException.class)
    public void deleteEvent_dbError() throws DAOException, ObjectNotFoundException {
        eventDAO.deleteEvent(VALID_ID);
    }

    @Test(expected = DAOException.class)
    public void deleteAllEvents_dbError() throws DAOException {
        eventDAO.deleteAllEvents();
    }
}
