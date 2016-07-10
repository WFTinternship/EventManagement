import com.workfront.internship.event_management.datasource.*;
import com.workfront.internship.event_management.model.EventCategory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by hermine on 7/9/16.
 */
public class TestEventCategoryDAOImpl {

    private EventCategoryDAO categoryDAO =  new EventCategoryDAOImpl();
    private EventCategory testCategory;
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;

    @Before
    public void setUp() {
        testCategory = TestUtil.setUpTestCategory();
        try {
            conn = DataSourceManager.getInstance().getConnection();
            insertTestCategory();
            testCategory.setId(getTestCategory().getId());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        try {
            deleteTestCategory();
            testCategory = null;
            categoryDAO = null;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs, stmt, conn);
        }
    }

    @Test
    public void testInsertCategory() throws SQLException {
        deleteTestCategory();
        categoryDAO.insertCategory(testCategory);
        EventCategory actualCategory = getTestCategory();
        assertEquals(actualCategory.getTitle(), testCategory.getTitle());
        assertEquals(actualCategory.getDescription(), testCategory.getDescription());
        assertNotNull(actualCategory.getCreationDate());
    }

    @Test
    public void testGetAllCategories() throws SQLException {
        List<EventCategory> expectedCategories = getAllCategories();
        List<EventCategory> actualCategories = categoryDAO.getAllCategories();
        assertEquals(actualCategories.size(), expectedCategories.size());
        for (int i = 0; i < actualCategories.size(); i++) {
            assertEquals(actualCategories.get(i).getId(), expectedCategories.get(i).getId());
            assertEquals(actualCategories.get(i).getTitle(), expectedCategories.get(i).getTitle());
            assertEquals(actualCategories.get(i).getDescription(), expectedCategories.get(i).getDescription());
            assertEquals(actualCategories.get(i).getCreationDate(), expectedCategories.get(i).getCreationDate());
        }
    }

    @Test
    public void testGetCategoryById() throws SQLException {
        EventCategory actualCategory = categoryDAO.getCategoryById(testCategory.getId());
        assertEquals(actualCategory.getId(), testCategory.getId());
        assertEquals(actualCategory.getTitle(), testCategory.getTitle());
        assertEquals(actualCategory.getDescription(), testCategory.getDescription());
        assertNotNull(actualCategory.getCreationDate());
    }

    @Test
    public void testUpdateCategory() throws SQLException {
        EventCategory newCategory = new EventCategory(testCategory);
        newCategory.setDescription("New test description");
        categoryDAO.updateCategory(newCategory);
        EventCategory actualCategory = getTestCategory();
        assertEquals(actualCategory.getId(), testCategory.getId());
        assertEquals(actualCategory.getTitle(), newCategory.getTitle());
        assertEquals(actualCategory.getDescription(), newCategory.getDescription());
        assertNotNull(actualCategory.getCreationDate());
    }

    @Test
    public void testDeleteCategory() throws SQLException {
        categoryDAO.deleteCategory(testCategory.getId());
        assertNull(getTestCategory());
    }

    //helper methods
    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException " + e.getMessage());
        }
    }



    private void insertTestCategory() throws SQLException {
        String sqlStr = "INSERT INTO event_category "
                + "(title, description) "
                + "VALUES (?, ?)";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setString(1, testCategory.getTitle());
        stmt.setString(2, testCategory.getDescription());
        stmt.executeUpdate();
    }

    private void deleteTestCategory() throws SQLException {
        String sqlStr = "DELETE FROM event_category WHERE title = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sqlStr);
        preparedStatement.setString(1, testCategory.getTitle());
        preparedStatement.executeUpdate();
    }

    private EventCategory getTestCategory() throws SQLException {
        String sqlStr = "SELECT * FROM event_category WHERE title  = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setString(1, testCategory.getTitle());
        rs = stmt.executeQuery();
        EventCategory category = null;
        while (rs.next()) {
            category = new EventCategory();
            category.setId(rs.getInt("id"))
                    .setTitle(rs.getString("title"))
                    .setDescription(rs.getString("description"))
                    .setCreationDate(rs.getTimestamp("creation_date"));
        }
        return category;
    }

    private List<EventCategory> getAllCategories() throws SQLException {
        List<EventCategory> categoriesList = new ArrayList<EventCategory>();
        String sqlStr = "SELECT * FROM event_category";
        stmt = conn.prepareStatement(sqlStr);
        rs = stmt.executeQuery();
        while (rs.next()) {
            EventCategory category = new EventCategory();
            category.setId(rs.getInt("id"))
                    .setTitle(rs.getString("title"))
                    .setDescription(rs.getString("description"))
                    .setCreationDate(rs.getTimestamp("creation_date"));
            categoriesList.add(category);
        }
        return categoriesList;
    }
}
