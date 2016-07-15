package unit;

import com.workfront.internship.event_management.datasource.UserDAO;
import com.workfront.internship.event_management.datasource.UserDAOImpl;
import com.workfront.internship.event_management.model.User;
import integration.TestHelper;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by Hermine Turshujyan 7/15/16.
 */
public class UserDAOImplUnitTest {

    Connection connection = mock(Connection.class);

    @Test(expected = RuntimeException.class)
    public void addUser_Get_Connection() throws SQLException {

        User user = TestHelper.createTestUser();

        UserDAO userDAO = new UserDAOImpl();
        Whitebox.setInternalState(userDAO, "conn", connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);

        userDAO.addUser(user);
    }

}
