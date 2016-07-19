package datasource;

import com.workfront.internship.event_management.dao.DataSourceManager;
import com.workfront.internship.event_management.dao.UserDAO;
import com.workfront.internship.event_management.dao.UserDAOImpl;
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

    @Test(expected = RuntimeException.class)
    public void addUser_dbError() throws SQLException {
        userDAO.addUser(new User());
    }

    @Test(expected = RuntimeException.class)
    public void getAllUsers_dbError() throws SQLException {
        userDAO.getAllUsers();
    }

    @Test(expected = RuntimeException.class)
    public void getUserById_dbError() throws SQLException {
        userDAO.getUserById(1);
    }

    @Test(expected = RuntimeException.class)
    public void getUserByUsername_dbError() throws SQLException {
        userDAO.getUserByUsername("username");
    }

    @Test(expected = RuntimeException.class)
    public void getUserByEmail_dbError() throws SQLException {
        userDAO.getUserByEmail("email");
    }

    @Test(expected = RuntimeException.class)
    public void updateVerifiedStatus_dbError() throws SQLException {
        userDAO.updateVerifiedStatus(1);
    }

    @Test(expected = RuntimeException.class)
    public void updateUser_dbError() throws SQLException {
        userDAO.updateUser(new User());
    }


    @Test(expected = RuntimeException.class)
    public void deleteUser_dbError() throws SQLException {
        userDAO.deleteUser(1);
    }

    @Test(expected = RuntimeException.class)
    public void deleteAllUsers_dbError() throws SQLException {
        userDAO.deleteAllUsers();
    }

}
