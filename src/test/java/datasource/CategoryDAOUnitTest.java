package datasource;

import com.workfront.internship.event_management.DAO.CategoryDAO;
import com.workfront.internship.event_management.DAO.CategoryDAOImpl;
import com.workfront.internship.event_management.DAO.DataSourceManager;
import com.workfront.internship.event_management.model.Category;
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
public class CategoryDAOUnitTest {

    private DataSourceManager dataSourceManager;
    private CategoryDAO categoryDAO;

    // @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {

        dataSourceManager = Mockito.mock(DataSourceManager.class);
        Connection connection = Mockito.mock(Connection.class);

        when(dataSourceManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class), eq(PreparedStatement.RETURN_GENERATED_KEYS))).thenThrow(SQLException.class);
        when(connection.prepareStatement(any(String.class))).thenThrow(SQLException.class);

        categoryDAO = new CategoryDAOImpl(dataSourceManager);
    }

    @Test(expected = RuntimeException.class)
    public void addCategory_dbError() throws SQLException {
        categoryDAO.addCategory(new Category());
    }

    @Test(expected = RuntimeException.class)
    public void getAllCategories_dbError() throws SQLException {
        categoryDAO.getAllCategories();
    }

    @Test(expected = RuntimeException.class)
    public void getCategoryById_dbError() throws SQLException {
        categoryDAO.getCategoryById(1);
    }

    @Test(expected = RuntimeException.class)
    public void updateCategory_dbError() throws SQLException {
        categoryDAO.updateCategory(new Category());
    }

    @Test(expected = RuntimeException.class)
    public void deleteUser_dbError() throws SQLException {
        categoryDAO.deleteCategory(1);
    }

    @Test(expected = RuntimeException.class)
    public void deleteAllUsers_dbError() throws SQLException {
        categoryDAO.deleteAllCategories();
    }

}
