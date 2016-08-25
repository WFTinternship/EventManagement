package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.ObjectNotFoundException;
import com.workfront.internship.event_management.model.User;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import javax.sql.DataSource;
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

    private static DataSource dataSource;
    private static Connection connection;
    private static UserDAO userDAO;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUpClass() throws Exception {

        dataSource = Mockito.mock(DataSource.class);
        connection = Mockito.mock(Connection.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        userDAO = new UserDAOImpl();
        Whitebox.setInternalState(userDAO, "dataSource", dataSource);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        userDAO = null;
        dataSource = null;
        connection = null;
    }

    @Test(expected = DAOException.class)
    public void addUser_dbError() throws DAOException, DuplicateEntryException {
        userDAO.addUser(new User());
    }

    @Test(expected = DAOException.class)
    public void getAllUsers_dbError() throws DAOException {
        userDAO.getAllUsers();
    }

    @Test(expected = DAOException.class)
    public void getUserById_dbError() throws DAOException, ObjectNotFoundException {
        userDAO.getUserById(1);
    }

    @Test(expected = DAOException.class)
    public void getUserByEmail_dbError() throws DAOException, ObjectNotFoundException {
        userDAO.getUserByEmail("email");
    }

    @Test(expected = DAOException.class)
    public void updateVerifiedStatus_dbError() throws DAOException, ObjectNotFoundException {
        userDAO.updateVerifiedStatus(1);
    }

    @Test(expected = DAOException.class)
    public void updateUser_dbError() throws DAOException, DuplicateEntryException, ObjectNotFoundException {
        userDAO.updateUser(new User());
    }

    @Test(expected = DAOException.class)
    public void deleteUser_dbError() throws DAOException, ObjectNotFoundException {
        userDAO.deleteUser(1);
    }

    @Test(expected = DAOException.class)
    public void deleteAllUsers_dbError() throws DAOException {
        userDAO.deleteAllUsers();
    }

}
