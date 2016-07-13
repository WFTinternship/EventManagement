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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by hermine on 7/9/16.
 */
public class TestEventCategoryDAOImpl {

    private static EventCategoryDAO categoryDAO;
    private EventCategory testCategory;

    @BeforeClass
    public static void setUpClass() {
        categoryDAO = new EventCategoryDAOImpl();
    }

    @Before
    public void setUp() {
        testCategory = TestHelper.createTestCategory();
        int categoryId = TestHelper.insertTestCategoryToDB(testCategory);
        testCategory.setId(categoryId);
    }

    @After
    public void tearDown() {
        TestHelper.deleteTestCategoryFromDB(testCategory.getId());
        testCategory = null;
    }

    @Test
    public void testInsertCategory(){
        TestHelper.deleteTestCategoryFromDB(testCategory.getId());
        int newCatId = categoryDAO.insertCategory(testCategory);
        EventCategory actualCategory = getTestCategory(newCatId);
        try {
            assertEquals(actualCategory.getTitle(), testCategory.getTitle());
            assertEquals(actualCategory.getDescription(), testCategory.getDescription());
        } finally {
            TestHelper.deleteTestCategoryFromDB(newCatId);
        }
    }

    @Test
    public void testGetAllCategories(){
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
    public void testGetCategoryById() {
        EventCategory actualCategory = categoryDAO.getCategoryById(testCategory.getId());

        assertEquals(actualCategory.getId(), testCategory.getId());
        assertEquals(actualCategory.getTitle(), testCategory.getTitle());
        assertEquals(actualCategory.getDescription(), testCategory.getDescription());
    }

    @Test
    public void testUpdateCategory()  {
        testCategory.setDescription("New test description");
        categoryDAO.updateCategory(testCategory);
        EventCategory actualCategory = getTestCategory(testCategory.getId());

        assertEquals(actualCategory.getId(), testCategory.getId());
        assertEquals(actualCategory.getTitle(), testCategory.getTitle());
        assertEquals(actualCategory.getDescription(), testCategory.getDescription());
    }

    @Test
    public void testDeleteCategory() {
        categoryDAO.deleteCategory(testCategory.getId());
        assertNull(getTestCategory(testCategory.getId()));
    }

    //helper methods
    private EventCategory getTestCategory(int id){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        EventCategory category = null;
        try {
            conn = DataSourceManager.getInstance().getConnection();
            String sqlStr = "SELECT * FROM event_category WHERE id  = ?";
            stmt = conn.prepareStatement(sqlStr);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            while (rs.next()) {
                category = new EventCategory();
                category.setId(rs.getInt("id"))
                        .setTitle(rs.getString("title"))
                        .setDescription(rs.getString("description"))
                        .setCreationDate(rs.getTimestamp("creation_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            TestHelper.closeResources(rs, stmt, conn);
        }
        return category;
    }

    private List<EventCategory> getAllCategories(){
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<EventCategory> categoriesList = new ArrayList<EventCategory>();
        try {
            conn = DataSourceManager.getInstance().getConnection();
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
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        } finally {
            TestHelper.closeResources(rs, stmt, conn);
        }
        return categoriesList;
    }
}
