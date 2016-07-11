import com.workfront.internship.event_management.datasource.DataSourceManager;
import com.workfront.internship.event_management.datasource.EventCategoryDAO;
import com.workfront.internship.event_management.datasource.EventCategoryDAOImpl;
import com.workfront.internship.event_management.model.EventCategory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
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

    private static EventCategoryDAO categoryDAO;
    private EventCategory testCategory;
    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;

    @BeforeClass
    public static void setUpClass(){
        categoryDAO = new EventCategoryDAOImpl();
    }

    @Before
    public void setUp() {
        testCategory = TestHelper.setUpTestCategory();
        testCategory.setId(TestHelper.insertTestCategory());
        try {
            conn = DataSourceManager.getInstance().getConnection();
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
        TestHelper.deleteTestCategory(testCategory.getId());
        testCategory = null;
        TestHelper.closeResources(rs, stmt, conn);
    }

    @Test
    public void testInsertCategory() throws SQLException {
        TestHelper.deleteTestCategory(testCategory.getId());
        categoryDAO.insertCategory(testCategory);
        EventCategory actualCategory = getTestCategory(testCategory.getId()+1);
        try{
            assertEquals(actualCategory.getTitle(), testCategory.getTitle());
            assertEquals(actualCategory.getDescription(), testCategory.getDescription());
       } finally {
           TestHelper.deleteTestCategory(testCategory.getId()+1);
       }
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
    }

    @Test
    public void testUpdateCategory() throws SQLException {
        EventCategory newCategory = new EventCategory(testCategory);
        newCategory.setDescription("New test description");
        categoryDAO.updateCategory(newCategory);
        EventCategory actualCategory = getTestCategory(testCategory.getId());
        assertEquals(actualCategory.getId(), testCategory.getId());
        assertEquals(actualCategory.getTitle(), newCategory.getTitle());
        assertEquals(actualCategory.getDescription(), newCategory.getDescription());
    }

    @Test
    public void testDeleteCategory() throws SQLException {
        categoryDAO.deleteCategory(testCategory.getId());
        assertNull(getTestCategory(testCategory.getId()));
    }

    //helper methods
    private EventCategory getTestCategory(int id) throws SQLException {
        String sqlStr = "SELECT * FROM event_category WHERE id  = ?";
        stmt = conn.prepareStatement(sqlStr);
        stmt.setInt(1, id);
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
