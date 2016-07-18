package datasource;

import com.workfront.internship.event_management.DAO.DataSourceManager;
import com.workfront.internship.event_management.DAO.EventRecurrenceDAO;
import com.workfront.internship.event_management.DAO.EventRecurrenceDAOImpl;
import com.workfront.internship.event_management.model.EventRecurrence;
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
public class EventRecurrenceDAOUnitTest {

    private DataSourceManager dataSourceManager;
    private EventRecurrenceDAO eventRecurrenceDAO;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

        dataSourceManager = Mockito.mock(DataSourceManager.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dataSourceManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);

        eventRecurrenceDAO = new EventRecurrenceDAOImpl(dataSourceManager);
    }

    @Test(expected = RuntimeException.class)
    public void addEventRecurrence_dbError() {
        eventRecurrenceDAO.addEventRecurrence(new EventRecurrence());
    }

    @Test(expected = RuntimeException.class)
    public void getEventRecurrenceById_dbError() {
        eventRecurrenceDAO.getEventRecurrenceById(1);
    }

    @Test(expected = RuntimeException.class)
    public void getEventRecurrencesByEventId_dbError() {
        eventRecurrenceDAO.getEventRecurrencesByEventId(1);
    }

    @Test(expected = RuntimeException.class)
    public void getAllEventRecurrences_dbError() {
        eventRecurrenceDAO.getAllEventRecurrences();
    }

    @Test(expected = RuntimeException.class)
    public void updateEventRecurrence_dbError() {
        eventRecurrenceDAO.updateEventRecurrence(new EventRecurrence());
    }

    @Test(expected = RuntimeException.class)
    public void deleteEventRecurrence_dbError() {
        eventRecurrenceDAO.deleteEventRecurrence(1);
    }

    @Test(expected = RuntimeException.class)
    public void deleteAllEventRecurrences_dbError() {
        eventRecurrenceDAO.deleteAllEventRecurrences();
    }
}
