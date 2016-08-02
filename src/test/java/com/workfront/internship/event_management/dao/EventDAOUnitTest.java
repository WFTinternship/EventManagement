package com.workfront.internship.event_management.dao;

/**
 * Created by Hermine Turshujyan 7/18/16.
 */
public class EventDAOUnitTest {

 /*   private DataSourceManager dataSourceManager;
    private static EventDAO eventDAO;

    @SuppressWarnings("unchecked")
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
    public void addEvent_dbError() throws DAOException {
        eventDAO.addEvent(new Event());
    }

    @Test(expected = RuntimeException.class)
    public void getAllEvents_dbError() throws DAOException {
        eventDAO.getAllEvents();
    }

    @Test(expected = RuntimeException.class)
    public void getEventById_dbError() throws DAOException {
        eventDAO.getEvent(1);
    }

    @Test(expected = RuntimeException.class)
    public void getEventsByCategoryId_dbError() throws DAOException {
        eventDAO.getEventsByCategory(1);

    }


    @Test(expected = RuntimeException.class)
    public void updateEvent_dbError() throws DAOException {
        eventDAO.updateEvent(new Event());
    }

    @Test(expected = RuntimeException.class)
    public void deleteEvent_dbError() throws DAOException {
        eventDAO.deleteEvent(1);
    }

    @Test(expected = RuntimeException.class)
    public void deleteAllEvents_dbError() throws DAOException {
        eventDAO.deleteAllEvents();
    }*/
}
