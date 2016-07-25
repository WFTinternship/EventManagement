package datasource;

import com.workfront.internship.event_management.dao.DataSourceManager;
import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.dao.UserDAOImpl;
import com.workfront.internship.event_management.exception.DAOException;
import com.workfront.internship.event_management.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

/**
 * Created by Hermine Turshujyan 7/15/16.
 */
public class UserDAOUnitTest {

    private DataSourceManager dataSourceManager;
    private UserDAO userDAO;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

        dataSourceManager = Mockito.mock(DataSourceManager.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dataSourceManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        userDAO = new UserDAOImpl(dataSourceManager);
    }

    @Test(expected = DAOException.class)
    public void addUser_dbError() throws DAOException {
        userDAO.addUser(new User());
    }

    @Test(expected = DAOException.class)
    public void getAllUsers_dbError() throws DAOException {
        userDAO.getAllUsers();
    }

    @Test(expected = DAOException.class)
    public void getUserById_dbError() throws DAOException {
        userDAO.getUserById(1);
    }

    @Test(expected = DAOException.class)
    public void getUserByEmail_dbError() throws DAOException {
        userDAO.getUserByEmail("email");
    }

    @Test(expected = DAOException.class)
    public void updateVerifiedStatus_dbError() throws DAOException {
        userDAO.updateVerifiedStatus(1);
    }

    @Test(expected = DAOException.class)
    public void updateUser_dbError() throws DAOException {
        userDAO.updateUser(new User());
    }


    @Test(expected = DAOException.class)
    public void deleteUser_dbError() throws DAOException {
        userDAO.deleteUser(1);
    }

    @Test(expected = DAOException.class)
    public void deleteAllUsers_dbError() throws DAOException {
        userDAO.deleteAllUsers();
    }

}
