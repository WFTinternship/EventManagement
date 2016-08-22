package com.workfront.internship.event_management.dao;

import com.workfront.internship.event_management.exception.dao.DAOException;
import com.workfront.internship.event_management.exception.dao.DuplicateEntryException;
import com.workfront.internship.event_management.exception.dao.ObjectNotFoundException;
import com.workfront.internship.event_management.model.Category;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

/**
 * Created by Hermine Turshujyan 7/18/16.
 */
public class CategoryDAOUnitTest {

    private static DataSource dataSource;
    private static Connection connection;
    private static CategoryDAO categoryDAO;

    @SuppressWarnings("unchecked")
    @BeforeClass
    public static void setUpClass() throws Exception {

        dataSource = Mockito.mock(DataSource.class);
        connection = Mockito.mock(Connection.class);

        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        categoryDAO = new CategoryDAOImpl();
        Whitebox.setInternalState(categoryDAO, "dataSource", dataSource);

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        categoryDAO = null;
        dataSource = null;
        connection = null;
    }

    @Test(expected = DAOException.class)
    public void addCategory_dbError() throws DAOException, DuplicateEntryException {
        categoryDAO.addCategory(new Category());
    }

    @Test(expected = DAOException.class)
    public void getAllCategories_dbError() throws DAOException {
        categoryDAO.getAllCategories();
    }

    @Test(expected = DAOException.class)
    public void getCategoryById_dbError() throws DAOException, ObjectNotFoundException {
        categoryDAO.getCategoryById(1);
    }

    @Test(expected = DAOException.class)
    public void updateCategory_dbError() throws DAOException, DuplicateEntryException, ObjectNotFoundException {
        categoryDAO.updateCategory(new Category());
    }

    @Test(expected = DAOException.class)
    public void deleteUser_dbError() throws DAOException, ObjectNotFoundException {
        categoryDAO.deleteCategory(1);
    }

    @Test(expected = DAOException.class)
    public void deleteAllUsers_dbError() throws DAOException {
        categoryDAO.deleteAllCategories();
    }
}
