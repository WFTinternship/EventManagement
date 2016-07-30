package com.workfront.internship.event_management.dao;

/**
 * Created by Hermine Turshujyan 7/18/16.
 */
public class InvitationDAOUnitTest {

    //todo add test for checking conn.close()
    //todo add method call validation
/*
    private DataSourceManager dataSourceManager;
    private InvitationDAO invitationDAO;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

        dataSourceManager = Mockito.mock(DataSourceManager.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dataSourceManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        invitationDAO = new InvitationDAOImpl(dataSourceManager);
    }


    @Test(expected = RuntimeException.class)
    public void addInvitation_dbError() throws DAOException {
        invitationDAO.addInvitation(new Invitation());
    }

    @Test(expected = RuntimeException.class)
    public void getInvitationById_dbError() {
        invitationDAO.getInvitation(1);
    }

    @Test(expected = RuntimeException.class)
    public void getAllInvitations_dbError() {
        invitationDAO.getAllInvitations();
    }

    @Test(expected = RuntimeException.class)
    public void getInvitationsByEventId_dbError() {
        invitationDAO.getInvitationsByEventId(1);
    }

    @Test(expected = RuntimeException.class)
    public void getInvitationsByUserId_dbError() {
        invitationDAO.getInvitationsByUserId(1);
    }


    @Test(expected = RuntimeException.class)
    public void updateInvitation_dbError() {
        invitationDAO.updateInvitation(new Invitation());
    }

    @Test(expected = RuntimeException.class)
    public void deleteInvitation_dbError() throws DAOException {
        invitationDAO.deleteInvitation(1);
    }

    @Test(expected = RuntimeException.class)
    public void deleteInvitationsByEventId_dbError() throws DAOException {
        invitationDAO.deleteInvitationsByEventId(1);
    }

    @Test(expected = RuntimeException.class)
    public void deleteInvitationsByUserId_dbError() throws DAOException {
        invitationDAO.deleteInvitationsByUserId(1);
    }

    @Test(expected = RuntimeException.class)
    public void deleteAllInvitations_dbError() throws DAOException {
        invitationDAO.deleteAllInvitations();
    }*/
}
