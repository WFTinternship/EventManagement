package datasource;

import com.workfront.internship.event_management.datasource.DataSourceManager;
import com.workfront.internship.event_management.datasource.UserDAO;
import com.workfront.internship.event_management.datasource.UserDAOImpl;
import com.workfront.internship.event_management.model.User;
import org.junit.After;
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
public class UserDAOImplUnitTest {

    private DataSourceManager dataSourceManager;
    private UserDAO userDAO;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        //create test user object
        User user = TestHelper.createTestUser();

        dataSourceManager = Mockito.mock(DataSourceManager.class);

        Connection connection = Mockito.mock(Connection.class);
        when(dataSourceManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);

        userDAO = new UserDAOImpl(dataSourceManager);
    }

    @After
    public void tearDown() {

    }

    @Test(expected = RuntimeException.class)
    public void addUser_Get_Connection() throws SQLException {
        userDAO.addUser(new User());
    }


}
