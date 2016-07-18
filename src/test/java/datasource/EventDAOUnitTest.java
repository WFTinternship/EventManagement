package datasource;

import com.workfront.internship.event_management.DAO.DataSourceManager;
import com.workfront.internship.event_management.DAO.EventDAO;
import com.workfront.internship.event_management.DAO.EventDAOImpl;
import com.workfront.internship.event_management.model.Event;
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
public class EventDAOUnitTest {

    private DataSourceManager dataSourceManager;
    private static EventDAO eventDAO;

    // @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

        dataSourceManager = Mockito.mock(DataSourceManager.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dataSourceManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        eventDAO = new EventDAOImpl(dataSourceManager);
    }

    @Test(expected = RuntimeException.class)
    public void addEvent_dbError() {
        eventDAO.addEvent(new Event());
    }

    @Test(expected = RuntimeException.class)
    public void getAllEvents_dbError() {
        eventDAO.getAllEvents();
    }

    @Test(expected = RuntimeException.class)
    public void getEventById_dbError() {
        eventDAO.getEventById(1);
    }

    @Test(expected = RuntimeException.class)
    public void getEventsByCategoryId_dbError() {
        eventDAO.getEventsByCategory(1);

    }

    @Test(expected = RuntimeException.class)
    public void getEventsByUserId_dbError() {
        eventDAO.getEventsByUserId("organizer", 1);

    }

    @Test(expected = RuntimeException.class)
    public void getParticipatedEventsByUserId_dbError() {
        eventDAO.getParticipatedEventsByUserId(1);
    }

    @Test(expected = RuntimeException.class)
    public void getAcceptedEventsByUserId_dbError() {
        eventDAO.getAcceptedEventsByUserId(1);

    }

    @Test(expected = RuntimeException.class)
    public void updateEvent_dbError() {
        eventDAO.updateEvent(new Event());
    }

    @Test(expected = RuntimeException.class)
    public void deleteEvent_dbError() {
        eventDAO.deleteEvent(1);
    }

    @Test(expected = RuntimeException.class)
    public void deleteAllEvents_dbError() {
        eventDAO.deleteAllEvents();
    }
}
